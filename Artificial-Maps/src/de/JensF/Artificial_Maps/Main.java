package de.JensF.Artificial_Maps;

import java.util.HashMap;
import java.util.Random;

import de.JensF.Artificial_Maps.Maps.MapHeight;
import de.JensF.Artificial_Maps.Maps.MapKoeppenAll;
import de.JensF.Artificial_Maps.Maps.MapKoeppenMain;
import de.JensF.Artificial_Maps.Maps.MapRainfall;
import de.JensF.Artificial_Maps.Maps.MapScreen;
import de.JensF.Artificial_Maps.Maps.MapTemperature;
import de.JensF.Artificial_Maps.Math.Climatology;
import de.JensF.Artificial_Maps.Math.Noise.Billowy_Noise;
import de.JensF.Artificial_Maps.Math.Noise.Turbulence;
import de.JensF.Artificial_Maps.Math.Noise.NoiseFunction;
import de.JensF.Artificial_Maps.Math.Noise.NoiseFunction3D;
import de.JensF.Artificial_Maps.Math.Noise.OpenSimplexNoise;
import de.JensF.Artificial_Maps.Math.Noise.Ridged_Noise;
import de.JensF.Artificial_Maps.Math.Noise.ValueNoise;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Die Main-Klasse stellt den Startpunkt und das Zentrum des Programms dar.<br>
 * Sie verwaltet die vorhandenen Rauschfunktionen und visuellen Karten.<br>
 * Ausserdem kuemmert sich die Main-Klasse um das Interface und die Interaktion zwischen dem Anwender und dem Programm.<br>
 */
public class Main extends Application{


	//Konstanten, welche angeben wie stark sich die Temperatur und der Niederschlag ueber das Jahr verteilt aendern
	public static final double TIME_STATIC_VALUE_TEMPERATURE = 1.5;
	public static final double TIME_STATIC_VALUE_RAINFALL = 8;

	//Rastergroesse fuer die generierung der Rauschkarten
	public static final int HEIGHT_MAP_SIZE = 8;
	public static final int TEMPERATURE_MAP_SIZE = 10;
	public static final int RAINFALL_MAP_SIZE = 4;

	//Anzahl Oktaven fuer die Iterative Anwendung von Noise Funktionen
	public static final int HEIGHT_MAP_OCTAVES = 8;
	public static final int TEMPERATURE_MAP_OCTAVES = 5;
	public static final int RAINFALL_MAP_OCTAVES = 5;
	
	//Instanz der Klasse Map
	private Map map = new Map();
	
	//Die unterschiedlichen visuellen Karten (sie werden in dem statischen Block unten registriert)
	private MapHeight mapHeight = new MapHeight();
	private MapTemperature mapTemp = new MapTemperature();
	private MapRainfall mapRain = new MapRainfall();
	private MapScreen mapKoeppen = new MapKoeppenMain();
	private MapScreen mapKoeppen2 = new MapKoeppenAll();
	
	//Alles was mit dem Interface zu tun hat
	private Scene scene;
	private Pane mapGroup = new Pane();
	private VBox buttonBox = new VBox();
	private GridPane gridPane = new GridPane();
	private Label heightLabel = new Label("Height = ?");
	private Label rainLabel = new Label("Rainfall = ?");
	private Label tempLabel = new Label("Temperature = ?");
	private Label heightGeneratorBoxLabel = new Label("Height Generator:");
	private Label temperatureGeneratorBoxLabel = new Label("Temperature Generator:");
	private Label rainfallGeneratorBoxLabel = new Label("Rainfall Generator:");
	private ComboBox<String> heightGeneratorBox = new ComboBox<String>();
	private ComboBox<String> temperatureGeneratorBox = new ComboBox<String>();
	private ComboBox<String> rainfallGeneratorBox = new ComboBox<String>();
	private VBox comboBoxBox = new VBox(
			new VBox(heightGeneratorBoxLabel, heightGeneratorBox), 
			new VBox(temperatureGeneratorBoxLabel, temperatureGeneratorBox), 
			new VBox(rainfallGeneratorBoxLabel, rainfallGeneratorBox) );
	private Label seedLabel = new Label("Seed");
	private TextField seedField = new TextField();
	private Button generateRandomButton = new Button("Random");
	private HBox seedFieldBox = new HBox(seedField, generateRandomButton);
	private Button generateButton = new Button("Generate");
	private VBox seedBox = new VBox(seedLabel, seedFieldBox, generateButton);
	private Label seaLevelLabel = new Label("Sea Level: ");
	private Slider seaLevelSlider = new Slider(0, 1, 0.4);
	private HBox seaLevelHbox = new HBox(seaLevelLabel, seaLevelSlider);
	private HBox monthBox = new HBox();
	private Button[] monthButton = new Button[12];
	String[] monthName = new String[]{"Jan","Feb","Mar","Apr","Mai","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	
	//Drei HashMap's mit den registrierten Rauschfunktionen
	private HashMap<String, NoiseFunction> heightGenerators = new HashMap<>();
	private HashMap<String, NoiseFunction3D> temperatureGenerators = new HashMap<>();
	private HashMap<String, NoiseFunction3D> rainfallGenerators = new HashMap<>();

	//Der fuer die Generierung momentan verwendete Seed
	private long seed;
	
	//Ein statischer Block in dem alle Rauschfunktionen und visuellen Karten registriert werden
	{
		//Rauschfunktionen fuer die generierung von Hoehenkarten werden registriert
		heightGenerators.put("Simplex Noise", new OpenSimplexNoise(0, HEIGHT_MAP_SIZE));
		heightGenerators.put("Simplex Turbulence", new Turbulence(new OpenSimplexNoise(0, HEIGHT_MAP_SIZE), HEIGHT_MAP_OCTAVES));
		heightGenerators.put("Ridged Noise", new Ridged_Noise(0, HEIGHT_MAP_SIZE) );
		heightGenerators.put("Ridged Turbulence", new Turbulence( new Ridged_Noise(0, HEIGHT_MAP_SIZE), HEIGHT_MAP_OCTAVES ));
		heightGenerators.put("Billowy Noise", new Billowy_Noise(0, HEIGHT_MAP_SIZE));
		heightGenerators.put("Billowy Turbulence", new Turbulence( new Billowy_Noise(0, HEIGHT_MAP_SIZE), HEIGHT_MAP_OCTAVES ));
		heightGenerators.put("Value Noise", new ValueNoise(13, HEIGHT_MAP_SIZE));
		heightGenerators.put("Value Turbulence", new Turbulence( new ValueNoise(0, HEIGHT_MAP_SIZE), HEIGHT_MAP_OCTAVES ));

		//Rauschfunktionen fuer die generierung von Temperaturkarten werden registriert
		temperatureGenerators.put("Simplex Noise", new OpenSimplexNoise(0, TEMPERATURE_MAP_SIZE));
		temperatureGenerators.put("Simplex Turbulence", new Turbulence(new OpenSimplexNoise(0, TEMPERATURE_MAP_SIZE), TEMPERATURE_MAP_OCTAVES));
		temperatureGenerators.put("Ridged Noise", new Ridged_Noise(0, TEMPERATURE_MAP_SIZE) );
		temperatureGenerators.put("Ridged Turbulence", new Turbulence( new Ridged_Noise(0, TEMPERATURE_MAP_SIZE), TEMPERATURE_MAP_OCTAVES ));
		temperatureGenerators.put("Billowy Noise", new Billowy_Noise(0, TEMPERATURE_MAP_SIZE));
		temperatureGenerators.put("Billowy Turbulence", new Turbulence( new Billowy_Noise(0, TEMPERATURE_MAP_SIZE), TEMPERATURE_MAP_OCTAVES ));

		//Rauschfunktionen fuer die generierung von Niederschlagskarten werden registriert
		rainfallGenerators.put("Simplex Noise", new OpenSimplexNoise(0, RAINFALL_MAP_SIZE));
		rainfallGenerators.put("Simplex Turbulence", new Turbulence(new OpenSimplexNoise(0, RAINFALL_MAP_SIZE), RAINFALL_MAP_OCTAVES));
		rainfallGenerators.put("Ridged Noise", new Ridged_Noise(0, RAINFALL_MAP_SIZE) );
		rainfallGenerators.put("Ridged Turbulence", new Turbulence( new Ridged_Noise(0, RAINFALL_MAP_SIZE), RAINFALL_MAP_OCTAVES ));
		rainfallGenerators.put("Billowy Noise", new Billowy_Noise(0, RAINFALL_MAP_SIZE));
		rainfallGenerators.put("Billowy Turbulence", new Turbulence( new Billowy_Noise(0, RAINFALL_MAP_SIZE), RAINFALL_MAP_OCTAVES ));

		
		//Die visuellen Darstellungen von Karten werden registriert
		registerMapScreen(mapHeight, "Height Map");
		registerMapScreen(mapTemp, "Temperature Map");
		registerMapScreen(mapRain, "Rainfall Map");
		registerMapScreen(mapKoeppen, "Koeppen Main Groups");
		registerMapScreen(mapKoeppen2, "Koeppen All Groups");
	}
	
	/**
	 * Die start-Methode wird aufgerufen, nachdem JavaFx initialisiert wurde.
	 * In der Methode wird das Interface erstellt und Benutzereingaben werden abgefangen.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		//Es wird beim ersten Start ein zufaelliger Seed gewaehlt
		seed = new Random().nextLong();
		seedField.setText(""+seed);

		//Die anfaengliche Groesse wird auf 550 gesetzt und zu beginn wird die Hoehenkarte angezeigt
		map.setSize(550);
		showMapscreen(mapHeight);
		
		initialiseGUI();
		
		//Zuletzt wird das Fenster angezeigt und die Karten werden generiert
		stage.setScene(scene);
		stage.show();
		
		reloadMap();
	}

	/**
	 * In dieser Methode wird das GUI initialisiert und ausgerichtet.<br>
	 * Ausserdem wird festgelegt, was Buttons ausfuehren
	 */
	private void initialiseGUI() {
		//Die Generatoren fuer die Hoehenkarten werden initialisiert
		for( String s : heightGenerators.keySet() )
			heightGeneratorBox.getItems().add(s);
		heightGeneratorBox.setOnAction( e -> reloadMap() );
		heightGeneratorBox.getSelectionModel().select(4);
		
		//Die Generatoren fuer die Temperaturkarten werden initialisiert
		for( String s : temperatureGenerators.keySet() )
			temperatureGeneratorBox.getItems().add(s);
		temperatureGeneratorBox.setOnAction( e -> reloadMap() );
		temperatureGeneratorBox.getSelectionModel().select(4);
		
		//Die Generatoren fuer die Niederschlagskarten werden initialisiert
		for( String s : rainfallGenerators.keySet() )
			rainfallGeneratorBox.getItems().add(s);
		rainfallGeneratorBox.setOnAction( e -> reloadMap());
		rainfallGeneratorBox.getSelectionModel().select(4);
		
		
		//Das Anzeigemenue links wird initialisiert
		comboBoxBox.setSpacing(10);
		comboBoxBox.setAlignment(Pos.CENTER);
		seedBox.setAlignment(Pos.CENTER);
		
		seaLevelHbox.setSpacing(5);
		seaLevelSlider.valueProperty().addListener( e -> {
			Climatology.SEA_LEVEL = seaLevelSlider.getValue();
			map.redraw();
		});
		
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		
		VBox infoBox = new VBox(heightLabel, rainLabel, tempLabel);
		infoBox.setAlignment(Pos.BOTTOM_CENTER);
		infoBox.setSpacing(10);
		
		VBox menuBox = new VBox(comboBoxBox, seaLevelHbox, buttonBox, infoBox, seedBox);
		menuBox.setSpacing(40);
		menuBox.setPadding(new Insets(10,10,10,10));
		menuBox.setMinWidth(220);
		menuBox.setAlignment(Pos.CENTER);
		
		//Die Monatsauswahl wird initialisiert
		for(int i=0; i<12; i++){
			int j = i;
			monthButton[i] = new Button(monthName[i]);
			monthButton[i].setOnAction( e -> showMonth(j));
		}
		
		monthBox.setAlignment(Pos.CENTER);
		monthBox.getChildren().addAll(monthButton);
		
		//Das gesamte Layout des Fensters wird festgelegt
		GridPane.setConstraints(menuBox, 1, 1, 1, 2);
		GridPane.setConstraints(monthBox, 2, 1);
		GridPane.setConstraints(mapGroup, 2, 2);
		GridPane.setHgrow(mapGroup, Priority.ALWAYS);
		GridPane.setVgrow(mapGroup, Priority.ALWAYS);
		
		gridPane.getChildren().addAll(menuBox, monthBox, mapGroup);
		
		scene = new Scene(gridPane);
		
		//Benutzerinteraktionen werden abgefangen und verarbeitet.
		scene.setOnScroll( e -> scroll(e));
		mapGroup.heightProperty().addListener( e -> resize());
		mapGroup.widthProperty().addListener( e -> resize());
		
		mapGroup.setOnMousePressed( e -> mousePressed(e));
		mapGroup.setOnMouseDragged( e -> mouseDragged(e));
		mapGroup.setOnMouseReleased( e -> mouseReleased(e));
		mapGroup.setOnMouseClicked( e -> mouseClicked(e));
		
		generateRandomButton.setOnAction( e -> generateRandomSeed());
		generateButton.setOnAction( e -> reloadMap());
	}

	/**
	 * Diese Methode wird aufgerufen, wenn die angezeigten Karten neu generiert werden sollen.<br>
	 * Dies ist beispielsweise der Fall wenn sich der Seed oder die Fenstergroesse aendert.
	 */
	private void reloadMap() {
		//Die angezeigten Informationen ueber den aktuellen Seed werden aus dem Eingabefeld uebernommen
		try{
			seed = Integer.valueOf(seedField.getText());
		}catch(NumberFormatException e){
			seed = seedField.getText().hashCode();
		}
		seedLabel.setText("Seed = "+seed);

		//Anhand des anfaenglichen Seeds werden mit Hilfe eines deterministischen Zufallszahlengenerators weitere, eindeutige Zufallszahlen generiert.
		Random rand = new Random(seed);
		
		//Es werden die momentan aktiven Rauschfunktionen fuer die Generierung der Hoehen-, Temperatur- und Niederschlagskarte ausgewaehlt
		//und anhand einer Zufallszahl als Seed neu berechnet.
		NoiseFunction f_height = heightGenerators.get(heightGeneratorBox.getSelectionModel().getSelectedItem());
		f_height.setSeed(rand.nextLong());
		NoiseFunction3D f_temp = temperatureGenerators.get(temperatureGeneratorBox.getSelectionModel().getSelectedItem());
		f_temp.setSeed(rand.nextLong());
		NoiseFunction3D f_rain = rainfallGenerators.get(rainfallGeneratorBox.getSelectionModel().getSelectedItem());
		f_rain.setSeed(rand.nextLong());
		
		//Zuletzt werden Map die neuen Rauschfunktionen uebergeben und alle Karten werden anhand der Funktionen neu berechnet
		map.setGenerators(f_height, f_temp, f_rain);
		map.reload();
	}
	
	/**
	 * Diese Methode aendert den ausgewaehlten Monat.
	 * i=0 entspricht Januar und i=11 ist Dezember.
	 */
	private void showMonth(int month) {
		map.setMonth(month);
	}


	/**
	 * Diese Methode zeigt in dem Eingabefeld fuer den Seed eine zufaellige Zahl an
	 */
	private void generateRandomSeed() {
		seedField.setText( "" + (new Random().nextInt()) );
	}
	
	/**
	 * Diese Methode muss fuer jede visuelle Karte aufgerufen werden, damit die Karte registriert wird und verwendet werden kann.
	 * @param screen Die visuelle Karte
	 * @param name Eine Bezeichnung fuer die Karte
	 */
	public void registerMapScreen(MapScreen screen, String name) {
		Button button = new Button(name);
		map.addScreens(screen);
		button.setOnAction( e -> showMapscreen(screen));
		buttonBox.getChildren().add(button);
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn die angezeigte visuelle Karte geaendert wurde.
	 * @param screen Die visuelle Karte, welche angezeigt werden soll.
	 */
	private void showMapscreen(MapScreen screen){
		mapGroup.getChildren().clear();
		mapGroup.getChildren().addAll(screen.getNode(), map.getNode());
	}

	/**
	 * Diese Methode wird aufgerufen, wenn sich die Groesse des angezeigten Fensters veraendert hat.<br>
	 * Alle Karten werden daraufhin neu berechnet.
	 */
	private void resize() {
		int size = (int) Math.min(mapGroup.getLayoutBounds().getWidth(), mapGroup.getLayoutBounds().getHeight());
		map.setSize(size);
	}

	
	/**
	 * Diese Methode wird aufgerufen, wenn mit dem Mausrad in die Karte herein und herausgezoomt wird.<br>
	 */
	private void scroll(ScrollEvent e) {
		if(e.getDeltaY() > 0)
			map.zoomIn(e.getX(), e.getY());
		else if( e.getDeltaY() < 0)
			map.zoomOut();
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn eine Maustaste gedrueckt wurde.<br>
	 */
	private void mousePressed(MouseEvent e) {
		map.startZoomRect(e.getX(), e.getY());
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn eine Maustaste gedrueckt gehalten und die Maus bewegt wurde.<br>
	 */
	private void mouseDragged(MouseEvent e) {
		map.dragZoomRect(e.getX(), e.getY());
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn eine Maustaste losgelassen wurde.<br>
	 */
	private void mouseReleased(MouseEvent e) {
		map.endZoomRect();
	}

	/**
	 * Diese Methode wird aufgerufen, wenn eine Maustaste geclickt wurde.<br>
	 * In dem Fall werden alle Informationen ueber den angeclickt Punkt angezeigt.
	 */
	private void mouseClicked(MouseEvent e) {
		double[] climate = map.getClimateByMouseposition(e.getX(), e.getY());
		double height = Math.round(climate[0]*100)/100d;
		double rain = Math.round(climate[1]*100)/100d;
		double temp = Math.round(climate[2]*100)/100d;
		
		double heightM = Math.round(Climatology.doubleToHeight(height));
		double rainMM = Math.round(Climatology.doubleToRainfall(rain));
		double tempC = Math.round(Climatology.doubleToTemperature(temp));
		
		heightLabel.setText("Height = "+height+" ( "+heightM+"m)");
		rainLabel.setText("Rainfall = "+rain+" ( "+rainMM+"mm )");
		tempLabel.setText("Temperature = "+temp+" ( "+tempC+"°C )");
	}

	
	
	
	/**
	 * Die Main-Methode stellt den Startpunkt des Programms dar. Sie initialisiert JavaFx woraufhin die Methode {@code Start }aufgerufen wird.
	 */
	public static void main(String[] args) {
		Main.launch(args);
	}
}
