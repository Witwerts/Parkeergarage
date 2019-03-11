package Parkeersimulator.Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.*;

import Parkeersimulator.*;
import Parkeersimulator.Logic.*;
import Parkeersimulator.View.*;
import Parkeersimulator.Controller.*;

public class MenuController extends JPanel implements MouseListener {
	private MainModel mainModel;
	private MenuModel menuModel;
	
	//array to save the menu buttons (shapes)
	private static HashMap<String, JButton> mButtons = new HashMap<String, JButton>();
	
	//array to get the drawn pages
	private static HashMap<String, JPanel> mPages = new HashMap<String, JPanel>();
	
	private HashMap<String, SimStat> stats;
	private HashMap<String, SimSetting> settings;
	
	//name of the current page that is loaded
	private static JPanel currPage;
	
	public MenuController(MainModel mainModel, MenuModel menuModel) {
		this.mainModel = mainModel;
		this.menuModel = menuModel;
		
		this.setLayout(null);
		int mIndex = 0;
		
		for(menuOption mButton : menuOption.values()) {
			//create button and set the settings
			JButton btn = new JButton();
			
			btn.setName(mButton.toString());
			btn.setText(mButton.toString());
			btn.setBorderPainted(false);
			btn.setFocusPainted(false);
			btn.setBounds(menuModel.sideWidth + menuModel.borderWidth + (menuModel.btnMargin*(mIndex+1)) + (menuModel.btnWidth*mIndex) - mIndex, menuModel.topHeight - menuModel.btnHeight, menuModel.btnWidth, menuModel.btnHeight);
			btn.setBackground(menuModel.btnBgColor);
			btn.setVisible(true);
			
			//add button to the array
			this.mButtons.put(mButton.toString(), btn);
			
			//add the button to the panel
			add(btn);
			
			//add mouse events to the button
			btn.addMouseListener(this);
			
			mIndex++;
		}
		
		setVisible(true);
		
		if(menuOption.values().length > 0) {
			this.openPage(menuOption.values()[0].toString());
			
			this.resetButtons();
		}
	}
	
	//on hover events
	public void mouseEntered(MouseEvent e) {
		this.resetButtons();
		
		for(JButton btn : this.mButtons.values()) {
			if((this.currPage == null || (this.currPage != null && this.currPage.toString() != btn.getName())) && e.getSource() == btn)
				btn.setBackground(menuModel.btnHoverColor);
		}
    }

	//on mouse leave events
	@Override
	public void mouseExited(MouseEvent e) {
		this.resetButtons();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		this.resetButtons();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.resetButtons();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.resetButtons();
		
		//check if you clicked on menu button
		for(JButton btn : this.mButtons.values()) {
			//System.out.println(btn.getName());
			
			if(e.getSource() == btn) {
				btn.setBackground(menuModel.btnActiveColor);
				this.openPage(btn.getName());
			}
			else
				btn.setBackground(menuModel.btnBgColor);
		}
	}
	
	public void openPage(String pageName)
	{
		//check if this page is already open
		if(this.currPage != null && this.currPage.toString() == pageName)
			return;
		
		//check if page name exists
		if(!pageExists(pageName))
			return;
		
		//hide previous page
		if(this.currPage != null) {
			this.currPage.setVisible(false);
		}
		
		menuOption mOption = menuOption.valueOf(pageName);
		JPanel page;
		
		if(mPages.containsKey(pageName))
			page = mPages.get(pageName);
		else {
			page = CreatePage(mOption);
			menuModel.add(page, BorderLayout.NORTH);
			this.mPages.put(pageName, page);
		}
		
		this.currPage = page;
		page.setVisible(true);
	}
	
	public boolean pageExists(String pageName)
	{
		for(menuOption option : menuOption.values())
		{
			if(option.toString() == pageName)
				return true;
		}
		
		return false;
	}
	
	public void resetButtons() {
		for(JButton btn : this.mButtons.values()) {
			if(this.currPage != null && this.currPage.toString() == btn.getName()) {
				btn.setBackground(menuModel.btnActiveColor);
			}
			else {
				btn.setBackground(menuModel.btnBgColor);
			}
		}
	}
	
	public JPanel CreatePage(menuOption type) {
		JPanel page = new JPanel();
		
		page.setLayout(null);
		page.setBackground(null);
		page.setBounds(0, 0, menuModel.mainWidth, menuModel.mainHeight - menuModel.topHeight);
		
		switch(type) {
			case Instellingen:
				this.settings = mainModel.GetSettings();
				
				int sIndex = 0;
				
				int mTop = 5;
				int mLeft = 15;
				
				//slider height
				int sHeight = 20;
				
				int height = 40;
				
				for(SimSetting setting : this.settings.values()) {
					if(!setting.isCustomizeable())
						continue;
					
					//text
					JLabel text = new JLabel(setting.getName());
					text.setBounds(mLeft, mTop + sIndex*height, menuModel.mainWidth - mLeft*2, height - sHeight);
					
					//slider
					JSlider slider = new JSlider(JSlider.HORIZONTAL, setting.getMin(), setting.getMax(), setting.getVal());
					
					slider.setMajorTickSpacing(setting.getMajorSpacing());
					slider.setMinorTickSpacing(setting.getMinorSpacing());
					slider.setPaintTicks(false);
					slider.setPaintLabels(false);
					slider.setName(setting.getName());
					
					slider.setBounds(mLeft, mTop + sIndex*height + height - sHeight, menuModel.mainWidth - mLeft*2, mTop + sHeight);
					slider.setBackground(null);
					
					page.add(slider);
					page.add(text);
					
					sIndex++;
				}
				
				break;
				
			case Statistieken:
				this.stats = mainModel.GetStats();
				
				/*for(int i = 0; i < this.stats.size(); i++) {
					SimStat stat = this.stats.get(i);
				}*/
				
				break;
		}
		
		return page;
	}
}

enum menuOption { 
	Instellingen,
	Statistieken
};
