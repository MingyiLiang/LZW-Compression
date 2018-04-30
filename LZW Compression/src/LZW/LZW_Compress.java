package LZW;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class LZW_Compress {
	private static String input_File = null;
	private static double Max_size;
	private static String LZWfilename;
	
	
	public static void Compress_file(String input) throws IOException{
		Max_size = Math.pow(2, 12);
		int table_size = 127;
		LinkedList<String>[] table = new LinkedList[table_size];
		for (int i=0; i<table_size; i++) {
			table[i] = new LinkedList<String>();
		}
		
		for(int i = 0; i < 127; i++) {
			table[i].add("" + (char) i);
		}
		String initString = "";
		List<Integer> compress_value = new ArrayList<Integer>();
		
		for(char symbol : input.toCharArray() ) {
			String Str_Symbol = initString + symbol;
			if(haskey(table,Str_Symbol)) {
				initString = Str_Symbol;
			}else {
				compress_value.add(getkey(table,initString));
				if (table_size < Max_size) {
					table[table_size++].add(Str_Symbol);
				}
				initString = "" + symbol;
				
			}
			if (!initString.equals("")) {
				compress_value.add(getkey(table,initString));
			}
			
			create_file(compress_value);
		}		
		
	}
	
	public static void create_file(List<Integer> list_values) throws IOException{
        
		BufferedWriter out = null;
		
		LZWfilename = input_File.substring(0,input_File.indexOf(".")) + ".lzw";
		
		try {
	            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LZWfilename),"UTF_12BE")); //The Charset UTF-16BE is used to write as 16-bit compressed file
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Iterator<Integer> Itr = list_values.iterator();
			while (Itr.hasNext()) {
				out.write(Itr.next());
			}
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		
		out.flush();
		out.close();
	}
	
	public static boolean haskey(LinkedList<String>[] table, String key) {
		boolean has = false;
		for(int i = 0; i < 127; i++) 
			if (table[i].contains(key)) {
				has = true;
			}
					
		return has;
	}
	
	public static int getkey(LinkedList<String>[] table, String key) {
		int table_key = 0;
		for(int i = 0; i < 127; i++) 
			if (table[i].contains(key)) {
				table_key = i;
			}
		return table_key;
	}
	
	public static void main(String[] args) throws IOException {
		input_File = args[0];
		
		
		StringBuffer input_string1 = new StringBuffer();
		
		try (BufferedReader br = Files.newBufferedReader(Paths.get(input_File), StandardCharsets.UTF_8)) {
		    for (String line = null; (line = br.readLine()) != null;) {
		        
		    	input_string1 = input_string1.append(line);
		    }
		}
	
		Compress_file(input_string1.toString());
	}

}
