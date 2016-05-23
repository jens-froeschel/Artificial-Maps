package de.JensF.Artificial_Maps.Math.Noise;

/**
 * Die Klasse NoiseFunction3D stellt eine Erweiterung der Klasse NoiseFunction dar.<br>
 * Die Erweiterung ist die Implementierung von dreidimensionalen Rauschfunktionen.<br>
 * Jede Rauschfunktion,welche fuer die Generierung von Temperatur- oder Niederschlagskarten 
 * verwendet werden soll, muss von dieser Klasse erben.
 * 
 */
public interface NoiseFunction3D extends NoiseFunction{
	
	/**
	 * Diese Methode ist implementierung der Berechnung des dreidimensionalen Rauschwertes noise(x,y,t)
	 * @param x die x-Koordinate im Bereich [0-1]
	 * @param y die y-Koordinate im Bereich [0,1]
	 * @param t die Zeit im Bereich [0,1]
	 * @return der berechnete Rauschwert
	 */
	public abstract double getValue(double x, double y, double t);

	/**
	 * Diese Methode wird aufgerufen, falls der verwendete Seed veraendert wurde 
	 * und die Rauschfunktionen neu berechnet werden sollen. 
	 */
	public abstract void setSeed(long seed);

}
