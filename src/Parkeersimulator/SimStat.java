package Parkeersimulator;

public class SimStat {
	private String id;
	private String name;
	private Object value;
	
	public SimStat(String id, String name, Object value) {
		this.setId(id);
		this.setName(name);
		this.setValue(value);
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
		Double val = (Double)this.value + value;
		
		this.value = val;
	}
}
