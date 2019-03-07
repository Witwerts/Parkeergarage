package Parkeersimulator.Logic;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.*;

import Parkeersimulator.*;
import Parkeersimulator.View.*;

public class MainModel implements Runnable {
	private CarQueue paymentCarQueue;
	private CarQueue exitCarQueue;
	
	private int numberOfFloors;
	private int numberOfRows;
	private int numberOfPlaces;
	
	private Car[][][] cars;
	private HashMap<CarType, CarQueue[]> queues;

	// settings
	boolean run;
	int currTime;

	ArrayList<View> views;

	String[] days = new String[] { "maandag", "dinsdag", "woensdag", "donderdag", "vrijdag", "zaterdag", "zondag" };
	private HashMap<String, SimStat> stats;
	private static HashMap<String, SimSetting> settings;

	public MainModel() {
		this.paymentCarQueue = new CarQueue();
		this.exitCarQueue = new CarQueue();

		this.run = false;

		this.views = new ArrayList<View>();
		this.stats = new HashMap<String, SimStat>();
		this.settings = new HashMap<String, SimSetting>();
		
		this.numberOfFloors = 3;
		this.numberOfRows = 6;
		this.numberOfPlaces = 30;
		
		//add settings
		updateSetting(new SimSetting("ResQueues", "Aantal wachtrijen (gereserveerden)", 1, 1, 10, 10));
		updateSetting(new SimSetting("PassQueues", "Aantal wachtrijen (abonnees)", 1, 1, 10, 10));
		updateSetting(new SimSetting("TotalQueues", "Aantal wachtrijen (normaal)", 1, 1, 10, 10));
		
		updateSetting(new SimSetting("TotalPassHolders", "Aantal abonnementen", 100, 100, 1000, 100));
		updateSetting(new SimSetting("Speed", "Snelheid simulatie", 100, 100, 1000, 10));
		updateSetting(new SimSetting("TotalDays", "Aantal dagen laten runnen", 1, 1, this.days.length, 1));
		
		updateSetting(new SimSetting("CostAdhoc", "Prijs per uur (normaal)", 3.25, 0.00, 1000.00, 1000));
		updateSetting(new SimSetting("CostPass", "Kosten abonnement", 40.00, 0.00, 1000.00, 1000));
		updateSetting(new SimSetting("CostRes", "Kosten reservatie", 5.00, 0.00, 1000.00, 1000));
		
		updateSetting(new SimSetting("WeekDayArrivals", "Aantal auto's per uur (doordeweeks)", 100, 100, 1000, 10));
		updateSetting(new SimSetting("WeekendArrivals", "Aantal auto's per uur (weekend)", 200, 100, 1000, 10));
		updateSetting(new SimSetting("WeekPassArrivals", "Percentage abonnees (doordeweeks)", 10.00, 1.00, 100.00, 100));
		updateSetting(new SimSetting("WeekendPassArrivals", "Percentage abonnees (weekend)", 15.00, 1.00, 100.00, 100));
		updateSetting(new SimSetting("WeekDayReservations", "Aantal reservingen (doordeweeks)", 75, 0, 1000, 100));
		updateSetting(new SimSetting("WeekendReserverations", "Aantal reserveringen (weekend)", 150, 0, 1000, 100));
		
		updateSetting(new SimSetting("EnterSpeed", "Aantal auto's (per minuut)", 3, 1, 10, 10));
		updateSetting(new SimSetting("PaymentSpeed", "Aantal betaalde klanten (per minuut)", 7, 1, 10, 10));
		updateSetting(new SimSetting("ExitSpeed", "Aantal verlatende klanten (per minuut)", 5, 1, 10, 10));

		this.reset();
	}

	public void reset() {
		this.run = true;
		this.currTime = 0;

		this.cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];

		this.queues = new HashMap<CarType, CarQueue[]>();

		for (CarType type : CarType.values()) {
			int total = this.getTotalQueues(type);
			CarQueue[] queue = new CarQueue[total];
			
			for(int i = 0; i < total; i++)
				queue[i] = new CarQueue();
			
			this.queues.put(type, queue);
		}
		
		//(re)set basic statistics
		addStat(new SimStat("Income", "Inkomsten", 0));
		addStat(new SimStat("PassIncome", "Inkomsten abonnementen", (int)getSetting("TotalPassHolders").getValue() * (double)getSetting("CostPass").getValue()));
		addStat(new SimStat("TotalLoss", "Opgelopen verlies", 0));
	}

	public void updateSetting(SimSetting setting) {
		this.settings.put(setting.getId(), setting);
	}

	public int getTotalQueues(CarType type) {
		int total = 1;

		switch (type) {
			case AD_HOC:
				total = (int) this.getSetting("TotalQueues").getValue();
	
				break;
	
			case RESERVATION:
				total = (int) this.getSetting("ResQueues").getValue();
	
				break;
	
			case PARKINGPASS:
				total = (int) this.getSetting("PassQueues").getValue();
	
				break;
		}

		return total;
	}

	public SimSetting getSetting(String Id) {
		return this.settings.get(Id);
	}

	public boolean updateStat(String id, Object add) {
		if (this.stats.containsKey(id)) {
			if (add instanceof Integer)
				this.stats.get(id).addValue((int) add);
			else if (add instanceof Double)
				this.stats.get(id).addValue((double) add);
			else
				this.stats.get(id).setValue(add);

			return true;
		}

		return false;
	}

	public void addStat(SimStat stat) {
		this.stats.put(stat.getName(), stat);
	}

	@Override
	public void run() {
		while ((this.currTime / 24 / 60) < (int)getSetting("TotalDays").getValue() && this.run)
			tick();
	}

	public void stop() {
		this.run = false;
	}

	public void addView(View view) {
		this.views.add(view);
	}

	public String getTime() {
		int iDay = (int) Math.floor(this.currTime / 24 / 60);
		int iHour = (int) Math.floor(this.currTime / 60) % 24;
		int iMin = (int) Math.floor(this.currTime % 60);

		return this.days[iDay % this.days.length] + " " + String.format("%02d", iHour) + ":"
				+ String.format("%02d", iMin);
	}

	public void tick() {
		this.updateViews(null);
		this.handleEntrance();
		
		SimSetting sSpeed = getSetting("Speed");

		try {
			Thread.sleep((int)Math.max((int)sSpeed.getMinValue(), Math.min((int)sSpeed.getValue(), (int)sSpeed.getMaxValue())));
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.updateCars();

		this.currTime++;
		this.handleExit();
	}

	private void updateViews(Object type) {
		for (View view : views) {
			if (type == null || (type != null && view.getClass().isInstance(type)))
				view.updateView();
		}
	}

	private void handleEntrance() {
		carsArriving();
		
		carsEntering(CarType.AD_HOC);
		carsEntering(CarType.PARKINGPASS);
	}

	private void handleExit() {
		carsReadyToLeave();
		carsPaying();
		carsLeaving();
	}

	private void carsArriving() {
		int numberOfCars;
		
		numberOfCars = getNumberOfCars((int)getSetting("WeekDayArrivals").getValue(), (int)getSetting("WeekendArrivals").getValue());
		addArrivingCars(numberOfCars, CarType.AD_HOC);
		numberOfCars = getNumberOfCars((int)((int)getSetting("WeekDayArrivals").getValue() * (double)getSetting("WeekPassArrivals").getValue()), (int)((int)getSetting("WeekendArrivals").getValue() * (double)getSetting("WeekendPassArrivals").getValue()));
		addArrivingCars(numberOfCars, CarType.PARKINGPASS);
		numberOfCars = getNumberOfCars((int)getSetting("WeekDayReservations").getValue(), (int)getSetting("WeekendReserverations").getValue());
		addArrivingCars(numberOfCars, CarType.RESERVATION);
	}

	private void carsEntering(CarType type) {
		if (!this.queues.containsKey(type))
			return;

		CarQueue[] queues = this.queues.get(type);
		
		for (CarQueue queue : queues) {
			if(queue == null)
				continue;
			
			int i = 0;
			
			while (queue.carsInQueue() > 0 && i < (int)getSetting("EnterSpeed").getValue()) {
				Location freeLocation = this.getFirstFreeLocation(type);
				Car car = queue.removeCar();
				
				if(freeLocation != null)
					this.setCarAt(freeLocation, car);
				else
					updateStat("totalLoss", getCost(car, 0));
				
				i++;
			}

		}
	}
	
	private Double getCost(Car car, int time) {
		Double cost = 0.00;
		int hours = (int)Math.ceil((double)(time/60));
		
		switch(car.getType()) {
			case AD_HOC:
				cost = (hours * (double)getSetting("CostAdhoc").getValue());
				
				break;
			case RESERVATION:
				cost = ((double)getSetting("CostRes").getValue() + (hours * (double)getSetting("CostAdhoc").getValue()));
				
				break;
			
			default:
				break;
		}
		
		return cost;
	}

	public Location getFirstFreeLocation(CarType type) {
		int start = type.getMinRow();
		int end = type.getMaxRow() > -1 && type.getMaxRow() < getNumberOfFloors() * getNumberOfRows() ? type.getMaxRow() : getNumberOfFloors() * getNumberOfRows();
		
		int sFloor = start / getNumberOfRows();
		int sRow = start % getNumberOfRows();
		
		int eFloor = end / getNumberOfRows();
		int eRow = end % getNumberOfRows();
		
		for (int floor = sFloor; floor <= eFloor; floor++) {
			for (int row = floor == sFloor ? sRow : 0; row < (floor >= eFloor ? eRow : getNumberOfRows()); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					if (getCarAt(location) == null) {
						return location;
					}
				}
			}
		}

		return null;
	}

	private void carsReadyToLeave() {
		// Add leaving cars to the payment queue.
		Car car = this.getFirstLeavingCar();
		while (car != null) {
			if (car.getHasToPay()) {
				car.setIsPaying(true);
				paymentCarQueue.addCar(car);
			} else {
				carLeavesSpot(car);
			}
			
			car = this.getFirstLeavingCar();
		}
	}

	private void carsPaying() {
		// Let cars pay.
		int i = 0;
		while (paymentCarQueue.carsInQueue() > 0 && i < (int)getSetting("PaymentSpeed").getValue()) {
			Car car = paymentCarQueue.removeCar();
			// TODO Handle payment.
			carLeavesSpot(car);
			i++;
		}
	}

	private void carsLeaving() {
		// Let cars leave.
		int i = 0;
		while (exitCarQueue.carsInQueue() > 0 && i < (int)getSetting("ExitSpeed").getValue()) {
			exitCarQueue.removeCar();
			i++;
		}
	}

	private int getNumberOfCars(int weekDay, int weekend) {
		Random random = new Random();

		// Get the average number of cars that arrive per hour.
		int averageNumberOfCarsPerHour = (this.currTime / 24 / 60) < 5 ? weekDay : weekend;

		// Calculate the number of cars that arrive this minute.
		double standardDeviation = averageNumberOfCarsPerHour * 0.3;
		double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
		return (int) Math.round(numberOfCarsPerHour / 60);
	}

	private void addArrivingCars(int numberOfCars, CarType type) {
		for (int i = 0; i < numberOfCars; i++) {
			Car car = new Car(type);
			this.addCarToQueue(car, type);
		}
	}

	private void addCarToQueue(Car car, CarType type) {
		if (this.queues.isEmpty() || !this.queues.containsKey(type))
			return;
		
		int total = this.getTotalQueues(type);
		int num = new Random().nextInt(total);
		
		this.queues.get(type)[num].addCar(car);
	}

	private void carLeavesSpot(Car car) {
		this.removeCarAt(car.getLocation());
		exitCarQueue.addCar(car);
	}

	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public int getNumberOfPlaces() {
		return numberOfPlaces;
	}

	public Car getCarAt(Location location) {
		if (!locationIsValid(location)) {
			return null;
		}
		return cars[location.getFloor()][location.getRow()][location.getPlace()];
	}

	public boolean setCarAt(Location location, Car car) {
		if (!locationIsValid(location)) {
			return false;
		}

		Car oldCar = getCarAt(location);
		if (oldCar == null) {
			cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
			car.setLocation(location);
			return true;
		}
		return false;
	}

	public Car removeCarAt(Location location) {
		if (!locationIsValid(location)) {
			return null;
		}
		Car car = getCarAt(location);
		if (car == null) {
			return null;
		}
		cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
		car.setLocation(null);
		return car;
	}

	public Car getFirstLeavingCar() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location);
					if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
						return car;
					}
				}
			}
		}
		return null;
	}

	public void updateCars() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location);
					if (car != null) {
						car.tick();
					}
				}
			}
		}
	}

	private boolean locationIsValid(Location location) {
		int floor = location.getFloor();
		int row = location.getRow();
		int place = location.getPlace();
		
		if (floor < 0 || floor >= this.numberOfFloors || row < 0 || row > numberOfRows || place < 0
				|| place > numberOfPlaces) {
			return false;
		}
		return true;
	}
	
	public HashMap<String, SimSetting> GetSettings() {
		return this.settings;
	}
	
	public HashMap<String, SimStat> GetStats(){
		return this.stats;
	}
}
