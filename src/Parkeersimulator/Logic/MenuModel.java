package Parkeersimulator.Logic;

import java.awt.Color;
import java.awt.Graphics;
import Parkeersimulator.*;

import javax.swing.JPanel;

public class MenuModel extends JPanel {
	//color of the menu itself
	public Color bgColor = new Color(63,72,204);
	public Color contentColor = new Color(96,104,213);
	public Color borderColor = Color.BLACK;
	
	//colors of the menu button
	public Color btnBgColor = new Color(bgColor.getRed() + 10, bgColor.getGreen() + 10, bgColor.getBlue() + 10);
	public Color btnHoverColor = new Color(bgColor.getRed() + 20, bgColor.getGreen() + 20, bgColor.getBlue() + 20);
	public Color btnActiveColor = contentColor;
	
	//size of the sidebar/button
	//public int sideWidth = 30;
	//public int sideHeight = 70;
	public int sideWidth = 0;
	public int sideHeight = 0;
	
	//height of the top bar
	public int topHeight = 42;
	
	//menu button size
	public int btnHeight = 34;
	public int btnWidth = 120;
	public int btnMargin = 12;
	
	//menu border width
	public int borderWidth = 2;
	
	public int textWidth = 200;
	public int optionHeight = 100;
	
	//menu size
	public int mainHeight = 600;
	public int mainWidth = 400;
	
	//editor size
	public int editorHeight = 50;
	public int editorWidth = mainWidth;
	
	public MenuModel() {
		this.setLayout(null);
		this.setBackground(this.bgColor);
		//this.repaint();
	}
	
	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
