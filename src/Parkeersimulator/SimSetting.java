package Parkeersimulator;

public class SimSetting {
	private String id;
	private String name;
	private Object value;
	
	private Object minValue;
	private Object maxValue;
	private int steps;
	
	public SimSetting(String id, String name, Object defValue, Object minValue, Object maxValue, int steps) {
		this.id = id;
		this.name = name;
		this.value = defValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.steps = steps;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getMinValue() {
		return minValue;
	}

	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}

	public Object getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}
}
