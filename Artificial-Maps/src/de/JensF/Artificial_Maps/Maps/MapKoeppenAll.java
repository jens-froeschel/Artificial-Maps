package de.JensF.Artificial_Maps.Maps;

import de.JensF.Artificial_Maps.Math.Climatology;
import de.JensF.Artificial_Maps.Math.ColorConverter;
import javafx.scene.image.PixelFormat;

/**
 * Diese Klasse repraesentiert die Koeppen-Karte mit allen 32 Gruppen.<br>
 * Die Koeppensche-Klimaklassifikation basiert auf „World Map of the Koeppen-Geiger climate classification updated“.<br>
 *
 */
public class MapKoeppenAll extends MapScreenStandard{
	
	/**
	 * Diese Methode wird aufgerufen, wenn die Karte neu gezeichnet werden soll.<br>
	 * Die Eingabe ist der Monat, welcher angezeigt werden soll.<br>
	 * Es wird ueber alle Klimadaten itteriert und die Farbe der Pixel wird mithilfe von calculateColorByPosition festgelegt.<br>
	 * Zuletzt wird das Canvas neu gezeichnet.
	 */
	protected void redraw(int month){
		int isize = (int)size;
		int buffer[] = new int[isize*isize];
		
		for(int i=0; i<isize*isize; i++){
			buffer[i] = calculateColorByPosition(i, month);
		}
		pixelWriter.setPixels(0, 0, isize, isize, PixelFormat.getIntArgbInstance(), buffer, 0, isize);
	}
	
	/**
	 * Diese Methode berechnet die Farbe eines Pixels anhand dessens Position und Geodaten.
	 * @param position Die gegebene Position. Sie enthaellt sowohl die x-, wie auch die y-Koordinate und kann direkt in height, temperature und downfall eingesetzt werden.
	 * @param month Der angezeigte Monat.
	 * @return Die Farbe als Integer-RGB Wert.
	 */
	private int calculateColorByPosition(int i, int month) {
		
		double[] rgb;

		
		if(height[i][month] < Climatology.SEA_LEVEL){
			//Meer
			rgb = getOceanColor(height[i][month], temperature[i][month], rainfall[i][month]);
		}else{
			// Festland
			
			//Die Koeppensche-Klimaklassifikation fuer die position wird berechnet.
			Koeppen_Climate2 climate = calculateClimateByPosition(i);

			//Anhand der Klimazone wird die passende Farbe ausgewaehlt und zurueckgegeben.
			switch (climate) {
			case RAINFOREST:
				rgb = getRainforestColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case MONSOON:
				rgb = getMonsoonColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case SAVANNAH_DRY_SUMMER:
				rgb = getSavannahSummerColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case SAVANNAH_DRY_WINTER:
				rgb = getSavannahWinterColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;

			case STEPPE_HOT:
				rgb = getSteppeHotColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case STEPPE_COLD:
				rgb = getSteppeColdColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DESERT_HOT:
				rgb = getDesertHotColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DESERT_COLD:
				rgb = getDesertColdColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			
			
			case CSA:
				rgb = getCSAColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case CSB:
				rgb = getCSBColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case CSC:
				rgb = getCSCColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case CFA:
				rgb = getCFAColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case CFB:
				rgb = getCFBColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case CFC:
				rgb = getCFCColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case CWA:
				rgb = getCWAColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case CWB:
				rgb = getCWBColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case CWC:
				rgb = getCWCColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
	
				
			case DSA:
				rgb = getDSAColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DSB:
				rgb = getDSBColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DSC:
				rgb = getDSCColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DSD:
				rgb = getDSDColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DFA:
				rgb = getDFAColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DFB:
				rgb = getDFBColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DFC:
				rgb = getDFCColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DFD:
				rgb = getDFDColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DWA:
				rgb = getDWAColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DWB:
				rgb = getDWBColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DWC:
				rgb = getDWCColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case DWD:
				rgb = getDWDColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;

			
			case TUNDRA:
				rgb = getTundraColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			case FROST:
				rgb = getFrostColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;

			case ERROR:
				rgb = getErrorColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			default:
				rgb = getErrorColor(height[i][month], temperature[i][month], rainfall[i][month]);
				break;
			}
		}

		return ColorConverter.RGB_to_Integer(rgb[0], rgb[1], rgb[2]);
	}

	/** Die Farbe fuer Klimazonen, welche nicht existieren sollten. */
	private double[] getErrorColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(0, 0, 0);
	}

	/** Die Farbe fuer die Regenwaelder. */
	private double[] getRainforestColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(150, 0, 1);
	}
	/** Die Farbe fuer die Monsun-Klimazone. */
	private double[] getMonsoonColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(253, 0, 2);
	}
	/** Die Farbe fuer die Savanne mit trockenen Sommern. */
	private double[] getSavannahSummerColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(255, 153, 156);
	}
	/** Die Farbe fuer die Savanne mit trockenen Wintern. */
	private double[] getSavannahWinterColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(254, 204, 203);
	}
	

	/** Die Farbe fuer die warme Steppe. */
	private double[] getSteppeHotColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(203, 141, 20);
	}
	/** Die Farbe fuer die kalte Steppe. */
	private double[] getSteppeColdColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(204, 169, 85);
	}
	/** Die Farbe fuer die warme Wueste. */
	private double[] getDesertHotColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(255, 204, 0);
	}
	/** Die Farbe fuer die kalte Wueste. */
	private double[] getDesertColdColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(255, 254, 101);
	}

	/** Die Farbe fuer die CSA Klimazone. */
	private double[] getCSAColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(0, 255, 1);
	}
	/** Die Farbe fuer die CSB Klimazone. */
	private double[] getCSBColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(150, 255, 2);
	}
	/** Die Farbe fuer die CSC Klimazone. */
	private double[] getCSCColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(199, 255, 0);
	}
	/** Die Farbe fuer die CFA Klimazone. */
	private double[] getCFAColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(0, 50, 1);
	}
	/** Die Farbe fuer die CFB Klimazone. */
	private double[] getCFBColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(1, 80, 0);
	}
	/** Die Farbe fuer die CFC Klimazone. */
	private double[] getCFCColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(0, 121, 0);
	}
	/** Die Farbe fuer die CWA Klimazone. */
	private double[] getCWAColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(182, 100, 1);
	}
	/** Die Farbe fuer die CWB Klimazone. */
	private double[] getCWBColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(149, 101, 1);
	}
	/** Die Farbe fuer die CWC Klimazone. */
	private double[] getCWCColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(89, 60, 2);
	}
	

	/** Die Farbe fuer die DSA Klimazone. */
	private double[] getDSAColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(255, 108, 255);
	}
	/** Die Farbe fuer die DSB Klimazone. */
	private double[] getDSBColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(254, 180, 255);
	}
	/** Die Farbe fuer die DSC Klimazone. */
	private double[] getDSCColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(229, 200, 255);
	}
	/** Die Farbe fuer die DSD Klimazone. */
	private double[] getDSDColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(200, 201, 203);
	}
	/** Die Farbe fuer die DFA Klimazone. */
	private double[] getDFAColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(52, 0, 51);
	}
	/** Die Farbe fuer die DFB Klimazone. */
	private double[] getDFBColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(101, 1, 100);
	}
	/** Die Farbe fuer die DFC Klimazone. */
	private double[] getDFCColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(202, 0, 200);
	}
	/** Die Farbe fuer die DFD Klimazone. */
	private double[] getDFDColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(199, 21, 133);
	}
	/** Die Farbe fuer die DWA Klimazone. */
	private double[] getDWAColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(201, 180, 255);
	}
	/** Die Farbe fuer die DWB Klimazone. */
	private double[] getDWBColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(165, 128, 175);
	}
	/** Die Farbe fuer die DWC Klimazone. */
	private double[] getDWCColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(134, 90, 179);
	}
	/** Die Farbe fuer die DWD Klimazone. */
	private double[] getDWDColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(111, 35, 183);
	}
	

	/** Die Farbe fuer die Tundra-Klimazone. */
	private double[] getTundraColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(101, 255, 255);
	}

	/** Die Farbe fuer die Frost-Klimazone. */
	private double[] getFrostColor(double height, double temperature, double rainfall){
		return ColorConverter.intRGB_to_RGB(99, 151, 253);
	}

	/** Die Farbe fuer das Meer. */
	private double[] getOceanColor(double height, double temperature, double rainfall){
		double[] rgb = ColorConverter.HSV_to_RGB(120 + (1.17-height)*120,  1 - height, rainfall);
		return rgb;
	}
	
	


	/**
	 * Die Methode berechnet die Klimazone der Koeppenschen-Klimaklassifikation fuer eine gegebene Position.
	 * @param pos Die gegebene Position. Sie enthaellt sowohl die x-, wie auch die y-Koordinate und kann direkt in height, temperature und downfall eingesetzt werden.
	 * @return Die Klimazone der Koeppenschen-Klimaklassifikation.
	 */
	private Koeppen_Climate2 calculateClimateByPosition(int pos) {

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
		
		//Diese Regel gibt an, ob mindestens 4 monatliche Durchschnittstemperaturen > 10°C betragen.
		boolean fourMonthRule = false;
		//Dieser Counter zaeglt, in wie vielen Monaten die Durchschnittstemperatur > 10°C ist.
		int fourMonthCount = 0;

		//Die zuvor definierten Werte werden berechnet	
		for(int m=0; m<12; m++){
			degree[m] = Climatology.doubleToTemperature( temperature[pos][m] );
			if( degree[m] < minTemp)
				minTemp = degree[m];
			if(degree[m] > maxTemp)
				maxTemp = degree[m];
			avgTemp += degree[m];
			
			if(degree[m] >= 10)
				fourMonthCount++;
			
			rain[m] = Climatology.doubleToRainfall( rainfall[pos][m] );
			if(rain[m] < minRain)
				minRain = rain[m];
			if(rain[m] > maxRain)
				maxRain = rain[m];
			annualPrecipitation += rain[m];
			
		}
		avgTemp /= 12;
		fourMonthRule = fourMonthCount >= 4;
	
		
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
		//Die weiteren Berechnungen der Untergruppen erfolgen nach der Koeppenschen-Klimaklassifikation beschrieben in Kapitel 3.4
		if(minTemp > 18){
			if( minRain > 60)
				return Koeppen_Climate2.RAINFOREST;
			if(annualPrecipitation > 25*(100-minRain))
				return Koeppen_Climate2.MONSOON;
			
			if(minRain == minRainSummer)
				return Koeppen_Climate2.SAVANNAH_DRY_SUMMER;
			
			if(minRain == minRainWinter)
				return Koeppen_Climate2.SAVANNAH_DRY_WINTER;
			
			return Koeppen_Climate2.ERROR;
		}
		
		
		//Wenn der Jahresniederschlag kleiner Zehn mal dem Schwellenwert ist, herrscht trockenes Klima
		//Die weiteren Berechnungen der Untergruppen erfolgen nach der Koeppenschen-Klimaklassifikation beschrieben in Kapitel 3.4
		if( annualPrecipitation < 10*temperatureThreshold ){
			if(annualPrecipitation > 5*temperatureThreshold)
				if(avgTemp >= 18)
					return Koeppen_Climate2.STEPPE_HOT;
				else
					return Koeppen_Climate2.STEPPE_COLD;
			
			if(annualPrecipitation <= 5*temperatureThreshold){
				if(avgTemp >= 18)
					return Koeppen_Climate2.DESERT_HOT;
				else
					return Koeppen_Climate2.DESERT_COLD;
			}
			return Koeppen_Climate2.ERROR;
		}
		
		
		//Wenn die monatliche minimale Temperatur zwischen -3° und 18° liegt, herrscht warm gemaessigtes Klima
		//Die weiteren Berechnungen der Untergruppen erfolgen nach der Koeppenschen-Klimaklassifikation beschrieben in Kapitel 3.4
		if(-3 < minTemp && minTemp < 18 ){
			//C
			if( minRainSummer < minRainWinter || (maxRainWinter > 3*minRainSummer && minRainSummer < 40) ){
				//CS
				if( maxTemp >= 22)
					return Koeppen_Climate2.CSA;
				if(fourMonthRule)
					return Koeppen_Climate2.CSB;
				return Koeppen_Climate2.CSC;
			}else if( minRainWinter < minRainSummer && maxRainSummer > 10*minRainWinter){
				//CW
				if( maxTemp >= 22)
					return Koeppen_Climate2.CWA;
				if(fourMonthRule)
					return Koeppen_Climate2.CWB;
				return Koeppen_Climate2.CWC;
			}else{
				//CF
				if( maxTemp >= 22)
					return Koeppen_Climate2.CFA;
				if(fourMonthRule)
					return Koeppen_Climate2.CFB;
				return Koeppen_Climate2.CFC;
			}
		}
		
		
		//Wenn die monatlich minimale temperatur unter -3° und die maximale Temperatur ueber 18° liegt, herrscht kalt gemaessigtes Klima
		//Die weiteren Berechnungen der Untergruppen erfolgen nach der Koeppenschen-Klimaklassifikation beschrieben in Kapitel 3.4
		if(maxTemp >= 10 && minTemp < -3){
			//D
			if( minRainSummer < minRainWinter || (maxRainWinter > 3*minRainSummer && minRainSummer < 40) ){
				//DS
				if( maxTemp >= 22)
					return Koeppen_Climate2.DSA;
				if(fourMonthRule)
					return Koeppen_Climate2.DSB;
				if(minTemp > -38)
					return Koeppen_Climate2.DSC;
				return Koeppen_Climate2.DSD;
			}else if( minRainWinter < minRainSummer && maxRainSummer > 10*minRainWinter){
				//DW
				if( maxTemp >= 22)
					return Koeppen_Climate2.DWA;
				if(fourMonthRule)
					return Koeppen_Climate2.DWB;
				if(minTemp > -38)
						return Koeppen_Climate2.DWC;
					return Koeppen_Climate2.DWD;
			}else{
				//DF
				if( maxTemp >= 22)
					return Koeppen_Climate2.DFA;
				if(fourMonthRule)
					return Koeppen_Climate2.DFB;
				if(minTemp > -38)
					return Koeppen_Climate2.DFC;
				return Koeppen_Climate2.DFD;
			}
		}	
		
		
		
		//Wenn die monatliche maximale Temperatur unter 0° liegt, herrscht polares Frost-Klima
		if(maxTemp < 0)
			return Koeppen_Climate2.FROST;
		
		//Wenn die monatliche maximale Temperatur zwischen 0° und 10° liegt, herrscht polares Tundra-Klima
		if(maxTemp < 10)
			return Koeppen_Climate2.TUNDRA;
					
			
		return Koeppen_Climate2.ERROR;
	}

	/** Diese Methode wird nicht verwendet. */
	int calculateColorByClimate(double height, double rainfall,	double temperature) {
		return 0;
	}
	
}

/**
 * Ein enum mit allen 31 Klimazonen der Koeppenschen-Klimaklassifikation.
 */
enum Koeppen_Climate2{
	ERROR, 
	RAINFOREST, MONSOON, 
	SAVANNAH_DRY_SUMMER, SAVANNAH_DRY_WINTER, STEPPE_HOT, STEPPE_COLD, DESERT_HOT, DESERT_COLD, 
	CSA, CSB, CSC, CWA, CWB, CWC, CFA, CFB, CFC, 
	DSA, DSB, DSC, DSD, DFA, DFB, DFC, DFD,DWA, DWB, DWC, DWD, 
	TUNDRA, FROST;
}