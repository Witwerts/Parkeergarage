package Parkeersimulator.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Parkeersimulator.Car;
import Parkeersimulator.Location;
import Parkeersimulator.Logic.*;

public class MapView extends View {
	private MainModel mainModel;
	private MapModel mapModel;
	
	private JLabel timeLabel;
	
    private Dimension mapSize;
	
	public MapView(MainModel mainModel, MapModel mapModel) {
		super(mainModel);
		
		this.mainModel = mainModel;
		this.mapModel = mapModel;
		this.mapSize = getMapSize();
		
		this.timeLabel = new JLabel(this.mainModel.getTime());
		
		timeLabel.setLocation(new Point(this.getWidth() / 2, 50));
		
		this.add(timeLabel);
	}

    /**
     * Overriden. The car park view component needs to be redisplayed. Copy the
     * internal image to screen.
     */
    @Override public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for(int floor = 0; floor < mainModel.getNumberOfFloors(); floor++) {
		    for(int row = 0; row < mainModel.getNumberOfRows(); row++) {
		        for(int place = 0; place < mainModel.getNumberOfPlaces(); place++) {
		            Location location = new Location(floor, row, place);
		            Car car = mainModel.getCarAt(location);
		            
		            Color color = car == null ? Color.white : car.getType().getColor();
		            this.drawPlace(g, location, color);
		        }
		    }
		}
    }
    
    public void updateView() {
    	if(this.timeLabel != null)
    		this.timeLabel.setText(this.mainModel.getTime());
    	
		repaint();
    }
    
    public void drawPlace(Graphics graphics, Location location, Color color) {
    	Point tPos = getTilePos(location);
    	
		graphics.setColor(color);
		graphics.fillRect(tPos.x, tPos.y, mapModel.tWidth, mapModel.tHeight);
	}
    
    public Point getTilePos(Location loc) {
		return new Point(
			((this.getSize().width - this.mapSize.width) / 2) + 
			(loc.getFloor() * (getFloorSize().width + mapModel.fMargin))
			+ (loc.getRow() * (mapModel.tWidth + mapModel.bWidth))
			+ ((loc.getRow() / 2) * mapModel.rMargin),
			((this.getSize().height - this.mapSize.height) / 2) + 
			loc.getPlace() * (mapModel.tHeight + mapModel.bWidth)
		);
	}
    
    public Dimension getMapSize() {
    	return new Dimension(
    		(this.getFloorSize().width * (mainModel.getNumberOfFloors() - 1))
    		+ (mapModel.fMargin * (mainModel.getNumberOfFloors() - 1))
    		+ this.getFloorSize().width,
    		this.getFloorSize().height
    	);
    }
    
    public Dimension getFloorSize() {
    	return new Dimension(
    		(mapModel.tWidth * mainModel.getNumberOfRows())
    		+ (((int)(mainModel.getNumberOfRows() / 2)) * mapModel.bWidth)
    		+ (((int)(mainModel.getNumberOfRows() % 2)) * mapModel.bWidth)
    		+ (((int)(mainModel.getNumberOfRows() / 2)) * mapModel.rMargin),
    		(mapModel.tHeight * mainModel.getNumberOfPlaces())
    		+ (mapModel.bWidth * (mainModel.getNumberOfPlaces()+1))
    	);
    }
}
