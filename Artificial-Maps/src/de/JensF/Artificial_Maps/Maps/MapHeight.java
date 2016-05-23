package de.JensF.Artificial_Maps.Maps;

import de.JensF.Artificial_Maps.Math.ColorConverter;

/**
 * Diese Klasse repraesentiert die visuelle Hoehenkarte beschrieben in Kapitel 4.1.
 */
public class MapHeight extends MapScreenStandard{

	/**
	 * In dieser Methode wird die Farbe der Hoehenwerte bestimmt.<br>
	 * Die Farbe ist ein Verlauf von Weiss zu Schwarz in Graustufen (siehe Abbildung 4.2).<br>
	 * Die Hoehe stellt somit den Hellwert (value) des HSV-Farbraum dar und die Farbsaettigung (saturation) ist 0.<br>
	 */
	@Override
	int calculateColorByClimate(double height, double rainfall,	double temperature) {
		double h = 100;
		double s = 0;
		double v = height;
		
		//Umwandlung der HSV-Farbwerte in RGB-Farbwerte
		double[] rgb = ColorConverter.HSV_to_RGB(h,s,v);
		//Umwandlung der drei RGB-Farbwerte im Bereich [0,1] zu einem RGB-Integer
		return ColorConverter.RGB_to_Integer(rgb[0], rgb[1], rgb[2]);
	}
	
}
