package de.JensF.Artificial_Maps;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		
		NoiseGenerator heightGenerator = new NoiseGenerator(new Random().nextLong(), 40, 40);
		heightGenerator.smooth(6);
		heightGenerator.scale();
		
		Map map = new Map(heightGenerator, 1000, 1000);
		
		map.redraw();
		
		Group group = new Group(map.getCanvas());
		Scene scene = new Scene(group);
		stage.setScene(scene);
		
		stage.show();
	}

	public static void main(String[] args) {
		Main.launch(args);
	}
}
