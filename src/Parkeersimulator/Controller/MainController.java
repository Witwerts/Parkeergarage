package Parkeersimulator.Controller;

import javax.swing.JPanel;

import Parkeersimulator.Logic.MainModel;

public class MainController extends JPanel {
	private MainModel model;
	
	public MainController(MainModel model) {
		this.model = model;
	}
}