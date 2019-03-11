package Parkeersimulator;

import javax.swing.*;

import Parkeersimulator.Controller.*;
import Parkeersimulator.Logic.*;
import Parkeersimulator.View.*;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;

public class Simulator {
	
	private static JFrame mainScreen;
	
	private static MainModel mainModel;
	private static MenuModel menuModel;
	private static MapModel mapModel;
	
	private static MenuView menuView;
	private static MapView mapView;
	
	private MainController mainController;
	private MapController mapController;
	private MenuController menuController;
	
	private static int mainWidth = 1100;
	private static int mainHeight = 600;
	
	private static Color bgColor = new Color(220,220,220);
	private static Color borderColor = new Color(44,50,143);
	private static int borderWidth = 1;
	
	public Simulator() {
		mainModel = new MainModel();
		menuModel = new MenuModel();
		mapModel = new MapModel();
		
		mainController = new MainController(mainModel);
		mapController = new MapController(mainModel, mapModel);
		menuController = new MenuController(mainModel, menuModel);
		
		menuView = new MenuView(mainModel, menuModel);
		mapView = new MapView(mainModel, mapModel);
		
		mainScreen = new JFrame("Simulator Parkeergarage");
		
		mainScreen.setSize(mainWidth, mainHeight);
		mainScreen.setLayout(null);
		mainScreen.setLocationRelativeTo(null);
		mainScreen.setResizable(false);
		mainScreen.setUndecorated(true);
		
		mainScreen.getRootPane().setBorder(
		        BorderFactory.createMatteBorder(borderWidth, borderWidth, borderWidth, borderWidth, borderColor));

		mainScreen.getContentPane().add(menuModel);
		mainScreen.getContentPane().add(mainController);
		
		mainScreen.getContentPane().add(menuView);
		mainScreen.getContentPane().add(menuController);
		
		mainScreen.getContentPane().add(mapView);
		mainScreen.getContentPane().add(mapController);
		
		menuController.setBounds(mainWidth - menuModel.mainWidth, 0, menuModel.mainWidth, menuModel.mainHeight);
		menuView.setBounds(mainWidth - menuModel.mainWidth, 0, menuModel.mainWidth, menuModel.mainHeight);
		mapController.setBounds(0,0, mainWidth - menuModel.mainWidth, mainHeight);
		mapView.setBounds(0,0, mainWidth - menuModel.mainWidth, mainHeight);
		
		menuModel.setBounds(mainWidth - menuModel.mainWidth, menuModel.topHeight, mainWidth, mainHeight - menuModel.topHeight);
		
		mainScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainScreen.setVisible(true);
		
		//start simulator
        mainModel.run();
	}
}
