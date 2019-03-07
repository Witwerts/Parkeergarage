package Parkeersimulator.View;

import javax.swing.JPanel;

import Parkeersimulator.Logic.MainModel;
import Parkeersimulator.Logic.MenuModel;

public class PageView extends JPanel {
	private MainModel mainModel;
	private MenuModel menuModel;

	public PageView(MainModel mainModel, MenuModel menuModel) {
		this.mainModel = mainModel;
		this.menuModel = menuModel;
	}
}
