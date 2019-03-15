package Parkeersimulator;

public class SimSetting implements Cloneable {
	private String id;
	private String name;
	private int value;
	
	private int minValue;
	private int maxValue;
	
	private int defValue;
	
	private int steps;
	
	private boolean customizeable;
	private boolean hasDecimal;
	
	public SimSetting(String id, String name, Object defValue, Object minValue, Object maxValue, int steps, boolean edit) {
		this.id = id;
		this.name = name;
		
		this.hasDecimal = defValue instanceof Double;
		this.value = this.hasDecimal() ? (int)((double)defValue*100) : (int)defValue;
		this.defValue = this.value;
		
		this.minValue = this.hasDecimal() ? (int)((double)minValue*100) : (int)minValue;
		this.maxValue = this.hasDecimal() ? (int)((double)maxValue*100) : (int)maxValue;
		
		this.steps = steps;
		this.setCustomizeable(edit);

		this.reset();
	}
	
	public SimSetting clone() {
		Object setting = null;
		
		try {
			setting = super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (SimSetting)setting;
	}
	
	public void reset() {
		this.value = this.defValue;
	}
	
	public void save() {
		this.defValue = this.value;
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
		return this.hasDecimal() ? (double)(this.value / 100) : (int)this.value; 
	}
	
	public String val() {
		return this.hasDecimal() ? String.format("%.2f", (double)(this.value / 100)) : String.valueOf(this.value);
	}
	
	public int getVal() {
		return this.value; 
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Object getMinValue() {
		return minValue;
	}
	
	public int getMin() {
		return minValue; 
	}

	public Object getMaxValue() {
		return maxValue;
	}
	
	public int getMax() {
		return this.maxValue; 
	}

	public int getSteps() {
		return this.steps;
	}
	
	public int getMinorSpacing() {
		return Math.max(1, (this.getMax() - this.getMin()) / this.getSteps());
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
		return this.hasDecimal;
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