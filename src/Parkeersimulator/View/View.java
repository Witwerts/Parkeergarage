package Parkeersimulator.View;

import javax.swing.JPanel;

import Parkeersimulator.Logic.*;

public abstract class View extends JPanel {
	private static final long serialVersionUID = -2767764579227738552L;
	private MainModel model;

	public View(MainModel model) {
		this.model=model;
		model.addView(this);
	}
	
	public MainModel getModel() {
		return model;
	}
	
	public void updateView() {
		repaint();
	}
	
	public void tick() {
		
	}
}
