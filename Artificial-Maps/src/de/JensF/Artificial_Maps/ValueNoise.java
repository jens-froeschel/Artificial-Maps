package de.JensF.Artificial_Maps;

public class ValueNoise {

	
	
	
	
	/**
	 * smoothes the array and scales the values to be between 0 and 1
	 * @param arr
	 * @return
	 */
	public static double[][] smooth(double[][] arr) {
		if (arr == null)
			return arr;

		int width = arr.length;

		if (width <= 0)
			return arr;

		int height = arr[0].length;
		
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				arr[x][y] = SmoothNoise(x, y, arr);

		
		return arr;
	}

	private static double SmoothNoise(int x, int y, double[][] arr) {
		double corners = (arrWert(x-1, y-1, arr) + arrWert(x+1, y - 1, arr) + arrWert(x-1, y+1, arr) + arrWert( x + 1, y + 1, arr)) / 16;
		double sides = (arrWert(x - 1, y, arr) + arrWert(x + 1, y, arr)+ arrWert(x, y - 1, arr) + arrWert(x, y + 1, arr)) / 8;
		double center = arrWert(x, y, arr) / 4;
		return corners + sides + center;
	}
	
	/**
	 * scales the array to contain values in range [0, 1]<br>
	 * the array is scaled to contain a 0 and a 1
	 * @param arr the array to be scaled
	 * @return the scaled array
	 */
	public static double[][] scale(double[][] arr){
		if (arr == null)
			return arr;

		int width = arr.length;

		if (width <= 0)
			return arr;

		int height = arr[0].length;
		
		
		double minValue = 1;
		double maxValue = 0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				if (arr[x][y] < minValue)
					minValue = arr[x][y];
				if (arr[x][y] > maxValue)
					maxValue = arr[x][y];
			}
		

		double scale = 1d/(maxValue - minValue);
		double minScaled = minValue * scale;
	

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				arr[x][y] = arr[x][y]*scale - minScaled;
		
		return arr;
	}

	/**
	 * gibt den Wert des Arrays an der Position (x,y) wieder<br>
	 * 
	 */
	private static double arrWert(int x, int y, double[][] arr) {
		int width = arr.length;
		int height = arr[0].length;

		while (x < 0)
			x = width - x;
		while (x >= width)
			x = x - width;
		while (y < 0)
			y = height - y;
		while (y >= height)
			y = y - height;
		return arr[x][y];
	}
	
	
	/**
	 * calculates the 2-dimensional value noise for the point (x,y)
	 * @param x the x coordinate in the range of [0, arr.length]
	 * @param y the y coordinate in the range of [0, arr.length]
	 * @param arr the array
	 * @return the value noise for (x,y)
	 */
	  public static double noise(double x, double y, double[][] arr){  
		  int octave = 8;
		  double[] frequency = new double[octave];
		  double[] amplitude = new double[octave];
		  

		  for(int i=0; i < octave; i++){
			  frequency[i] = Math.pow(2d, i);
			  amplitude[i] = Math.pow(1d/2d, i);
		  }
		  
		  return noise(x, y, arr, octave, frequency, amplitude);
	  }
	
	  
	public static double noise(double x, double y, double[][] arr, int octaves, double[] frequency, double[] amplitude) {
		if (frequency.length != octaves)
			throw new IllegalArgumentException("frequency length must be equal to number of octaves");
		if (amplitude.length != octaves)
			throw new IllegalArgumentException("amplitude length must be equal to number of octaves");

		double total = 0;

		double totalAmplitude = 0;

		for (int i = 0; i < octaves; i++) {
			total += Interpolate2D(x * frequency[i], y * frequency[i], arr)
					* amplitude[i];
			totalAmplitude += amplitude[i];
		}

		if (total / totalAmplitude < 0 || total / totalAmplitude > 1)
			System.out.println("Error: noise returns value out of range [0,1]! Value: " + (total / totalAmplitude));

		return total / totalAmplitude;
	}
	
	/**
	 * calculates the 2-Dimensional Interpolation of the Points x and y
	 * @param x the x coordinate of the Point scaled to the array's length
	 * @param y the y coordinate of the Point scaled to the array's length
	 * @param arr the array 
	 * @return the value of the point (x,y)
	 */
	public static double Interpolate2D(double x, double y, double[][] arr){
		int intX = (int)x;
		int intY = (int)y;
		double doubX = x-intX;
		double doubY = y-intY;
		
		double x1 = Interpolate1D(arrWert(intX, intY, arr), arrWert(intX, intY+1, arr), doubY);
		double x2 = Interpolate1D(arrWert(intX + 1, intY, arr), arrWert(intX + 1, intY+1, arr), doubY);
		return Interpolate1D( x1, x2, doubX );
	}
  
	/**
	 * calculates a 1-Dimensional cosinus-interpolation between the Points a and b
	 * @param a value of the left point
	 * @param b value of the right point
	 * @param d between 0 and 1 determining if the point is nearer to a or b. 
	 * d = 0 means the point to interpolate ist a and d = 1 means the point is b.
	 * @return the value of the point between a and b
	 */
	public static double Interpolate1D(double a, double b, double d) {
		
		//for the cosinus interpolation an angle between 0 and 180° is needed
		double deg = d*180;
		
		//since Math.cos works with radians not degrees we have to transform the value
		double cos = Math.cos(Math.toRadians(deg));
		
		//since cosinus(angle) is in the range [-1, 1] we transform it to a value between 0 and 1
		double trans = (cos+1)/2;
		
		//we return the interpolation of a and b
		return a*trans + b*(1-trans);
	}
	
}
