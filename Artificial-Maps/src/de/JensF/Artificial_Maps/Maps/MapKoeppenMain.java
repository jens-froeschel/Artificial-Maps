package de.JensF.Artificial_Maps.Maps;

import de.JensF.Artificial_Maps.Math.Climatology;
import de.JensF.Artificial_Maps.Math.ColorConverter;
import javafx.scene.image.PixelFormat;


/**
 * Diese Klasse stellt die Berechnung und Visualisierung der fuenf Hauptgruppen der Koeppenschen Klimaklassifikation dar (siehe Kapitel 3.4).
 * Die Koeppen-Klassifikation in Hauptgruppen ist:<br><br>
 * Gruppe   - Klima            - Farbe <br>
 * Gruppe A - Tropisch         - Rot <br>
 * Gruppe B - Trocken          - Gelb <br>
 * Gruppe C - Warm Gemaessigt  - Gruen <br>
 * Gruppe D - Kalt Gemaessigt  - Lila <br>
 * Gruppe E - Polar            - Grau <br>
 *
 */
public class MapKoeppenMain extends MapScreenStandard{
	
	/**
	 * Diese Methode wird aufgerufen, wenn die Karte neu gezeichnet werden soll.<br>
	 * Die Eingabe ist der Monat, welcher angezeigt werden soll.<br>
	 * Es wird ueber alle Klimadaten itteriert und die Farbe der Pixel wird mithilfe von calculateColorByPosition festgelegt.<br>
	 * Zuletzt wird das Canvas neu gezeichnet.
	 */
	protected void redraw(int month){
		int isize = (int)size;
		int buffer[] = new int[isize*isize];
		
		for(int position=0; position<isize*isize; position++){
			buffer[position] = calculateColorByPosition(position, month);
		}
		pixelWriter.setPixels(0, 0, isize, isize, PixelFormat.getIntArgbInstance(), buffer, 0, isize);
	}
	
	/**
	 * Diese Methode berechnet die Farbe eines Pixels anhand dessens Position und Geodaten.
	 * @param position Die gegebene Position. Sie enthaellt sowohl die x-, wie auch die y-Koordinate und kann direkt in height, temperature und downfall eingesetzt werden.
	 * @param month Der angezeigte Monat.
	 * @return Die Farbe als Integer-RGB Wert.
	 */
	private int calculateColorByPosition(int position, int month) {
		
		double[] rgb;

		
		if(height[position][month] < Climatology.SEA_LEVEL){
			//Meer
			rgb = getOceanColor(height[position][month], temperature[position][month], rainfall[position][month]);
		}else{
			//Festland
			
			//Die Koeppensche-Klimaklassifikation fuer die position wird berechnet.
			Koeppen_Climate climate = calculateClimateByPosition(position);
			
			//Anhand der Klimazone wird die passende Farbe ausgewaehlt und zurueckgegeben.
			switch (climate) {
			case CONTINENTAL: rgb = getContinentalColor(height[position][month], temperature[position][month], rainfall[position][month]);
				break;
			case DRY: rgb = getDryColor(height[position][month], temperature[position][month], rainfall[position][month]);
				break;
			case POLAR: rgb = getPolarColor(height[position][month], temperature[position][month], rainfall[position][month]);
				break;
			case TEMPERATE: rgb = getTemperateColor(height[position][month], temperature[position][month], rainfall[position][month]);
				break;
			case TROPICAL: rgb = getTropicalColor(height[position][month], temperature[position][month], rainfall[position][month]);
				break;
			default: rgb = new double[]{0, 0, 0};
				break;
			}
		}
		return ColorConverter.RGB_to_Integer(rgb[0], rgb[1], rgb[2]);
	}
	
	/** Die Farbe fuer die kalt gemaessigte Klimazone. */
	private double[] getContinentalColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(204, 44, 204);
	}

	/** Die Farbe fuer die polare Klimazone. */
	private double[] getPolarColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(99, 151, 253);
	}

	/** Die Farbe fuer die tropische Klimazone. */
	private double[] getTropicalColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(150, 0, 1);
	}
	
	/** Die Farbe fuer die trockene Klimazone. */
	private double[] getDryColor(double height, double temperature, double rainfall) {
		return ColorConverter.intRGB_to_RGB(255, 204, 0);
	}

	/** Die Farbe fuer die warm gemaessigte Klimazone. */
	private double[] getTemperateColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(10, 206, 10);
	}

	/** Die Farbe fuer das Meer. */
	private double[] getOceanColor(double height, double temperature, double rainfall){
		double[] rgb = ColorConverter.HSV_to_RGB(120 + (1.17-height)*120,  1 - height, rainfall);
		return rgb;
	}
	
	


	/**
	 * Diese Methode berechnet die Hauptgruppe der Koeppenschen-Klimaklassifikation fuer eine gegebene Position.
	 * @param pos Die gegebene Position. Sie enthaellt sowohl die x-, wie auch die y-Koordinate und kann direkt in height, temperature und downfall eingesetzt werden.
	 * @return Die Hauptgruppe der Koeppenschen-Klimaklassifikation.
	 */
	private Koeppen_Climate calculateClimateByPosition(int pos) {
	
		//Im folgenden werden die in Kapitel 3.4 beschriebenen, benoetigten Werte berechnet.
		
		//Die Temperatur in Grad Celsius fuer alle 12 Monate
		double[] degree = new double[12];
		//Der Niederschlag in Millimeter fuer alle 12 Monate
		double[] rain   = new double[12];
		
		//Die minimale und maximale Monatstemperatur
		double minTemp = 100000;
		double maxTemp = -100000;
		//Die durchschnittliche Monatstemperatur
		double avgTemp = 0;
		//Der minimale und maximale Monatsniederschlag
		double minRain = 100000;
		double maxRain = -100000;
		//Der Jahresniederschlag
		double annualPrecipitation = 0;
		
		//Die zuvor definierten Werte werden berechnet
		for(int m=0; m<12; m++){
			degree[m] = Climatology.doubleToTemperature( temperature[pos][m] );
			if( degree[m] < minTemp)
				minTemp = degree[m];
			if(degree[m] > maxTemp)
				maxTemp = degree[m];
			avgTemp += degree[m];
			
			rain[m] = Climatology.doubleToRainfall( rainfall[pos][m] );
			if(rain[m] < minRain)
				minRain = rain[m];
			if(rain[m] > maxRain)
				maxRain = rain[m];
			annualPrecipitation += rain[m];
			
		}
		avgTemp /= 12;
		
		
		//Im folgenden gilt fuer Sommer / Winter Werte:
		//Obere  Hemisphere: Sommermonate Apr, Mai, Jun, Jul, Aug, Sep
		//Obere  Hemisphere: Wintermonate Okt, Nov, Dec, Jan, Feb, Mar
		//Untere Hemisphere: Sommermonate Okt, Nov, Dec, Jan, Feb, Mar
		//Untere Hemisphere: Wintermonate Apr, Mai, Jun, Jul, Aug, Sep
		
		//Der minimale/maximale Niederschlag im Sommer/Winter
		double minRainSummer = 1000;
		double minRainWinter = 1000;
		double maxRainSummer = -1000;
		double maxRainWinter = -1000;
		//Der gesamte Niederschlag im Winter/Sommer
		double winterPerception = 0;
		double summerPerception = 0;
		//Der benoetigte Schwellenwert fuer die Temperatur
		double temperatureThreshold = 0;
		
		//Die zuvor definierten Werte werden berechnet
		if(pos < 0.5){
			//Obere Hemisphere Sommer
			for(int month=3; month<9; month++){
				if(rain[month] > maxRainSummer)
					maxRainSummer = rain[month];
				if(rain[month] < minRainSummer)
					minRainSummer = rain[month];
				summerPerception += rain[month];
			}
			
			//Obere Hemisphere Winter
			for(int month=0; month<3; month++){
				if(rain[month] > maxRainWinter)
					maxRainWinter = rain[month];
				if(rain[month] < minRainWinter)
					minRainWinter = rain[month];
				winterPerception += rain[month];
			}
			for(int month=9; month<12; month++){
				if(rain[month] > maxRainWinter)
					maxRainWinter = rain[month];
				if(rain[month] < minRainWinter)
					minRainWinter = rain[month];
				winterPerception += rain[month];
			}
		}else{
			//Untere Hemisphere Sommer
			for(int month=0; month<3; month++){
				if(rain[month] > maxRainSummer)
					maxRainSummer = rain[month];
				if(rain[month] < minRainSummer)
					minRainSummer = rain[month];
				summerPerception += rain[month];
			}
			for(int month=9; month<12; month++){
				if(rain[month] > maxRainSummer)
					maxRainSummer = rain[month];
				if(rain[month] < minRainSummer)
					minRainSummer = rain[month];
				summerPerception += rain[month];
			}

			//Untere Hemisphere Winter
			for(int month=3; month<9; month++){
				if(rain[month] > maxRainWinter)
					maxRainWinter = rain[month];
				if(rain[month] < minRainWinter)
					minRainWinter = rain[month];
				winterPerception += rain[month];
			}
		}
		
		//Der Schwellenwert wird berechnet
		if(winterPerception >= annualPrecipitation * 2d/3d)
			temperatureThreshold = 2 * avgTemp;
		else if(summerPerception >= annualPrecipitation * 2d/3d)
			temperatureThreshold = 2 * avgTemp + 28;
		else
			temperatureThreshold = 2 * avgTemp + 14;
		
		
		
		//Wenn Temperatur in allen Monat mehr als 18° betraegt, herrscht tropisches Klima
		if(minTemp > 18)
			return Koeppen_Climate.TROPICAL;
		
		
		//Wenn der Jahresniederschlag kleiner Zehn mal dem Schwellenwert ist, herrscht trockenes Klima
		if( annualPrecipitation < 10*temperatureThreshold )
			return Koeppen_Climate.DRY;
		
		
		//Wenn die monatliche minimale Temperatur zwischen -3° und 18° liegt, herrscht warm gemaessigtes Klima
		if(-3 < minTemp && minTemp < 18 )
			return Koeppen_Climate.TEMPERATE;
		
		
		//Wenn die monatlich minimale temperatur unter -3° und die maximale Temperatur ueber 18° liegt, herrscht kalt gemaessigtes Klima
		if(maxTemp >= 10 && minTemp < -3)
			return Koeppen_Climate.CONTINENTAL;
				
		
		//Wenn die monatliche maximale Temperatur unter 10° liegt, herrscht polares Klima
		if(maxTemp < 10)
			return Koeppen_Climate.POLAR;
						
			
		//Dieser Zustand duerfte niemals erreicht werden.
		return Koeppen_Climate.DRY;
	}
	
	/** Diese Methode wird nicht verwendet. */
	int calculateColorByClimate(double height, double rainfall,	double temperature) {
		return 0;
	}
	
}

/**
 * Ein enum mit den fuenf Hauptgruppen der Koeppenschen-Klimaklassifikation.
 */
enum Koeppen_Climate{
	TROPICAL, DRY, TEMPERATE, CONTINENTAL, POLAR;
}