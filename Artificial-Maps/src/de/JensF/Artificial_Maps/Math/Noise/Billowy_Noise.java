package de.JensF.Artificial_Maps.Math.Noise;

/**
 * Diese Klasse entspricht dem Billowy Noise Algorithmus beschrieben in Kapitel 2.3
 */
public class Billowy_Noise implements NoiseFunction, NoiseFunction3D{

	//Eine Instanz von Open Simplex Noise, welche fuer die Berechnung von Billowy Noise benoetigt wird.
	private OpenSimplexNoise noise;

	/** Die Instanz von Open Simplex Noise wird initialisiert */
	public Billowy_Noise(long seed, int size){
		this.noise = new OpenSimplexNoise(seed, size);
	}
	
	/** Diese Berechnung entspricht der in Kapitel 2.3 beschriebenen Berechnung von Billowy Nosie
	 * billowy(x,y) = |simplex(x,y)-0.5|*2 
	 */
	@Override
	public double getValue(double x, double y) {
		return Math.abs( noise.getValue(x, y)-0.5 )*2;
	}

	/** Diese Berechnung entspricht der in Kapitel 2.3 beschriebenen Berechnung von Billowy Nosie
	 * billowy(x,y,t) = |simplex(x,y,t)-0.5|*2 
	 */
	@Override
	public double getValue(double x, double y, double t) {
		return Math.abs( noise.getValue(x, y, t)-0.5 )*2;
	}

	/** Der Seed wurde geaendert */
	@Override
	public void setSeed(long seed) {
		noise.setSeed(seed);;
	}

}