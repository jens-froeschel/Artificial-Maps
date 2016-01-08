package de.JensF.Artificial_Maps;

public class Math2{

	public static double max(double... input) {
		if(input.length < 1)
			throw new IllegalArgumentException("input must have at least one value");
		
		double output = input[0];

		for(double d : input)
			if(d > output)
				output = d;
		
		return output;
	}
	
	public static double min(double... input) {
		if(input.length < 1)
			throw new IllegalArgumentException("input must have at least one value");
		
		double output = input[0];

		for(double d : input)
			if(d < output)
				output = d;
		
		return output;
	}
}
