package Parkeersimulator.Controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Parkeersimulator.Logic.*;
import Parkeersimulator.*;

import javax.swing.*;
import java.awt.*;

public class MapController extends JPanel {
	private MainModel mainModel;
	private MapModel mapModel;
	
	public MapController(MainModel mainModel, MapModel mapModel) {
		this.mainModel = mainModel;
		this.mapModel = mapModel;
	}
}
