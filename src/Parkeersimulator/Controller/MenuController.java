package Parkeersimulator.Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Parkeersimulator.*;
import Parkeersimulator.Logic.*;
import Parkeersimulator.View.*;
import javafx.scene.control.Slider;
import Parkeersimulator.Controller.*;

public class MenuController extends JPanel implements MouseListener, ChangeListener, ActionListener {
	private MainModel mainModel;
	private MenuModel menuModel;
	
	//array to save the menu buttons (shapes)
	private HashMap<String, JButton> mButtons = new HashMap<String, JButton>();
	
	//array to get the drawn pages
	private HashMap<String, JPanel> mPages = new HashMap<String, JPanel>();
	
	private HashMap<String, SimStat> stats;
	private HashMap<String, SimSetting> settings = new HashMap<String, SimSetting>();
	
	//name of the current page that is loaded
	private JPanel currPage;
	
	//sliders
	private HashMap<String, JSlider> mSliders = new HashMap<String, JSlider>();
	
	//labels
	private HashMap<String, JLabel> mLabels = new HashMap<String, JLabel>();
	
	//editor
	private boolean editorEnabled;
	private JPanel editor;
	private HashMap<String, JButton> eButtons = new HashMap<String, JButton>();
	
	//update timer
	private Timer uTimer;
	
	public MenuController(MainModel mainModel, MenuModel menuModel) {
		this.mainModel = mainModel;
		this.menuModel = menuModel;
		
		if(this.mainModel != null)
			this.settings = new HashMap<>(this.mainModel.GetSettings());
		
		editorEnabled = false;
		
		this.setLayout(null);
		this.setBackground(this.menuModel.bgColor);
		
		int mIndex = 0;
		
		for(MenuOption mButton : MenuOption.values()) {
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
		
		if(MenuOption.values().length > 0) {
			this.openPage(MenuOption.values()[0].toString());
			
			this.resetButtons();
		}
		
		//create timer
		uTimer = new Timer(1000, this);
		uTimer.start();
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
		
		//check if an editor button is clicked
		for(JButton btn : this.eButtons.values()) {
			if(e.getSource() == btn) {
				EditorOption opt = EditorOption.valueOf(btn.getName());
				
				if(opt != null)
					editMenu(opt);
			}
		}
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
			if(e.getSource() == btn) {
				btn.setBackground(menuModel.btnActiveColor);
				this.openPage(btn.getName());
			}
			else
				btn.setBackground(menuModel.btnBgColor);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = ((JSlider)e.getSource());
		
		if(slider == null)
			return;
		
		SimSetting setting = this.settings != null && this.settings.containsKey(slider.getName()) ? this.settings.get(slider.getName()) : null;
		
		if(setting == null)
			return;
		
		this.setSetting(setting, (int)slider.getValue());
	}
	
	public void openPage(String pageName)
	{
		//check if this page is already open
		if(this.currPage != null && this.currPage.toString() == pageName)
			return;
		
		//check if page name exists
		if(!pageExists(pageName))
			return;
		
		MenuOption mOption = MenuOption.valueOf(pageName);
		JPanel page;
		
		if(mPages.containsKey(pageName))
			page = mPages.get(pageName);
		else {
			page = CreatePage(mOption);
			this.mPages.put(pageName, page);
		}
		
		if(this.currPage != null)
			menuModel.remove(this.currPage);
		
		if(this.getEditor() != null)
			this.remove(this.getEditor());
		
		this.currPage = page;
		
		if(this.getEditor() != null && this.hasEditor(mOption))
			this.add(this.getEditor());
		
		menuModel.add(this.currPage);
		
		menuModel.repaint();
	}
	
	public boolean pageExists(String pageName)
	{
		for(MenuOption option : MenuOption.values())
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
	
	public JPanel CreatePage(MenuOption type) {
		JPanel page = new JPanel();
		
		page.setLayout(null);
		page.setBounds(this.menuModel.borderWidth, 0, this.menuModel.mainWidth, this.hasEditor(type) ? (this.menuModel.mainHeight - this.menuModel.topHeight - this.menuModel.editorHeight - this.menuModel.borderWidth) : (this.menuModel.mainHeight - this.menuModel.topHeight - this.menuModel.borderWidth));
		page.setBackground(this.menuModel.contentColor);
		
		//set default variables for the page
		int sIndex = 0;
		int mTop = 5;
		int mLeft = 15;
		int height = 40;
		
		switch(type) {
			case Instellingen:
				this.settings = mainModel.GetSettings();
				
				//label size
				int lWidth = 45;
				int lHeight = 50;
				int lMargin = 2;
				
				//slider size
				int sWidth = menuModel.mainWidth - mLeft * 2 - lWidth;
				int sHeight = 20;
				
				for(SimSetting setting : this.settings.values()) {
					if(!setting.isCustomizeable())
						continue;
					
					//text
					JLabel text = new JLabel(setting.getName());
					text.setBounds(mLeft, mTop + sIndex*height, menuModel.mainWidth - mLeft*2, height - sHeight);
					
					//create slider
					//System.out.println(setting.getName() + " min:" + setting.getMin() + " max:" + setting.getMax() + " val: " + setting.getVal());
					
					JSlider slider = new JSlider(JSlider.HORIZONTAL, setting.getMin(), setting.getMax(), setting.getVal());
					
					slider.setMajorTickSpacing(setting.getMajorSpacing());
					slider.setMinorTickSpacing(setting.getMinorSpacing());
					slider.setPaintTicks(false);
					slider.setPaintLabels(false);
					slider.setName(setting.getId());
					
					slider.setBounds(mLeft, mTop + sIndex*height + height - sHeight, sWidth - lMargin*2, mTop + sHeight);
					slider.setBackground(null);
					
					slider.addChangeListener(this);
					
					//add slider to the array
					this.mSliders.put(setting.getId(), slider);
					
					//label
					JLabel label = new JLabel(setting.val(), SwingConstants.CENTER);
					label.setName(setting.getId());
					label.setBounds(mLeft + sWidth - lMargin*2, mTop + sIndex*height - sHeight + (lHeight / 2), lWidth, lHeight);
					
					//add label to the array
					this.mLabels.put(setting.getId(), label);
					
					//add the items to the page
					page.add(slider);
					page.add(label);
					page.add(text);
					
					sIndex++;
				}
				
				break;
				
			case Statistieken:
				this.stats = mainModel.GetStats();
				
				height = 20;
				
				for(SimStat stat : this.stats.values()) {
					//text
					JLabel text = new JLabel(stat.getName() + ": " + stat.val());
					text.setBounds(mLeft, mTop + sIndex*height, menuModel.mainWidth - mLeft*2, height);
					
					this.mLabels.put(stat.getId(), text);
					
					page.add(text);
					
					sIndex++;
				}
				
				break;
		}
		
		return page;
	}
	
	public boolean hasEditor(MenuOption pageName) {
		switch(pageName) {
			case Instellingen:
				return true;
			default:
				return false;
		}
	}
	
	public void showEditor(boolean visible) {
		JPanel editor = this.getEditor();
		
		if(editor != null)
			editor.setVisible(visible);
	}
	
	public JPanel getEditor() {
		JPanel editor = this.editor;
		
		if(this.editor == null) {
			editor = new JPanel();
			
			int mRight = 5;
			int sRight = 12;
			int bHeight = 28;
			int bWidth = 100;
			int bIndex = EditorOption.values().length;
			
			//editor.setLayout(null);
			editor.setLayout(null);
			editor.setBackground(null);
			editor.setBounds(0,this.menuModel.mainHeight - this.menuModel.editorHeight,this.menuModel.editorWidth,this.menuModel.editorHeight);
			
			//add editor buttons to system to detect
			this.eButtons = new HashMap<String, JButton>();
			
			for(EditorOption eButton : EditorOption.values()) {
				JButton btn = new JButton();
				
				btn.setText(eButton.name());
				btn.setBounds(this.menuModel.mainWidth - sRight - (bIndex * (bWidth + mRight)), (this.menuModel.editorHeight - bHeight) / 2,bWidth,bHeight);
				btn.setName(eButton.name());
				
				btn.addMouseListener(this);
				
				editor.add(btn);
				
				this.editor = editor;
				
				this.eButtons.put(eButton.toString(), btn);
				
				bIndex--; 
			}
		}
		
		this.repaint();
		
		return this.editor;
	}
	
	public void editMenu(EditorOption opt) {
		switch(opt) {
			case RESET:
				this.resetSettings();
				
				break;
			case STOP:
				this.mainModel.stop();
				this.mainModel.reset();
				
				break;
			case START:
				this.saveSettings();
				
				this.mainModel.reset();
				this.mainModel.start();
				
				break;
		}
	}
	
	public void resetSettings() {
		if(this.mainModel == null || this.mainModel.GetSettings() == null)
			return;
		
		HashMap<String, SimSetting> settings = new HashMap<String, SimSetting>(this.mainModel.GetSettings());
		
		for(SimSetting setting : settings.values()) {
			this.settings.put(setting.getId(), setting.clone());
			setting.reset();
			
			this.setSetting(setting, setting.getVal());
		}
	}
	
	public void setSetting(SimSetting setting, int val) {
		if(setting == null)
			return;
		
		JSlider sData = this.mSliders != null && this.mSliders.containsKey(setting.getId()) ? this.mSliders.get(setting.getId()) : null;
		JLabel lData = this.mLabels != null && this.mLabels.containsKey(setting.getId()) ? this.mLabels.get(setting.getId()) : null;
		
		if(sData == null || lData == null)
			return;
		
		setting.setValue(val);
		sData.setValue(val);
		lData.setText(setting.val());
	}
	
	public void saveSettings() {
		if(this.mainModel == null)
			return;
		
		this.mainModel.SetSettings(new HashMap<>(this.settings));
	}
	
	public void updateStats() {
		if(this.mainModel == null)
			return;
		
		HashMap<String, SimStat> stats = this.mainModel.GetStats();
		
		if(stats == null)
			return;
		
		for(SimStat stat : stats.values()) {
			if(stat == null || !this.mLabels.containsKey(stat.getId()))
				continue;
			
			JLabel lData = this.mLabels.get(stat.getId());
			
			lData.setText(stat.getName() + ": " + stat.val());
		}
			
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.updateStats();
	}
}

enum EditorOption {
	RESET,
	STOP,
	START
}
