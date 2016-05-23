package de.JensF.Artificial_Maps.Math.Noise;

/**
 * Dieses Interface stellt das Grundgeruest fuer alle Raschfunktionen dar.<br>
 * Jede zwei- und dreidimensionale Rauschfunktion muss von NoiseFunction erben.
 */
public interface NoiseFunction {
	
	/**
	 * Diese Methode ist implementierung der Berechnung des zweidimensionalen Rauschwertes noise(x,y)
	 * @param x die x-Koordinate im Bereich [0-1]
	 * @param y die y-Koordinate im Bereich [0,1]
	 * @return der berechnete Rauschwert
	 */
	public abstract double getValue(double x, double y);

	/**
	 * Diese Methode wird aufgerufen, falls der verwendete Seed veraendert wurde 
	 * und die Rauschfunktionen neu berechnet werden sollen. 
	 */
	public abstract void setSeed(long seed);
}
