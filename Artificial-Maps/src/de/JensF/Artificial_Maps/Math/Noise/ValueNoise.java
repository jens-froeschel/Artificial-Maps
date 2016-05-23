package de.JensF.Artificial_Maps.Math.Noise;

import java.util.Random;

/**
 * Diese Klasse stellt eine Umsetzung des Value Noise Algorithmus dar.
 */
public class ValueNoise implements NoiseFunction3D{

	private int size;
	private double[][] arr;

	/**
	 * Im Konstruktor wird ein Raster von Zufallswerten erzeugt 
	 */
	public ValueNoise(int seed, int size){
		this.size = size;
		arr = new double[size][size];
		Random rand = new Random(seed);
		
		for(int y=0; y<size; y++)
			for(int x=0; x<size; x++)
				arr[x][y] = rand.nextDouble();
		
	}
	
	
	/**
	 * Diese Methode entspricht dem in Kapitel 2.1 beschriebenen Value Noise.<br>
	 * Er entspricht somit auch Algorithmus 1 aus Abbildung 2.1 <br> 
	 */
	public static double Interpolate2D(double x, double y, double[][] arr, int width, int height){
		x %= width;
		y %= height;
		int intX = (int)x;
		int intY = (int)y;
		double doubX = x-intX;
		double doubY = y-intY;
	
		
		int yp = intY==height-1? 0 : intY+1;
		int xp = intX==width-1? 0 : intX+1;
	
		
		
		double x1 = Interpolate1D(arr[intX][intY], arr[intX][yp], doubY);
		double x2 = Interpolate1D(arr[xp][intY], arr[xp][yp], doubY);
		return Interpolate1D( x1, x2, doubX );
	}
	
	/**
	 * Dies ist ein fuer die eindimensionale Interpolation verwendeter Algorithmus.<br>
	 * Weil die lineare Interpolation nicht sehr gut aussieht, wird hier eine Cosinus-Interpolation durchgefuehrt.
	 */
	public static double Interpolate1D(double a, double b, double d) {
		
		//Ein Winkel zwischen 0° und 180° wird fuer den Kosinus benoetigt und muss in Radians umgewandelt werden. 
		//Danach wird der Kosinus des Winkels berechnet.
		double deg = d*180;
		double cos = Math.cos(Math.toRadians(deg));
		
		//Weil Kosinus im Bereich [-1,1] liegt, wird er auf den Bereich [0,1] skaliert
		double trans = (cos+1)/2;
		
		//der mit dem Kosinus interpolierte Wert wird zurueckgegeben
		return a*trans + b*(1-trans);
	}

	/**
	 * Berechnet value(x,y)
	 */
	@Override
	public double getValue(double x, double y) {
		return Interpolate2D(x*size, y*size, arr, size, size);
	}

	/**
	 * Berechnet value(x,y,s), was nicht implementiert wurde.
	 */
	@Override
	public double getValue(double x, double y, double t) {
		return 0;
	}

	/**
	 * Die Methode wird nicht verwendet.
	 */
	@Override
	public void setSeed(long seed) {
		
	}
	
}
