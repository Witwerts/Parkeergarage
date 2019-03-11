package Parkeersimulator.Logic;

import java.awt.Color;
import java.awt.Graphics;
import Parkeersimulator.*;

import javax.swing.JPanel;

public class MenuModel extends JPanel {
	//color of the menu itself
	public static Color bgColor = new Color(63,72,204);
	public static Color contentColor = new Color(96,104,213);
	public static Color borderColor = Color.BLACK;
	
	//colors of the menu button
	public static Color btnBgColor = new Color(bgColor.getRed() + 10, bgColor.getGreen() + 10, bgColor.getBlue() + 10);
	public static Color btnHoverColor = new Color(bgColor.getRed() + 20, bgColor.getGreen() + 20, bgColor.getBlue() + 20);
	public static Color btnActiveColor = contentColor;
	
	//size of the sidebar/button
	//public static int sideWidth = 30;
	//public static int sideHeight = 70;
	public static int sideWidth = 0;
	public static int sideHeight = 0;
	
	//height of the top bar
	public static int topHeight = 42;
	
	//menu button size
	public static int btnHeight = 34;
	public static int btnWidth = 120;
	public static int btnMargin = 12;
	
	//menu border width
	public static int borderWidth = 2;
	
	public static int textWidth = 200;
	public static int optionHeight = 100;
	
	//menu size
	public static int mainHeight = 600;
	public static int mainWidth = 400;
	
	public MenuModel() {
		this.setLayout(null);
		this.setBackground(this.contentColor);
		//this.repaint();
	}
	
	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
