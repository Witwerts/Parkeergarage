package Parkeersimulator.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Parkeersimulator.Logic.*;

public class MenuView extends JPanel {
	private MainModel mainModel;
	private MenuModel menuModel;
	
	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw menu
		
		g.setColor(menuModel.bgColor);
		g.fillRect(menuModel.sideWidth + menuModel.borderWidth, 0, this.getWidth(), this.getHeight());
		
		//draw border (left)
		g.setColor(menuModel.borderColor);
		g.fillRect(menuModel.sideWidth, 0, menuModel.borderWidth, this.getHeight());
		
		//draw sidebar button
		g.setColor(menuModel.borderColor);
		g.fillRect(0, menuModel.sideHeight + menuModel.topHeight, menuModel.sideWidth + menuModel.borderWidth, menuModel.sideHeight + (menuModel.borderWidth*2));
		
		g.setColor(menuModel.bgColor);
		g.fillRect(menuModel.borderWidth, menuModel.borderWidth + menuModel.sideHeight + menuModel.topHeight, menuModel.sideWidth, menuModel.sideHeight);
		
		//draw menu top / buttons
		
		g.setColor(new Color(0,0,0,0));
		g.fillRect(menuModel.sideWidth + menuModel.borderWidth, 0, this.getWidth() - menuModel.borderWidth, menuModel.topHeight);
	}
	
	public MenuView(MainModel mainModel, MenuModel menuModel) {
		this.mainModel = mainModel;
		this.menuModel = menuModel;
	}
}

