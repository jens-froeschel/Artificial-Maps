package de.JensF.Artificial_Maps.Math.Noise;

/**
 * Diese Klasse entspricht dem Ridged Noise Algorithmus beschrieben in Kapitel 2.3
 */
public class Ridged_Noise implements NoiseFunction, NoiseFunction3D{

	//Eine Instanz von Open Simplex Noise, welche fuer die Berechnung von Ridged Noise benoetigt wird.
	private OpenSimplexNoise noise;
	
	/** Die Instanz von Open Simplex Noise wird initialisiert */
	public Ridged_Noise(long seed, int size){
		this.noise = new OpenSimplexNoise(seed, size);
	}

	/** Diese Berechnung entspricht der in Kapitel 2.3 beschriebenen Berechnung von Ridged Nosie
	 * ridged(x,y) = 1 - |simplex(x,y)-0.5|*2 
	 */
	@Override
	public double getValue(double x, double y) {
		return 1 - Math.abs( noise.getValue(x, y)-0.5 )*2;
	}
	
	/** Diese Berechnung entspricht der in Kapitel 2.3 beschriebenen Berechnung von Ridged Nosie
	 * ridged(x,y,t) = 1 - |simplex(x,y,t)-0.5|*2 
	 */
	@Override
	public double getValue(double x, double y, double t) {
		return 1 - Math.abs( noise.getValue(x, y, t)-0.5 )*2;
	}

	/** Der Seed wurde geaendert */
	@Override
	public void setSeed(long seed) {
		noise.setSeed(seed);
	}
}