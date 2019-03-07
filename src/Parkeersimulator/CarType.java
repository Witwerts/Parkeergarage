package Parkeersimulator;

import java.awt.Color;
import java.util.HashMap;

public enum CarType {
	PARKINGPASS(1, Color.BLUE, false, 0, 3),
	RESERVATION(2, Color.GREEN, false, 0, -1),
	AD_HOC(3, Color.RED, false, 3, -1);
	
	private int id;
    private Color color;
    private boolean hasToPay;
    
    private int minRow;
    private int maxRow;
    
	private static HashMap<Integer, Color> types;

    private CarType(int id, Color color, boolean hasToPay, int minRow, int maxRow){
        this.id = id;
        this.color = color;
        this.hasToPay = hasToPay;
        
        this.minRow = minRow;
        this.maxRow = maxRow;
    }
    
    public int getId() {
    	return this.id;
    }
    
    public Color getColor() {
    	return this.color;
    }
    
    public boolean hasToPay() {
    	return this.hasToPay;
    }
    
    public int getMinRow() {
    	return this.minRow;
    }
    
    public int getMaxRow() {
    	return this.maxRow;
    }
}