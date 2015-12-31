package de.JensF.Artificial_Maps;

import java.util.Random;

public class NoiseGenerator {

	double[][] arr;
	int width = 0;
	int height = 0;
	
	
	public NoiseGenerator(long seed, int width, int height){
		if(width < 10)
			throw new IllegalArgumentException("The width must be greater or equal 10");
		if(height < 10)
			throw new IllegalArgumentException("The height must be greater or equal 10");
		
		this.width = width;
		this.height = height;
		arr = new double[width][height];
		Random rand = new Random(seed);
		
		for(int y=0; y<height; y++)
			for(int x=0; x<height; x++)
				arr[x][y] = rand.nextDouble();
	}


	public NoiseGenerator(double[][] arr) {
		this.arr = arr;
		
		this.width = arr.length;
		if(width < 10)
			throw new IllegalArgumentException("The array's width must be greater or equal 10");

		this.height = arr[0].length;
		if(height < 10)
			throw new IllegalArgumentException("The array's height must be greater or equal 10");
	}
	
	public void smooth(){
		arr = ValueNoise.smooth(arr);
	}

	public void scale(){
		arr = ValueNoise.scale(arr);
	}
	
	/**
	 * smoothes the array multiple times
	 * @param count how often the array shall be smoothed
	 */
	public void smooth(int count){
		for(int i=0; i<count; i++)
			smooth();
	}
	
	/**
	 * returns the Noise Value
	 * @param x the x coordinate in range [0-1]
	 * @param y the y coordinate in range [0-1]
	 * @return the calculated value
	 */
	public double getValue(double x, double y){
		return ValueNoise.noise(x*width, y*height, arr);
	}
}
