package de.JensF.Artificial_Maps.Maps;

import de.JensF.Artificial_Maps.Math.Climatology;
import de.JensF.Artificial_Maps.Math.ColorConverter;

/**
 * Diese Klasse repraesentiert die visuelle Niederschlagskarte beschrieben in Kapitel 4.3.
 */
public class MapRainfall extends MapScreenStandard{

	/**
	 * In dieser Methode wird die Farbe der Niederschlagswerte bestimmt.<br>
	 * Die Farbe besteht aus Stufenweisen Farbuebergaengen von Gelb zu Blau (siehe Abbildung 4.6).<br>
	 */
	@Override
	int calculateColorByClimate(double height, double rainfall,	double temperature) {
		double[] rgb;
		
		//Der Niederschlagswert wird in Millimeter umgerechnet
		rainfall = Climatology.doubleToRainfall(rainfall);
		
		//Der Pixel erhaelt eine Farbe abhaengig von dem Niederschlag in Millimetern
		if(rainfall == 0){
			rgb = new double[] {255,150,0};
		}else if(rainfall < 50){
			rgb = new double[] {255,200,0};
		}else if(rainfall < 100){
			rgb = new double[] {255,230,80};
		}else if(rainfall < 150){
			rgb = new double[] {255,255,9};
		}else if(rainfall < 200){
			rgb = new double[] {50, 200, 255};
		}else if(rainfall < 300){
			rgb = new double[] {0, 150, 255};
		}else if(rainfall < 400){
			rgb = new double[] {0, 85, 255};
		}else if(rainfall < 500){
			rgb = new double[] {0, 0, 255};
		}else{
			rgb = new double[] {0, 50, 150};
		}
		
		//Umwandlung der drei RGB-Farbwerte im Bereich [0,1] zu einem RGB-Integer
		return ColorConverter.RGB_to_Integer(rgb[0]/255d, rgb[1]/255d, rgb[2]/255d);
	}
	
}
