package de.JensF.Artificial_Maps.Maps;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

/**
 * Die Klasse MapScreenStandard erweiter die Klasse MapScreen.<br>
 * Sie stellt eine einfachere und schnellere Implementierungsmoeglichkeit fuer visuelle Karten dar. <br>
 * Anstelle der setClimate Methode von MapScreen muss man in dieser Klasse die calculateColorByClimate Methode ueberschreiben.
 * Diese Methode bricht die bei setClimate uebergebenen Arrays auf und erhaelt nur noch die drei Klimawerte der einzelnen pixel als Eingabe.
 * Dadurch muss man nur noch angeben, welche Farbe die Karte bei gewissen Klimawerten haben soll.
 * Fuer moegliche Implementierungen siehe MapHeight, MapTemperature oder MapRainfall.
 *
 */
abstract class MapScreenStandard implements MapScreen{
	
	//Ein JavaFx Canvas und der PixelWirter, mit welchem man die Pixel des Canvas einfaerben kann.
	protected Canvas canvas = new Canvas();
	protected GraphicsContext g = canvas.getGraphicsContext2D();
	protected PixelWriter pixelWriter = g.getPixelWriter();
	
	//Die groesse des Canvas.
	protected double size;
	
	//Die Klimadaten als Arrays.
	protected double[][] height;
	protected double[][] rainfall;
	protected double[][] temperature;

	/**
	 * Diese Methode wird aufgerufen, wenn die Groesse des Canvas geaendert werden soll.
	 */
	@Override
	public void setSize(double size){
		if(size == this.size)
			return;
		
		this.size = size;
		canvas.setWidth(size);
		canvas.setHeight(size);
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn die Klimadaten geaendert wurden.
	 * Die Methode zeichnet die visuelle Karte neu.
	 */
	@Override
	public void setClimate(double[][] heightArray, double[][] rainfallArray, double[][] temperatureArray) {
		this.height = heightArray;
		this.rainfall = rainfallArray;
		this.temperature = temperatureArray;
		
		//Die Karte wird neu gezeichnet und als Monat wird der Januar angezeigt.
		redraw(0);
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn die Karte neu gezeichnet werden soll.<br>
	 * Die Eingabe ist der Monat, welcher angezeigt werden soll.<br>
	 * Es wird ueber alle Klimadaten itteriert und die Farbe der Pixel wird mithilfe von calculateColorByPosition festgelegt.<br>
	 * Zuletzt wird das Canvas neu gezeichnet.
	 */
	protected void redraw(int month){
		int isize = (int)size;
		//Buffer stellt einen Puffer mit den Farben der einzelnen Pixel dar.
		int buffer[] = new int[isize*isize];
		
		//Es wird ueber alle Klimadaten itteriert und die Farbe der Pixel wird mithilfe von calculateColorByPosition festgelegt.
		for(int i=0; i<isize*isize; i++)
			buffer[i] = calculateColorByClimate(height[i][month], rainfall[i][month], temperature[i][month]);
		
		//Das Canvas wird anhand der berechneten Farben neu gezeichnet
		pixelWriter.setPixels(0, 0, isize, isize, PixelFormat.getIntArgbInstance(), buffer, 0, isize);
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn der angezeigte Monat geaendert wurde.<br>
	 * Die visuelle Karte wird mit dem neuen Monat neu gezeichnet.
	 */
	@Override
	public void setMonth(int month) {
		if(height != null && rainfall != null && temperature != null)
			redraw(month);
	}
	
	/**
	 * Diese Methode muss ueberschrieben werden. Sie berechnet die Farbe einer Position anhand der drei Geodaten Hoehe, Temperatur und Niederschlag.
	 */
	abstract int calculateColorByClimate(double height, double rainfall, double temperature);
	
	/**
	 * Diese Methode gibt einen JavaFx Node wieder, welcher an der Stelle der Anzeigeflaeche angezeigt werden soll.
	 */
	@Override
	public Node getNode() {
		return canvas;
	}
}
