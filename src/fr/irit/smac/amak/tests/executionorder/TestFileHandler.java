package fr.irit.smac.amak.tests.executionorder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONObject;

import fr.irit.smac.amak.tools.FileHandler;

public class TestFileHandler {	

	

	public static void main(String[] args) {

		JSONObject json = new JSONObject();
		json.put("Name","Guilhem");
		
		//FileHandler.writeJSON("testJSON", json);		
		/*FileHandler.writeJSON("testJSON", new Entity(10,"A"));
		FileHandler.writeJSON("testJSON", new Entity(20,"B"));
		FileHandler.writeJSON("testJSON", new Entity(30,"C"));
		FileHandler.writeJSON("testJSON", new Entity(40,"D"));*/
		
		List<String> list = new ArrayList<String>();
		list.add("je");
		list.add("suis");
		list.add("moi");
		FileHandler.writeCSV("testCSV.csv", list, true);

	}



}
