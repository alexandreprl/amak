package fr.irit.smac.amak.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
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

	private static final char DEFAULT_SEPARATOR = ',';
	private static Map<String, BufferedReader> openedCsvFiles = new HashMap<>();
	private static Map<String, PrintWriter> openedWritableCsvFiles = new HashMap<>();

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
	 * Write a line in a file
	 * 
	 * @param name
	 *            The name of the file
	 * 
	 * @param line
	 *            The line to write
	 */
	public static void writeLine(String name, String line, boolean overwrite) {
		try {
			FileWriter fw = new FileWriter(name, overwrite);
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
	 *            The name of the file
	 * 
	 * @param obj
	 *            The Object
	 */
	public static void writeJSON(String name, Object obj, boolean overwrite) {
		JsonHierarchicalStreamDriver jet = new JsonHierarchicalStreamDriver();
		XStream xstream = new XStream(jet);
		xstream.setMode(XStream.NO_REFERENCES);
		FileWriter file;
		try {
			file = new FileWriter(name + ".json", overwrite);
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
	 *            The name of the file
	 * 
	 * @param obj
	 *            The object
	 */
	public static void writeXML(String name, Object obj, boolean overwrite) {
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(obj);
		try {
			File file = new File(name);
			FileWriter fw = new FileWriter(file, overwrite);
			fw.write(xml);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Write in csv format
	 * 
	 * @param name
	 * 
	 * @param values
	 * 
	 */
	public static void writeCSV(String name, List<String> values, boolean overwrite) {
		FileWriter writer;
		try {
			writer = new FileWriter(name, overwrite);
			FileHandler.writeLine(writer, values);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param w
	 * @param values
	 * @throws IOException
	 */
	private static void writeLine(Writer w, List<String> values) throws IOException {
		writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	}

	private static void writeLine(Writer w, List<String> values, char separators) throws IOException {
		writeLine(w, values, separators, ' ');
	}

	/**
	 * Used to foolow the csv format
	 * 
	 * @param value
	 * 
	 * @return result
	 */
	private static String followCVSformat(String value) {

		String result = value;
		if (result.contains("\"")) {
			result = result.replace("\"", "\"\"");
		}
		return result;

	}

	/**
	 * Write the line in csv format
	 * 
	 * @param w
	 *            The writer
	 * @param values
	 *            The String to write
	 * @param separators
	 *            The separtors
	 * @param customQuote
	 *            The customQuote
	 * @throws IOException
	 */
	private static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

		boolean first = true;

		// default customQuote is empty

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			if (!first) {
				sb.append(separators);
			}
			if (customQuote == ' ') {
				sb.append(followCVSformat(value));
			} else {
				sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
			}

			first = false;
		}
		sb.append("\n");
		w.append(sb.toString());

	}

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

	public static String readCSVLine(String filename) {
		try {
			if (!openedCsvFiles.containsKey(filename)) {
				openedCsvFiles.put(filename, new BufferedReader(new FileReader(filename)));
			}
			BufferedReader csvReader = openedCsvFiles.get(filename);
			String line;
			line = csvReader.readLine();
			if (line == null) {
				csvReader.close();
				openedCsvFiles.remove(filename);
				return null;
			} else {
				return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String writeCSVLine(String filename, String line, Object... params) {
		try {
			if (!openedWritableCsvFiles.containsKey(filename)) {
				openedWritableCsvFiles.put(filename, new PrintWriter(filename));
			}
			PrintWriter csvWriter = openedWritableCsvFiles.get(filename);
			csvWriter.println(String.format(line, params));
			csvWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
