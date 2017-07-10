package fr.irit.smac.amak.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

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

	private static final char DEFAULT_SEPARATOR = ',';
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
	public static void writeLine(String name, String line, boolean ecrase){
		try {
			FileWriter fw = new FileWriter(name,ecrase);
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
	public static void writeJSON(String name, Object obj, boolean ecrase){
		JsonHierarchicalStreamDriver jet = new JsonHierarchicalStreamDriver();
		XStream xstream = new XStream(jet);
		xstream.setMode(XStream.NO_REFERENCES);
		FileWriter file;
		try {
			file = new FileWriter(name+".json",ecrase);
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
	public static void writeXML(String name, Object obj, boolean ecrase){
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(obj);
	        try {
	        	File file = new File(name);
	        	FileWriter fw = new FileWriter(file,ecrase);
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
	public static void writeCSV(String name, List<String> values, boolean ecrase){
        FileWriter writer;
		try {
			writer = new FileWriter(name,ecrase);
			FileHandler.writeLine(writer,values);
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
     * 			The writer
     * @param values
     * 			The String to write
     * @param separators
     * 			The separtors
     * @param customQuote
     * 			The customQuote
     * @throws IOException
     */
    private static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        //default customQuote is empty

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
	
	
}
