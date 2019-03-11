package Parkeersimulator;

public class SimSetting {
	private String id;
	private String name;
	private Object value;
	
	private Object minValue;
	private Object maxValue;
	private int steps;
	
	private boolean customizeable;
	
	public SimSetting(String id, String name, Object defValue, Object minValue, Object maxValue, int steps, boolean edit) {
		this.id = id;
		this.name = name;
		this.value = defValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.steps = steps;
		this.setCustomizeable(edit);
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
	
	public int getVal() {
		return this.hasDecimal() ? (int)((double)this.value * 100) : (int)this.value; 
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getMinValue() {
		return minValue;
	}
	
	public int getMin() {
		return this.hasDecimal() ? (int)((double)this.minValue * 100) : (int)this.minValue; 
	}

	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}

	public Object getMaxValue() {
		return maxValue;
	}
	
	public int getMax() {
		return this.hasDecimal() ? (int)((double)this.maxValue * 100) : (int)this.maxValue; 
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	public int getSteps() {
		return steps;
	}
	
	public int getMinorSpacing() {
		return Math.max(1, (this.getMax() - this.getMin()) / this.steps);
	}
	
	public int getMajorSpacing() {
		int min = this.getMin();
		int max = this.getMax();
		
		int length = String.valueOf(max - min).length();
		int steps = length > 1 ? (min > 0 && max % min == 0 ? (int)Math.min(10, max / min) : (max - min) / (int)(Math.pow(10, length - 2))) : max - min + 1;
		
		int step = min > 0 ? Math.round(max / steps) : Math.round((max - min) / steps);
		
		return step;
	}
	
	public boolean hasDecimal() {
		return this.value instanceof Double;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public boolean isCustomizeable() {
		return customizeable;
	}

	public void setCustomizeable(boolean customizeable) {
		this.customizeable = customizeable;
	}
}