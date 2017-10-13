package fr.irit.smac.amak.tests;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import fr.irit.smac.amak.tools.FileHandler;

public class TestFileHandler {	

	

	@SuppressWarnings("unchecked")
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
		FileHandler.writeCSVLine("testCSV.csv", list.toArray(new String[0]));

	}



}
