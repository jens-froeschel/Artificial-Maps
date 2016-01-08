package de.JensF.Artificial_Maps;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.stage.Stage;

public class Main extends Application{

	private Map map;

	@Override
	public void start(Stage stage) throws Exception {
		
		NoiseGenerator heightGenerator = new NoiseGenerator(new Random().nextLong(), 40, 40);
		heightGenerator.smooth(6);
		heightGenerator.scale();

		NoiseGenerator temperatureGenerator = new NoiseGenerator(new Random().nextLong(), 40, 40);
		temperatureGenerator.smooth(3);
		temperatureGenerator.scale();

		NoiseGenerator rainfallGenerator = new NoiseGenerator(new Random().nextLong(), 40, 40);
		rainfallGenerator.smooth(1);
		rainfallGenerator.scale();
		
		map = new Map(heightGenerator, temperatureGenerator, rainfallGenerator, 500, 500);
		
		map.redraw();
		
		Group group = new Group(map.getCanvas());
		Scene scene = new Scene(group);
		stage.setScene(scene);
		
		scene.setOnScroll( e -> scroll(e));
		
		stage.show();
	}

	private void scroll(ScrollEvent e) {
		map.zoomIn(e.getX(), e.getY());
		map.redraw();
	}
	
	
	
	

	public static void main(String[] args) {
		Main.launch(args);
	}
}
