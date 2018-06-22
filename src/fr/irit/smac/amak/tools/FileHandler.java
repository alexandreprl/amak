package fr.irit.smac.amak.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * @author Marcillaud Guilhem
 *
 */
public class FileHandler {
	/**
	 * Files which have been opened for reading
	 */
	private static Map<String, BufferedReader> openedReadableFiles = new HashMap<>();
	/**
	 * Files which have been opened for writing
	 */
	private static Map<String, PrintWriter> openedWritableFiles = new HashMap<>();

	/**
	 * Clear the content of a file
	 * 
	 * @param name
	 *            The name of the file
	 */
	public static void clearFile(String name) {
		new File(name).delete();
	}

	/**
	 * Write a JSONObject in the file
	 * 
	 * @param filename
	 *            The name of the file
	 * 
	 * @param obj
	 *            The Object
	 */
	public static void writeJSON(String filename, Object obj) {
		JsonHierarchicalStreamDriver jet = new JsonHierarchicalStreamDriver();
		XStream xstream = new XStream(jet);
		xstream.setMode(XStream.NO_REFERENCES);
		writeLine(filename, xstream.toXML(obj));
	}

	/**
	 * Write an Object in a xml file
	 * 
	 * @param filename
	 *            The name of the file
	 * 
	 * @param obj
	 *            The object
	 */
	public static void writeXML(String filename, Object obj) {
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(obj);
		writeLine(filename, xml);
	}

	/**
	 * Read a json object from a file
	 * 
	 * @param filename
	 *            The name of the file that must be opened
	 * @return the JSON object read
	 */
	public static JSONObject readJSONObject(String filename) {
		JSONParser parser = new JSONParser();
		JSONObject object;
		try {
			object = (JSONObject) parser.parse(new FileReader(filename));
			return object;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read a line from a file
	 * 
	 * @param filename
	 *            The name of the file that must be read
	 * @return the content of the line
	 */
	public static String readLine(String filename) {
		try {
			if (!openedReadableFiles.containsKey(filename)) {
				openedReadableFiles.put(filename, new BufferedReader(new FileReader(filename)));
			}
			BufferedReader csvReader = openedReadableFiles.get(filename);
			String line;
			line = csvReader.readLine();
			if (line == null) {
				csvReader.close();
				openedReadableFiles.remove(filename);
				return null;
			} else {
				return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write a line as CSV
	 * 
	 * @param filename
	 *            The name of the file that must be read
	 * @param params
	 *            The data that must be written
	 */
	public static void writeCSVLine(String filename, CharSequence... params) {
		writeLine(filename, String.join(",", params));
	}
	/**
	 * Write a line to a file. This method is used by the others
	 * @param filename the file in which to write
	 * @param line the line to write
	 * @param params the parameters that should be added to the string "line" ({@link String#format(java.util.Locale, String, Object...)})
	 */
	public static void writeLine(String filename, String line, Object... params) {
		try {
			if (!openedWritableFiles.containsKey(filename)) {
				openedWritableFiles.put(filename, new PrintWriter(filename));
			}
			PrintWriter csvWriter = openedWritableFiles.get(filename);
			csvWriter.println(String.format(line, params));
			csvWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
