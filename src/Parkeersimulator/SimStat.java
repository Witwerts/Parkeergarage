package Parkeersimulator;

public class SimStat {
	private String id;
	private String name;
	private Object value;
	
	private boolean hasDecimal;
	
	public SimStat(String id, String name, Object value) {
		this.id = id;
		this.name = name;
		this.value = value;
		
		this.hasDecimal = value instanceof Double;
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
	
	public void addValue(int value) {
		int val = (Integer)this.value + value;
		
		this.value = val;
	}
	
	public void addValue(double value) {
		Double val = (double)this.value + value;
		
		this.value = val;
	}
	
	public String val() {
		return this.hasDecimal() ? String.format("%.2f", (double)this.value) : String.valueOf(this.value);
	}
	
	public boolean hasDecimal() {
		return this.hasDecimal;
	}
}
