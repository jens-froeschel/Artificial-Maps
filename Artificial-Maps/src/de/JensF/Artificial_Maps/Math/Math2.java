package de.JensF.Artificial_Maps.Math;

/**
 * Diese Klasse stellt einige nuetzliche, mathematische Methoden zur Verfuegung
 */
public class Math2{

	/**
	 * Diese Methode bestimmt den maximalen Wert aus einer beliebigen Menge von Eingaben
	 */
	public static double max(double... input) {
		if(input.length < 1)
			throw new IllegalArgumentException("input must have at least one value");
		
		double output = input[0];

		for(double d : input)
			if(d > output)
				output = d;
		
		return output;
	}
	
	/**
	 * Diese Methode bestimmt den minmalen Wert aus einer beliebigen Menge von Eingaben
	 */
	public static double min(double... input) {
		if(input.length < 1)
			throw new IllegalArgumentException("input must have at least one value");
		
		double output = input[0];

		for(double d : input)
			if(d < output)
				output = d;
		
		return output;
	}
	
	/**
	 * Diese Funktion skaliert den Eingabewert anhand einer logistischen Funktion.<br>
	 * Die Eingabe wird anhand einer logistischen Funktion des  Bereiches [-5,5] skaliert.<br>
	 */
	public static double logitScale5(double input){
		//die Eingabe wird auf den Bereich [0.0067,0.9933] skaliert. Dadurch gilt -5 <= logit(input) <= 5
		input = 0.0067d + (0.9933-0.0067)*input;
		//logit(input) wird berechnet
		input = -Math.log(1d/input - 1);
		//die Eingabe wird aus dem Bereich [-5, 5] auf den Bereich [0,1] zurueck skaliert
		input +=5;
		input /= 10;
		return input;
	}
	
	
	/**
	 * Diese Funktion skaliert den Eingabewert anhand einer logistischen Funktion.<br>
	 * Die Eingabe wird anhand einer logistischen Funktion des  Bereiches [-3,3] skaliert.<br>
	 * Somit stellt diese Funktion die in Kapitel 3.1 beschriebene Umverteilung dar.
	 */
	public static double logitScale(double input){
		//die Eingabe wird auf den Bereich [0.0475, 0.9525] skaliert. Dadurch gilt -3 <= logit(input) <= 3
		input = 0.0475d + (0.9525d-0.0475d)*input;
		//logit(input) wird berechnet
		input = -Math.log(1d/input - 1d);
		//die Eingabe wird aus dem Bereich [-3, 3] auf den Bereich [0,1] zurueck skaliert
		input +=3;
		input /= 6;
		return input;
	}
	
	
	/**
	 * Diese Funktion skaliert den Eingabewert anhand einer sigmoid Funktion.<br>
	 * Die Eingabe wird anhand einer sogmoid Funktion des  Bereiches [-3,3] skaliert.<br>
	 */
	public static double sigmoidScale(double input){
		//die Eingabe wird auf den Bereich [-3, 3] skaliert. Dadurch gilt 0.04742 <= sigmoid(input) <= 0.95258
		input = -3+input*6;
		
		//sigmoid(input) wird berechnet
		input = 1d/(1d+Math.exp(-input));
		
		//die Eingabe wird aus dem Bereich [0.04742, 0.95258] auf den Bereich [0,1] zurueck skaliert
		input = (input-0.04742)/(0.95258-0.04742);
		return input;
	}
}
