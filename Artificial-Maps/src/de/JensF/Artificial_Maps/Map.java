package de.JensF.Artificial_Maps;

import java.util.ArrayList;

import de.JensF.Artificial_Maps.Maps.MapScreen;
import de.JensF.Artificial_Maps.Math.Climatology;
import de.JensF.Artificial_Maps.Math.Noise.NoiseFunction;
import de.JensF.Artificial_Maps.Math.Noise.NoiseFunction3D;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Die Klasse Map verwaltet und generiert die Karten.
 *
 */
public class Map {

	//Die aktiven Kartengeneratoren
	private NoiseFunction heightGenerator;
	private NoiseFunction3D temperatureGenerator;
	private NoiseFunction3D rainfallGenerator;
	
	//Eine Liste mit allen visuellen Karten
	private ArrayList<MapScreen> mapScreens = new ArrayList<>();
	
	//Ein Puffer mit den aktuellen Klimadaten
	//buffer[0] sind die Hoehendaten
	//buffer[1] sind die Temperaturdaten
	//buffer[2] sind die Niederschlagsdaten
	//buffer[1][a][b] sind die Temperaturdaten fuer den Pixel a im Monat b
	private double[][][] buffer;
	
	//Der linke/obere Rand des Zoombereichs die Werte liegen im Bereich [0,1]
	private double zoomOffsetX = 0;
	private double zoomOffsetY = 0;
	
	//Die Skalierung der angezeigten Karte (der Grad des Zooms) im Bereich [0, 1]
	private double scale = 1;
	//Die Groesse des Canvas, auf welches die Karte gezeichnet wird
	private double size = 0;
	//Der ausgewaehlte Monat
	private int month = 0;
	
	//Die Grafik und Position des Zoom-Rechtecks
	private Rectangle zoomRect = new Rectangle(0,0,0,0);
	private double zoomRectStartX;
	private double zoomRectStartY;
	
	//Der Hintergrundprozess fuer das Laden der Karten
	private Task<Void> task, task2;
	
	/**
	 * Der Konstruktor. Es werden die Farben des Zoom-Rechtecks eingestellt.
	 */
	public Map(){
		zoomRect.setFill(Color.TRANSPARENT);
		zoomRect.setStroke(Color.BLACK);
	}
	
	/**
	 * Die aktuellen Kartengeneratoren werden geaendert.
	 */
	public void setGenerators(NoiseFunction heightGenerator, NoiseFunction3D temperatureGenerator, NoiseFunction3D rainfallGenerator) {
		if(heightGenerator != null)
			this.heightGenerator = heightGenerator;
		if(temperatureGenerator != null)
			this.temperatureGenerator = temperatureGenerator;
		if(rainfallGenerator != null)
			this.rainfallGenerator = rainfallGenerator;
	}
	
	/**
	 * Die Liste mit den visuellen Karten wird uebergeben.
	 */
	public void addScreens(MapScreen... screens) {
		for(MapScreen screen: screens)
			mapScreens.add(screen);
	}
	
	/**
	 * Die aktuelle Groesse des Canvas wird geaendert.
	 */
	public void setSize(double size){
		if(size == this.size)
			return;
		
		this.size = size;
		
		for(MapScreen screen: mapScreens)
			screen.setSize(size);
		reload();
	}
	
	/**
	 * Der angezeigte Monat wird geaendert.
	 */
	public void setMonth(int month){
		if(this.month != month){
			this.month = month;
			
			for(MapScreen screen: mapScreens)
				screen.setMonth(month);
		}
	}
	
	/**
	 * Anhand der Rauschgeneratoren wird buffer mit Klimadaten gefuellt.<br>
	 *  Danach werden diese Klimadaten mittels der Hoehen-, Temperature- und Rauschfunktion an die Erde angenaehert
	 *  und zuletzt ueber die visuellen Karten grafisch dargestellt.
	 */
	public void reload(){
		//Die Groesse des Canvas als Integer Wert
		int isize = (int) size;
		
		//Falls ein neuladen Prozess bereits am laufen ist, wird dieser beendet
		if(task != null)
			task.cancel();
		
		//Der Prozess fuer das befuellen vom Bufffer wird erzeugt und gestartet
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {

				//Der Buffer wird geleert
				buffer = new double[3][isize*isize][12];
				
				double xpos, ypos;
				double[] climate;
				
				//Die maximalen und minimalen Klimawerte. Sie werden spaeter berechnet
				double minHeight = 1;
				double maxHeight = 0;
				double minTemp = 1;
				double maxTemp = 0;
				double minRain = 1;
				double maxRain = 0;

				//Eine Array mit 1/12 , 2/12, 3/12, ... , 11/12, 1 als Werte. 
				//Die Werte repraesentieren die 12 Monate des Jahres skaliert auf den Bereich [0,1]
				double[] scaledMonth = new double[12];
				for(int j=0; j<12; j++)
					scaledMonth[j] = j/11d;

				//Mit einer doppelten for-Schleife wird ueber jeden angezeigten Pixel itteriert
				for(int y = 0; y<isize; y++){
					for(int x=0; x<isize; x++){
						//Falls der Prozess unterbrochen wurde, wird die Schleife unterbrochen
						if(isCancelled())
							return null;
						else{
							//Die Position des angezeigten Pixels im Bereich [0,1] (der Zoom wird mit eingerechnet)
							xpos = zoomOffsetX + ((double)x/size)*scale;
							ypos = zoomOffsetY + ((double)y/size)*scale;
							
							//Eine Schleife ueber alle 12 Monate des Jahres
							for(int j=0; j<12; j++){

								//Die Klimadaten des Pixels in dem Monat werden berechnet und in Buffer gespeichert
								climate = calculateClimateByPosition(xpos, ypos, scaledMonth[j]);
								buffer[0][y*isize+x][j] = climate[0];
								buffer[1][y*isize+x][j] = climate[1];
								buffer[2][y*isize+x][j] = climate[2];
								
								//Die maximalen und minimalen Klimawerte werden bestimmt
								if(climate[0] > maxHeight)
									maxHeight = climate[0];
								if(climate[0] < minHeight)
									minHeight = climate[0];
								
								if(climate[1] > maxTemp)
									maxTemp = climate[1];
								if(climate[1] < minTemp)
									minTemp = climate[1];

								if(climate[2] > maxRain)
									maxRain = climate[2];
								if(climate[2] < minRain)
									minRain = climate[2];
							}
						}
					}
				}

				//Eine Schleife wie oben
				for(int y = 0; y<isize; y++){
					for(int x=0; x<isize; x++){
						if(isCancelled())
							return null;
						else{
							for(int j=0; j<12; j++){
								//Die Klimadaten werden gestreckt, damit der minimale Wert immer 0 ist und der maximale Wert 1
								buffer[0][y*isize+x][j] -= minHeight;
								buffer[0][y*isize+x][j] /= (maxHeight - minHeight);
								
								buffer[1][y*isize+x][j] -= minTemp;
								buffer[1][y*isize+x][j] /= (maxTemp - minTemp);

								buffer[2][y*isize+x][j] -= minRain;
								buffer[2][y*isize+x][j] /= (maxRain - minRain);
							}
						}
					}
				}
				return null;
			}
		};

		//Der folgende Code wird aufgerufen, wenn der obere Prozess fertig ist
		task.setOnSucceeded(e -> {
			if (buffer != null) {
				zoomRect.setVisible(false);

				//Den visuellen Karten werden die neuen Klimadaten uebergeben und die Karten werden neu berechnet
				for (MapScreen screen : mapScreens)
					screen.setClimate(buffer[0], buffer[1], buffer[2]);
			}
		});

		//Der gerade definierte Hintergrundprozess wird gestartet
		Thread th = new Thread( task );
		th.start();
	}

	/**
	 * Die Methode redraw berechnet nur die visuellen Karten neu und nicht alle Klimadaten.<br>
	 * Die Methode wird aufgerufen, wenn sich der Meeresspiegel aendert.
	 */
	public void redraw(){
		//Falls dieser Prozess bereits am laufen ist, wird er beendet
		if(task2 != null)
			task2.cancel();

		//Falls momentan eine neuberechnung der Klimadaten stattfindet, wird dieser Prozess nicht ausgefuehrt
		if( task != null && task.isRunning())
			return;
		
		//Der Prozess zum neuzeichnen wird definiert
		task2 = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if(buffer != null){
					zoomRect.setVisible(false);
					
					//Den visuellen Karten werden die neuen Klimadaten uebergeben und die Karten werden neu berechnet
					for(MapScreen screen: mapScreens)
						screen.setClimate(buffer[0], buffer[1], buffer[2]);
				}
				return null;
			}
		};
		
		//Der gerade definierte Hintergrundprozess wird gestartet
		Thread th = new Thread( task2 );
		th.start();
	}
	
	/**
	 * Berechnet die Klimadaten einer gegebenen Position.<br>
	 * 
	 * @param x Die x Koordinate des Pixels im Bereich [0,1]
	 * @param y Die y Koordinate des Pixels im Bereich [0,1]
	 * @param t Die Zeit / der Monat im Bereich [0,1]
	 * @return Die aktuellen Klimadaten [Hoehe, Temperatur, Niederschlag]
	 */
	private double[] calculateClimateByPosition(double x, double y, double t){
		double height = heightGenerator.getValue(x, y);
		double temperature = temperatureGenerator.getValue(x, y, t/Main.TIME_STATIC_VALUE_TEMPERATURE);
		double rainfall = rainfallGenerator.getValue(x, y, t/Main.TIME_STATIC_VALUE_RAINFALL);
		
		return Climatology.climatologyMapping(height, rainfall, temperature, x, y, t);
	}
	

	/**
	 *  Berechnet die Klimadaten eines gegebenen Pixels.<br>
	 *  Wird aufgerufen, wenn man mit der Maus auf die Karte klickt
	 *  
	 * @param x Die x-Koordinate der Maus
	 * @param y Die y-Koordinate der Maus
	 * @return Die aktuellen Klimadaten an der Position [Hoehe, Temperatur, Niederschlag]
	 */
	public double[] getClimateByMouseposition(double x, double y) {
		x = zoomOffsetX + (x/size)*scale;
		y = zoomOffsetY + (y/size)*scale;
		
		int ix = (int)(x*size);
		int iy = (int)(y*size);
		int isize = (int)size;
		
		if(iy*isize+ix >= buffer[0].length)
			return new double[]{0,0,0};
		
		return new double[]{buffer[0][iy*isize+ix][month], buffer[1][iy*isize+ix][month], buffer[2][iy*isize+ix][month] };
	}
	
	
	/*			Die folgenden Methoden werden fuer die Zoomfunktion benoetigt					*/

	/**
	 * Wird aufgerufen, wenn man an einen Pixel heranzoomt.
	 */
	public void zoomIn(double zoomMiddleX, double zoomMiddleY){
		
		zoomRectStartX = zoomMiddleX - 0.8*size/2;
		zoomRectStartY = zoomMiddleY - 0.8*size/2;

		zoomRect.setX(zoomRectStartX);
		zoomRect.setY(zoomRectStartY);
		zoomRect.setWidth(size*0.8);
		zoomRect.setHeight(size*0.8);

		zoomRect.setVisible(true);
		
		endZoomRect();
	}
	
	/**
	 * Wird aufgerufen, wenn man herauszoomt.
	 */
	public void zoomOut() {
		double zoomMiddleX = zoomOffsetX + scale/2;
		double zoomMiddleY = zoomOffsetY + scale/2;
		
		scale /= 0.8;
		if(scale >= 1)
			scale = 1;
		
		zoomOffsetX = zoomMiddleX - scale/2;
		zoomOffsetY = zoomMiddleY - scale/2;
		
		if(zoomOffsetX < 0){
			zoomOffsetX = 0;
		}
		if(zoomOffsetY < 0){
			zoomOffsetY = 0;
		}
		if(zoomOffsetX + scale > 1){
			zoomOffsetX = 1 - scale;
		}
		if(zoomOffsetY + scale > 1){
			zoomOffsetY = 1 - scale;
		}
		reload();
	}
	
	/**
	 * Wird aufgerufen, wenn man beginnt ein Zoom-Rechteck mit der Maus zu ziehen.
	 */
	public void startZoomRect(double x, double y) {
		zoomRect.setVisible(true);
		zoomRect.setX(x);
		zoomRect.setY(y);
		zoomRect.setWidth(0);
		zoomRect.setHeight(0);
		zoomRectStartX = x;
		zoomRectStartY = y;
	}

	/**
	 * Wird aufgerufen, wenn man das Zoom-Rechteck mit der Maus zieht.
	 */
	public void dragZoomRect(double x, double y) {
		double zoomSize = Math.max(Math.abs(zoomRectStartX - x), Math.abs(zoomRectStartY - y));
		
		if( zoomRectStartX < x)
			zoomRect.setX(zoomRectStartX);
		else
			zoomRect.setX(Math.max( zoomRectStartX - zoomSize, 0));
		
		if( zoomRectStartY < y)
			zoomRect.setY(zoomRectStartY);
		else
			zoomRect.setY(Math.max( zoomRectStartY - zoomSize, 0));
		
		zoomRect.setWidth(zoomSize);
		zoomRect.setHeight(zoomSize);
	}

	/**
	 * Wird aufgerufen, wenn man aufhoert das Zoom-Rechteck mit der Maus zu ziehen.
	 */
	public void endZoomRect(){
		double x1 = zoomRect.getX();
		double x2 = x1 + zoomRect.getWidth();
		double y1 = zoomRect.getY();
		
		if(zoomRect.getWidth() < 10){
			zoomRect.setVisible(false);
			return;
		}
		
		zoomOffsetX += (x1/size)*scale;
		zoomOffsetY += (y1/size)*scale;
		
		scale = ((x2-x1)*scale)/size;
		
		reload();
	}

	/**
	 * Gibt die Darstellung des Zoom-Rechtecks als JavaFx Node zurueck.
	 */
	public Node getNode(){
		return zoomRect;
	}

}