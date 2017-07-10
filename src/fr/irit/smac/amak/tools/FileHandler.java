package fr.irit.smac.amak.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.security.auth.login.Configuration;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.json.simple.JSONObject;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * @author Marcillaud Guilhem
 *
 */
public class FileHandler {

	/**
	 * Clear the content of a file
	 * 
	 * @param name
	 * 			The name of the file
	 */
	public static void clearFile(String name){
		File file = new File(name);
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write a line in a file
	 * 
	 * @param name
	 * 			The name of the file
	 * 
	 * @param line
	 * 			The line to write
	 */
	public static void writeLine(String name, String line){
		try {
			FileWriter fw = new FileWriter(name,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(line);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write a JSONObject in the file
	 * 
	 * @param name
	 * 			The name of the file
	 * 
	 * @param obj
	 * 			The Object
	 */
	public static void writeJSON(String name, Object obj){
		JsonHierarchicalStreamDriver jet = new JsonHierarchicalStreamDriver();
		XStream xstream = new XStream(jet);
		xstream.setMode(XStream.NO_REFERENCES);
		FileWriter file;
		try {
			file = new FileWriter(name+".json",true);
			file.write(xstream.toXML(obj));
			file.flush();
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Write an Object in a xml file
	 * 
	 * @param name
	 * 			The name of the file
	 * 
	 * @param obj
	 * 			The object
	 */
	public static void writeXML(String name, Object obj){
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(obj);
	        try {
	        	File file = new File(name);
	        	FileWriter fw = new FileWriter(file,true);
	        	fw.write(xml);
	        	fw.flush();
	        	fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
}
