package de.JensF.Artificial_Maps;

import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

public class Map {

	
	private NoiseGenerator heightGenerator;
	
	private Canvas canvas = new Canvas();
	private GraphicsContext g = canvas.getGraphicsContext2D();
	private PixelWriter pixelWriter = g.getPixelWriter();

	public Map(NoiseGenerator heightGenerator, int canvasWidth, int canvasHeight) {
		this.heightGenerator = heightGenerator;
		
		canvas.setWidth(canvasWidth);
		canvas.setHeight(canvasHeight);
	}
	
	public void setCanvasWidth(double canvasWidth){
		canvas.setWidth(canvasWidth);
	}
	
	public void setCanvasHeight(double canvasHeight){
		canvas.setHeight(canvasHeight);
	}
	
	
	public void redraw(){
		int canvasWidth = (int) canvas.getWidth();
		int canvasHeight = (int) canvas.getHeight();
		
		
		
		Task<int[]> task = new Task<int[]>() {
			@Override
			protected int[] call() throws Exception {
				int[] buffer = new int[canvasWidth*canvasHeight];
				
				for(int y = 0; y<canvasHeight; y++)
					for(int x=0; x<canvasWidth; x++)
						buffer[y*canvasWidth+x] = calculateColor((double)x/canvasWidth, (double)y/canvasHeight);
					
				return buffer;
			}
		};
		
		task.setOnSucceeded( e -> {
			pixelWriter.setPixels(0, 0, canvasWidth, canvasHeight, PixelFormat.getIntArgbInstance(), task.getValue(), 0, canvasWidth);
		});
		
		Thread th = new Thread( task );
		th.start();
	}
	
	protected int calculateColor(double x, double y) {
		double value = heightGenerator.getValue(x, y);
		
		return 0xFF000000 | (int)(value*256);
	}

	public Canvas getCanvas(){
		return canvas;
	}
	
	
	
	
	public void vizualize1DInterpolation(){
		Task<int[]> task = new Task<int[]>() {
			@Override
			protected int[] call() throws Exception {
				int[] buffer = new int[500*500];
				
				double width = canvas.getWidth();
				double height = canvas.getHeight();
				for(int x=0; x<width; x++)
				{
					int y = (int) ValueNoise.Interpolate1D(0, height, (double)x/width);
					buffer[y*500+x] = 0xFF000000;
				}
				
				return buffer;
			}
		};

		task.setOnSucceeded( e -> {
			pixelWriter.setPixels(0, 0, 500, 500, PixelFormat.getIntArgbInstance(), task.getValue(), 0, 500);
		});
		
		Thread th = new Thread( task );
		th.start();
	}
	
	public void vizualize2DInterpolation(){
		Task<int[]> task = new Task<int[]>() {
			@Override
			protected int[] call() throws Exception {
				int[] buffer = new int[500*500];
				
				double width = canvas.getWidth();
				double height = canvas.getHeight();
				double value = 0;
				double[][] arr = new double[][]{
						{0, 1, 0}, {1, 0.5, 0}, {0.5, 1, 0}
				};
				
				for(int x=0; x<width; x++)
					for(int y=0; y<height; y++)
					{
						
						value =  ValueNoise.Interpolate2D((double)x/width, (double)y/width, arr);
						
						buffer[y*500+x] = 0xFF000000 | (int)(value*256);
					}
				
				return buffer;
			}
		};

		task.setOnSucceeded( e -> {
			pixelWriter.setPixels(0, 0, 500, 500, PixelFormat.getIntArgbInstance(), task.getValue(), 0, 500);
		});
		
		Thread th = new Thread( task );
		th.start();
	}

}
