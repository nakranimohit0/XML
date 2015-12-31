import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		String xml = "";
		File in = new File("xmlIn.txt");
		
		try (Scanner scan = new Scanner(in)) {
			while (scan.hasNext()) xml += scan.next();
		}
		catch (IOException e) {
			System.err.println(e);
		}
		
		XMLTag lst = new XMLTag(xml);
		print(lst);
		print(lst.find("num1").get(0).find("txt"));
		print(lst.find("num2").get(0).xml);
		print(lst.name);
	}
	
	public static void print(Object o) {
		System.out.println(o);
	}
}

class XMLTag {
	private static boolean first = true;
	public String name;
	public ArrayList<XMLTag> data;
	public String xml;
	
	public XMLTag(String x) {
		if (x == null || x.trim().equals("")) {
			name = null;
			data = null;
			xml = null;
			return;
		}
		x = x.trim();
		
		if (first) {
			x = "<xml>" + x + "</xml>";
			first = false;
		}
		if (x.indexOf("<") == -1 || x.indexOf(">") == -1) {
			name = x;
			data = null;
			xml = null;
			return;
		}
		name = x.substring(x.indexOf("<") + 1, x.indexOf(">"));
		
		if (x.indexOf("</" + name + ">") == -1) {
			data = null;
			xml = null;
			return;
		}
		xml = x.substring(x.indexOf("<" + name + ">") + 2 + name.length(), x.indexOf("</" + name + ">")).trim();
		data = extractData(xml);
	}
	
	private ArrayList<XMLTag> extractData(String x) {
		ArrayList<XMLTag> res = new ArrayList<XMLTag>();
		
		if (x == null || x.trim().equals("")) return null;
		if (x.indexOf("<") == -1 || x.indexOf(">") == -1) {
			res.add(new XMLTag(x));
			return res;
		}
		x = x.trim();
		
		String x1 = "";
		String x2 = "";
		String n = x.substring(x.indexOf("<") + 1, x.indexOf(">"));

		x1 = x.substring(0, (x.indexOf("</" + n + ">") == -1) ? x.indexOf(">") + 1 : x.indexOf("</" + n + ">") + 3 + n.length());
		res.add(new XMLTag(x1));
		
		if (x.substring(x1.length() - 1).trim().length() == 1) return res;
		x2 = x.substring(x1.length());
		
		for (XMLTag t: extractData(x2)) res.add(t);
		return res;
	}
	
	public ArrayList<XMLTag> find(String s) {
		ArrayList<XMLTag> res = new ArrayList<XMLTag>();
		for (XMLTag t: data) if (t.name.equals(s)) res.add(t);
		return res;
	}
	
	public String toString() {
		return name + ((data != null) ? (": " + data) : "");
	}
}
