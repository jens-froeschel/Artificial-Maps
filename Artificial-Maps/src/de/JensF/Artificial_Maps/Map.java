package de.JensF.Artificial_Maps;


import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

public class Map {

	
	private NoiseGenerator heightGenerator;
	private NoiseGenerator temperatureGenerator;
	private NoiseGenerator rainfallGenerator;
	
	private Canvas canvas = new Canvas();
	private GraphicsContext g = canvas.getGraphicsContext2D();
	private PixelWriter pixelWriter = g.getPixelWriter();

	//the zoom start and end position in range [0, 1]
	private double zoomStartX = 0;
	private double zoomStartY = 0;
	private double zoomEndX = 1;
	private double zoomEndY = 1;
	
	
	public Map(NoiseGenerator heightGenerator, NoiseGenerator temperatureGenerator, NoiseGenerator rainfallGenerator, int canvasWidth, int canvasHeight) {
		this.heightGenerator = heightGenerator;
		this.temperatureGenerator = temperatureGenerator;
		this.rainfallGenerator = rainfallGenerator;
		
		canvas.setWidth(canvasWidth);
		canvas.setHeight(canvasHeight);
	}
	
	public void setCanvasWidth(double canvasWidth){
		canvas.setWidth(canvasWidth);
	}
	
	public void setCanvasHeight(double canvasHeight){
		canvas.setHeight(canvasHeight);
	}
	
	
	public void zoomIn(double zoomPixelX, double zoomPixelY){
		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();
		
		double zoomStartX_old = zoomStartX;
		double zoomStartY_old = zoomStartY;
		
		double zoomWidth = zoomEndX - zoomStartX;
		double zoomx = zoomStartX + (zoomPixelX/canvasWidth)*zoomWidth;
		double zoomy = zoomStartY + (zoomPixelY/canvasHeight)*zoomWidth;
		
		System.out.println(zoomWidth+"   "+zoomx+"   "+zoomy);
		
		zoomStartX = zoomx - zoomWidth*0.42;
		zoomStartY = zoomy - zoomWidth*0.42;
		zoomEndX = zoomx + zoomWidth*0.42;
		zoomEndY = zoomy + zoomWidth*0.42;
		
		if(zoomStartX < 0){
			zoomEndX -= zoomStartX;
			zoomStartX = 0;
		}
		if(zoomStartY < 0){
			zoomEndY -= zoomStartY;
			zoomStartY = 0;
		}
		if(zoomEndX > 1){
			zoomStartX -= (zoomEndX - 1);
			zoomEndX = 1;
		}
		if(zoomEndY > 1){
			zoomStartY -= (zoomEndY - 1);
			zoomEndY = 1;
		}
		
		g.strokeRect(canvasWidth*(zoomStartX-zoomStartX_old), canvasHeight*(zoomStartY- zoomStartY_old), canvasWidth*0.8, canvasHeight*0.8);
	}
	
	public void redraw(){
		int canvasWidth = (int) canvas.getWidth();
		int canvasHeight = (int) canvas.getHeight();
		
		double zoomWidth = canvasWidth/(zoomEndX-zoomStartX);
		double zoomHeight = canvasHeight/(zoomEndY-zoomStartY);
		
		Task<int[]> task = new Task<int[]>() {
			@Override
			protected int[] call() throws Exception {
				int[] buffer = new int[canvasWidth*canvasHeight];
				
				for(int y = 0; y<canvasHeight; y++)
					for(int x=0; x<canvasWidth; x++){
						buffer[y*canvasWidth+x] = calculateColorByPosition(zoomStartX+((double)x/zoomWidth), zoomStartY+(double)y/zoomHeight);
					}
				System.out.println("Test");
				return buffer;
			}
		};
		
		task.setOnSucceeded( e -> {
			pixelWriter.setPixels(0, 0, canvasWidth, canvasHeight, PixelFormat.getIntArgbInstance(), task.getValue(), 0, canvasWidth);
		});
		
		Thread th = new Thread( task );
		th.start();
	}
	
	protected int calculateColorByPosition(double x, double y) {
		double height = heightGenerator.getValue(x, y);
		double temperature = temperatureGenerator.getValue(x, y);
		double rainfall = rainfallGenerator.getValue(x, y);
		
		
		double value = calculateColorByClimate(height, temperature, rainfall);
	//	Color col = Color.hsb(240+(value*60), 1, 1);
		
		return (int)value;//0xFF000000 | (int)(value*256);//+ (int)(col.getRed()*255*255*255+col.getGreen()*255*255+col.getBlue()*255);
	}

	
	
	
	private double calculateColorByClimate(double height, double temperature, double rainfall) {
		//System.out.println("Height: "+height+"  temp: "+temperature+"  rain:"+rainfall);
		
		Biom biom = Biom.LAND;
		
		if(height < 0.4)
			biom = Biom.OCEAN;
		else if(height > 0.9)
			biom = Biom.MOUNTAIN;
		
		
		
		double[] rgb;
		
		switch(biom){
		case LAND:   rgb = getLandColor(height, temperature, rainfall);
			break;
	//	case MOUNTAIN:  rgb = getMountainColor(height, temperature, rainfall);
	//		break;
		case OCEAN:  rgb = getOceanColor(height, temperature, rainfall);
			break;
		default:  rgb = new double[]{0,0,0};
			break;
		}
		//System.out.println("Red: "+(int)(255*rgb[0])+"  green: "+(int)(255*rgb[1])+"  blue:"+(int)(255*rgb[2]));
		
		return ColorConverter.RGB_to_Integer((int)(rgb[0]*255), (int)(rgb[1]*255), (int)(rgb[2]*255));
	}
	
	
	private double[] getLandColor(double height, double temperature, double rainfall) {

		double[] rgb = ColorConverter.HSV_to_RGB(60 + (1-height)*60,  1 - height, rainfall - 0.1);
//		System.out.println("getOceanColor "+(120 + (0.3-height)*120)+"  "+  (0.3 - height) +"   "+rainfall+" \t "+(int)(rgb[0]*255)+"  "+(int)(rgb[1]*255)+"  "+(int)(rgb[2]*255));
		return rgb;
	}

	private double[] getMountainColor(double height, double temperature, double rainfall) {
		double[] rgb = ColorConverter.HSV_to_RGB(120 + (1-height)*120,  1 - height, rainfall);
		return rgb;
	}

	private double[] getOceanColor(double height, double temperature, double rainfall){
		double[] rgb = ColorConverter.HSV_to_RGB(120 + (1.17-height)*120,  1 - height, rainfall-0.1);
//		if(height > 0.1)
//			System.out.println("getOceanColor "+(120 + (0.3-height)*120)+"  "+  (0.3 - height) +"   "+rainfall+" \t "+(int)(rgb[0]*255)+"  "+(int)(rgb[1]*255)+"  "+(int)(rgb[2]*255));
		return rgb;
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

enum Biom{
	OCEAN, LAND, MOUNTAIN;
}