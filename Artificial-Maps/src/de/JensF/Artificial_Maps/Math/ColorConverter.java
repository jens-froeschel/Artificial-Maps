package de.JensF.Artificial_Maps.Math;

/**
 * Diese Klasse ist eine Bibliothek des Autors Jens Froeschel und kuemmert sich um die konvertierung zwischen verschiedenen Farbraeumen.<br>
 * Die hier umgesetzten Algorithmen sind nicht Teil der Bachelorarbeit und stellen nur ein Hilfsmittel fuer deren Umsetzung dar.
 */
public class ColorConverter {
	
	/**
	 * input: RGB values in range [0, 1] <br><p>
	 * output: [H, S, V] as HSV-variables <br>
	 * H in range [0,360]<br>
	 * S and V in range [0,1]
	 */
	public static double[] RGB_to_HSV(double red, double green, double blue){
		double max = Math2.max(red, green, blue);
		double min = Math2.min(red, green, blue);
		
		double h = 0;
		double s = 0;
		double v = 0;
		
		if(max == min)
			h = 0;
		else if(max == red)
			h = 60 * (green-blue)/(max-min);
		else if(max == green)
			h = 60 * ( 2 + (blue-red)/(max-min) );
		else if(max == blue)
			h = 60 * ( 4 + (red-green)/(max-min) );
		if( h < 0 )
			h += 360;
		
		if(max == 0)
			s = 0;
		else
			s = (max-min)/max;
		
		v = max;
		
		return new double[]{h, s, v};
	}

	
	/**
	 * input: HSV values<br>
	 * H in range [0, 360]<br>
	 * S and V in range [0,1]<br><p>
	 * Output: [R, G, B] in range [0, 1]
	 */
	public static double[] HSV_to_RGB(double h, double s, double v){
		int hi = (int)(h/60);
		double f = (h/60)-hi;
		double p = v*(1-s);
		double q = v*(1-s*f);
		double t = v*(1-s*(1-f));
		
		switch(hi){
		case 1: return new double[]{q, v, p};
		case 2: return new double[]{p, v, t};
		case 3: return new double[]{p, q, v};
		case 4: return new double[]{t, p, v};
		case 5: return new double[]{v, p, q};
		default: return new double[]{v, t, p};
		}
	}

	/**
	 * input: Integer representing this RGB <br><p>
	 * output: [L, A, B] as Hunter-Lab variables
	 */
	public static double[] Integer_to_LAB(int i){
		int[] RGB = Integer_to_RGB(i);
		return RGB_to_LAB(RGB[0], RGB[1], RGB[2]);
	}
	
	/**
	 * input: RGB values in range [0, 255] <br><p>
	 * output: [L, A, B] as Hunter-Lab variables
	 */
	public static double[] RGB_to_LAB(double red, double green, double blue){
		double[] XYZ = RGB_to_XYZ(red, green, blue);
		return XYZ_to_LAB(XYZ[0], XYZ[1], XYZ[2]);
	}
	
	/**
	 * input: [L, A, B] as Hunter-Lab variables <br><p>
	 * output: Integer representing this RGB
	 */
	public static int LAB_to_Integer(double L, double A, double B){
		double[] XYZ = LAB_to_XYZ(L, A, B);
		int[] RGB = XYZ_to_RGB(XYZ[0], XYZ[1], XYZ[2]);
		return intRGB_to_Integer(RGB[0], RGB[1], RGB[2]);
	}
	

	/**
	 * input: RGB values in range [0, 255] <br><p>
	 * output: double vector with 3 components [X, Y, Z] <br>
	 * in range [0, 95.047], [0, 100], [0, 108.883]
	 */
	public static double[] RGB_to_XYZ(double red, double green, double blue){
		double R = (double)red/255;
		double G = (double)green/255;
		double B = (double)blue/255;
		
		if(R>0.04045)
			R = Math.pow( (R+0.055)/1.055, 2.4);
		else 
			R = R / 12.92;
		
		if(G>0.04045)
			G = Math.pow( (G+0.055)/1.055, 2.4);
		else 
			G = G / 12.92;
		
		if(B>0.04045)
			B = Math.pow( (B+0.055)/1.055, 2.4);
		else 
			B = B / 12.92;
		
		R *= 100;
		G *= 100;
		B *= 100;
		
		double X = 0.412453 *R +  0.357580*G  + 0.180423*B;
		double Y = 0.212671 *R +  0.715160*G  + 0.072169*B;
		double Z = 0.019334 *R +  0.119193*G  + 0.950227*B;
		
		return new double[]{X, Y, Z};
	}
	
	
	/**
	 * input: XYZ values in range [0, 95.047], [0, 100], [0, 108.883]
	 * output: double vector with 3 components [R, G, B] in range [0,255]
	 */
	public static int[] XYZ_to_RGB(double X, double Y, double Z){
		X = X/100;
		Y = Y/100;
		Z = Z/100;
		
		double R = 3.240479*X - 1.537150*Y - 0.498535*Z;
		double G = -0.969256*X + 1.875992*Y + 0.041556*Z;
		double B = 0.055648*X - 0.204043*Y + 1.057311*Z;
		
		if( R>0.0031308 ) 
			R = 1.055 * Math.pow(R, 1/2.4) - 0.055;
		else        
			R = 12.92 * R;
		if( G>0.0031308 ) 
			G = 1.055 * Math.pow(G, 1/2.4) - 0.055;
		else        
			G = 12.92 * G;
		if( B>0.0031308 ) 
			B = 1.055 * Math.pow(B, 1/2.4) - 0.055;
		else        
			B = 12.92 * B;
		
		
		int red = (int) (R*255);
		int green = (int) (G*255);
		int blue = (int) (B*255);
				
		return new int[]{red, green, blue};
	}
	
	
	
	/**
	 * input: [X, Y, Z] <br><p>
	 * output: [L, A, B] as hunter-lab
	 */
	public static double[] XYZ_to_LAB(double X, double Y, double Z){
		double L = 10 * Math.sqrt(Y);
		double A = 17.5*(( (1.02 * X) -Y) / Math.sqrt(Y));
		double B = 7 * ( (Y - (0.847*Z)) / Math.sqrt(Y));
		if(Double.isNaN(L) || Double.isNaN(A) || Double.isNaN(B)){
			L=0; A=0; B=0;
		}
			
		return new double[]{L, A, B};
	}
	
	/**
	 * input: [L, A, B] as hunter-lab <br><p>
	 * output:[X, Y, Z]
	 */
	private static double[] LAB_to_XYZ(double L, double A, double B) {
		double Y = L/10;
		double X = A/17.5 * L/10;
		double Z = B/7 * L/10;
		
		Y = Math.pow(Y, 2);
		X = (X + Y) /1.02;
		Z = -( Z - Y) / 0.847;
		
		return new double[]{X, Y, Z};
	}
	
	
	/**
	 * Input: RGB in range [0, 255]
	 * Output: RGB in range [0,1]
	 */
	public static double[] intRGB_to_RGB(int R, int G, int B){
		return new double[]{R/255d, G/255d, B/255d};
	}

	/**
	 * Input: RGB in range [0, 1]
	 * Output: Integer representing this RGB
	 */
	public static int RGB_to_Integer(double r, double g, double b) {
		return intRGB_to_Integer((int)(r*255),(int)(g*255),(int)(b*255));
	}
	
	/**
	 * Input: RGB in range [0, 255]
	 * Output: Integer representing this RGB
	 */
	public static int intRGB_to_Integer(int R, int G, int B){
		return 0xFF000000 | (R<<16) | (G<<8) | B;
	}
	
	/**
	 * Input: Integer representing this RGB
	 * Output: [R, G, B] in range [0, 255]
	 */
	public static int[] Integer_to_RGB(int integer){
		return new int[]{ (integer & 0x00FF0000)>>16, (integer & 0x0000FF00)>>8, integer & 0x000000FF };
	}

}
