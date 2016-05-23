package de.JensF.Artificial_Maps.Maps;

import javafx.scene.Node;

/**
 * Diese Klasse stellt das Grundgeruest fuer alle visuellen Karten dar und jede visuelle Karte muss von ihr erben.<br>
 * 
 */
public interface MapScreen {

	/**
	 * Diese Methode wird aufgerufen, wenn sich die Groesse der Anzeigeflaeche geaendert hat.
	 * @param size Die neue Groesse der Anzeigeflaeche.
	 */
	public void setSize(double size);

	/**
	 * Diese Methode gibt einen JavaFx Node wieder, welcher an der Stelle der Anzeigeflaeche angezeigt werden soll.
	 */
	public Node getNode();

	/**
	 * Diese Methode wird aufgerufen, wenn sich die generierten Klimadaten veraendert haben und 
	 * die angezeigte Karte neu berechnet werden soll.
	 * @param height Die Hoehenwerte [Pixel][Monat]
	 * @param rainfall Die Niederschlagswerte [Pixel][Monat]
	 * @param temperature Die Temperaturwerte [Pixel][Monat]
	 */
	public void setClimate(double[][] height, double[][] rainfall, double[][] temperature);
	
	/**
	 * Diese Methode wird aufgerufen, wenn sich der angezeigte Monat geaendert hat und die Karte neu berechnet werden soll.
	 */
	public void setMonth(int month);
}
