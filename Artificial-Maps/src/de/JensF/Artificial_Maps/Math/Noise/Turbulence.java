package de.JensF.Artificial_Maps.Math.Noise;

/**
 * Diese Klasse stellt die in Kapitel 2.4 beschriebenen Turbulenzen dar.
 */
public class Turbulence implements NoiseFunction3D{
	
	//Die zu verwendende Rauschfunktion
	private NoiseFunction3D noise;
	
	//Die Anzahl der Oktaven
	private int octaves;

	
	/**
	 * Im Konstruktor wird die Anzahl der Oktaven und die zu verwendende Rauschfunktion festgelegt
	 */
	public Turbulence(NoiseFunction3D noise, int octaves) {
		this.octaves = octaves;
		this.noise = noise;
	}
	
	/**
	 * Diese Methode entspricht den im Kapitel 2.4 beschriebenen Algorithmus
	 */
	@Override
	public double getValue(double x, double y) {
		  
		//Die Frequenzen und Amplituden werden der Uebersichthalber im vorhinein berechnet
		double[] frequency = new double[octaves];
		double[] amplitude = new double[octaves];
		for(int i=0; i < octaves; i++){
			frequency[i] = Math.pow(1.92d, i);
			amplitude[i] = Math.pow(1d/2d, i);
		}
		
		//total ist die berechnete Summer und total Amplitude der maximal moegliche Wert, den total haben koennte
		double total = 0;
		double totalAmplitude = 0;

		//Die in Kapitel 2.4 beschriebene Summe wird berechnet
		for (int i = 0; i < octaves; i++) {
			total += noise.getValue(x*Math.pow(1.92d, i), y*Math.pow(1.92d, i)) * Math.pow(1d/2d, i);
			totalAmplitude += amplitude[i];
		}	
		
		//total wird wieder auf den Bereich [0,1] skaliert, da total groesser sein koennte als 1
		return total/totalAmplitude;
	}
	
	/**
	 * Diese Methode entspricht den im Kapitel 2.4 beschriebenen Turbulenzen Algorithmus
	 */
	@Override
	public double getValue(double x, double y, double t) {
		
		//Die Frequenzen und Amplituden werden der Uebersichthalber im vorhinein berechnet
		double[] frequency = new double[octaves];
		double[] amplitude = new double[octaves];
		
		for(int i=0; i < octaves; i++){
			frequency[i] = Math.pow(2d, i);
			amplitude[i] = Math.pow(1d/2d, i);
		}
		
		//total ist die berechnete Summer und total Amplitude der maximal moegliche Wert, den total haben koennte
		double total = 0;
		double totalAmplitude = 0;
		
		//Die in Kapitel 2.4 beschriebene Summe wird berechnet
		for (int i = 0; i < octaves; i++) {
			total += noise.getValue(x*frequency[i], y*frequency[i], t) * amplitude[i];
			totalAmplitude += amplitude[i];
		}	
		
		//total wird wieder auf den Bereich [0,1] skaliert, da total groesser sein koennte als 1
		return total/totalAmplitude;
	}
	
	/** Der Seed wurde geaendert */
	@Override
	public void setSeed(long seed) {
		noise.setSeed(seed);
	}
}
