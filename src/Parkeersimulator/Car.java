package Parkeersimulator;

import java.awt.*;
import java.util.Random;

public class Car {
    private Location location;
    private int minutesLeft;
    private int spentMinutes;
    private boolean isPaying;
    private CarType type;
    
    /**
     * Constructor for objects of class Car
     */
    public Car(CarType type) {
    	this.setType(type);
    	Random random = new Random();
    	
    	this.minutesLeft = (int) (15 + random.nextFloat() * 3 * 60);
    }
    
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }
    
    public int getSpentMinutes() {
    	return spentMinutes;
    }

    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }
    
    public boolean getIsPaying() {
        return isPaying;
    }

    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    public boolean getHasToPay() {
    	if(this.type != null)
    		return this.type.hasToPay();
    	
        return true;
    }

    public void tick() {
        minutesLeft--;
        spentMinutes++;
    }

	public CarType getType() {
		return type;
	}

	public void setType(CarType type) {
		this.type = type;
	}
}