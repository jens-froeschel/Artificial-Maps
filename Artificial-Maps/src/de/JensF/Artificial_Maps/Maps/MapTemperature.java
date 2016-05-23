package de.JensF.Artificial_Maps.Maps;

import de.JensF.Artificial_Maps.Math.ColorConverter;

/**
 * Diese Klasse repraesentiert die visuelle Temperaturkarte beschrieben in Kapitel 4.2.
 */
public class MapTemperature extends MapScreenStandard{

	/**
	 * In dieser Methode wird die Farbe der Temperaturwerte bestimmt.<br>
	 * Die Farbe besteht aus mehreren Verlaeufen, welche fluessig ineinander uebergehen (siehe Abbildung 4.4).<br>
	 */
	@Override
	int calculateColorByClimate(double height, double rainfall,	double temperature) {
		double h = 0;
		double s = 0;
		double v = 0;
		
		//Dieser Wert ist der Wert der Temperatur skaliert auf den Bereich [0,1] innerhalb des 1/8 Bereiches, in welchem sich die Temperatur befindet.
		//Wenn die Temperatur z.B. den Wert (3.45)/8 hat, waere der Wert 0.45
		double temp_eigth = 0;
		
		//Abhaengig von der Temperatur werden unterschiedlicher Abschnitte der Farbverlaeufe erzeugt.
		//Die Temperaturbereiche sind fuer jeden Farbverlauf 1/8 breit. 
		if(temperature < 1d/8d ){
			temp_eigth = temperature*8d;
			h = 305d - 10d * temp_eigth;
			s =  10d + 90d * temp_eigth;
			v =  95d +  5d * temp_eigth;
		}else if(temperature < 2d/8d ){
			temp_eigth = (temperature-1d/8d)*8d;
			h = 295d - 55d * temp_eigth;
			s = 100d;
			v = 100d;
		}else if(temperature < 3d/8d ){
			temp_eigth = (temperature-2d/8d)*8d;
			h = 240d - 65d * temp_eigth;
			s = 100d - 40d * temp_eigth;
			v = 100d;
		}else if(temperature < 4d/8d ){
			temp_eigth = (temperature-3d/8d)*8d;
			h = 175d - 55d * temp_eigth;
			s =  60d + 40d * temp_eigth;
			v = 100d -  5d * temp_eigth;
		}else if(temperature < 5d/8d ){
			temp_eigth = (temperature-4d/8d)*8d;
			h = 120d - 60d * temp_eigth;
			s = 100d - 30d * temp_eigth;
			v =  95d +  5d * temp_eigth;
		}else if(temperature < 6d/8d ){
			temp_eigth = (temperature-5d/8d)*8d;
			h =  60d - 25d * temp_eigth;
			s =  70d + 30d * temp_eigth;
			v = 100d;
		}else if(temperature < 7d/8d ){
			temp_eigth = (temperature-6d/8d)*8d;
			h =  35d - 35d * temp_eigth;
			s = 100d;
			v = 100d;
		}else {
			temp_eigth = (temperature-7d/8d)*8d;
			h = 0d;
			s = 100d;
			v = 100d - 30d * temp_eigth;
		}
		
		s /= 100;
		v /= 100;
			
		//Umwandlung der HSV-Farbwerte in RGB-Farbwerte
		double[] rgb = ColorConverter.HSV_to_RGB(h,s,v);
		//Umwandlung der drei RGB-Farbwerte im Bereich [0,1] zu einem RGB-Integer
		return ColorConverter.RGB_to_Integer(rgb[0], rgb[1], rgb[2]);
	}
	
}
