package de.JensF.Artificial_Maps.Math;

/**
 * Diese Klasse kuemmert sich um die Umwandlung von Rauschwerten in geowissenschaftliche Daten.
 *
 */
public class Climatology {

	// Der Meeresspiegel im Bereich [0,1]
	public static double SEA_LEVEL = 0.5;
	
	
	/**
	 * Die Umwandlung in Hoehenwerte basiert auf der in Kapitel 3.1 beschriebenen Hoehenfunktion
	 */
	public static double doubleToHeight(double d){
		d = Math2.logitScale5(d);
		
		return -8848+d*(8848*2);
	}
	
	
	/**
	 * Die Umwandlung in Temperaturwerte basiert auf der in Kapitel 3.2 beschriebenen Temperaturfunktion. <br>
	 * Der in Kapitel 3.2 beschriebene Waermeequator wird in der Funktion climatologyMapping mit einberechnet.
	 */
	public static double doubleToTemperature(double d){
		return -48+d*(48+57);
	}
	
	/**
	 * Die Umwandlung in Niederschlagswerte basiert auf der in Kapitel 3.3 beschriebenen Niederschlagsfunktion
	 */
	public static double doubleToRainfall(double d){
		double ret = -250+d*(200+650);
		if(ret < 0)
			ret = 0;
		return ret;
	}
	
	/**
	 * Diese Methode kuemmert sich um die Abhaengigkeit von Koordinaten und den einzelnen Geodaten untereinander.<br>
	 * Momentan wird lediglich der in Kapitel 3.2 beschriebene Hoehenequator berechnet.
	 */
	public static double[] climatologyMapping(double height, double rainfall, double temperature, double x, double y, double t){
		//Die y-Koordinate des Hoehenequators wird bestimmt
		double equatorLine = (t-2d/12d);
		if(equatorLine < 0)
			equatorLine+= 13d/12d;
		equatorLine = Math.abs(equatorLine - 0.5)*2;
		equatorLine = 0.55 - equatorLine*0.15;
		equatorLine = 1 - equatorLine;
		
		//Die Temperatur wird von dem Hoehenequator beeinflusst
		temperature = (temperature + (0.5-Math.abs(y-equatorLine))*2*0.8  )/1.8;
		
		return new double[]{height, rainfall, temperature};
		
	}
}
