package LZW;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class LZWCompression {

	public int count;
	
	
	/**
	 * conductor
	 */
	public LZWCompression() {
		
	}
	
	@SuppressWarnings("deprecation")
	public void LZW_Compress(String input, String output) throws IOException{	
		// enter all symbols into the table
		SinglyBigLinkedList[] table = new SinglyBigLinkedList[127];
		for(int i = 0; i < 127; i ++) {
			table[i] = new SinglyBigLinkedList();		
		}
		for(int i = 0; i < 256; i ++) {
			table[Math.abs((String.valueOf((char) i).hashCode()))%127].addNodeEnd(i, String.valueOf((char) i));
		}
			
		count = 256;
		
		
		

		/** Pointer to input and output file */
		DataInputStream read = new DataInputStream(new BufferedInputStream(new FileInputStream(input)));

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output)));

		/** Local Variables */
		byte input_byte;
		String temp = "";
		byte[] buffer = new byte[3];
		boolean onleft = true;

		try {

			/** Read the First Character from input file into the String */
			input_byte = read.readByte();
			int i = new Byte(input_byte).intValue();
			
			if (i < 0) {
				i += 256;
			}
			//char c = (char) i;
			String c = String.valueOf((char)i);
			temp = "" + c;

			/** Read Character by Character */
			while (true) {
				input_byte = read.readByte();
				i = new Byte(input_byte).intValue();
				//System.out.println(i);
				if (i < 0) {
					i += 256;
				}
				//c = (char) i;
				c = String.valueOf((char)i);
				//System.out.println(c);

				if (haskey(table,temp + c) == true){
					temp = temp + c;
					
				} else {
					String s12 = to12bit(getdata(table,temp));
					//System.out.println(getdata(table,temp));
					/**
					 * Store the 12 bits into an array and then write it to the
					 * output file
					 */

					if (onleft) {
						buffer[0] = (byte) Integer.parseInt(s12.substring(0, 8), 2);
						buffer[1] = (byte) Integer.parseInt(s12.substring(8, 12) + "0000", 2);
					} else {
						buffer[1] += (byte) Integer.parseInt(s12.substring(0, 4), 2);
						buffer[2] = (byte) Integer.parseInt(s12.substring(4, 12), 2);
						for (int b = 0; b < buffer.length; b++) {
							//System.out.println(buffer[b]);
							out.writeByte(buffer[b]);
							buffer[b] = 0;
						}
					}
					onleft = !onleft;
					if (count < 4096) {
						
						count = count +1;
						table[count%127].addNodeEnd(count, temp + c);
					}
					temp = "" + c;
				}
			}

		} catch (EOFException e) {
			String temp_12 = to12bit(getdata(table,temp));
			if (onleft) {
				buffer[0] = (byte) Integer.parseInt(temp_12.substring(0, 8), 2);
				buffer[1] = (byte) Integer.parseInt(temp_12.substring(8, 12)+ "0000", 2);
				out.writeByte(buffer[0]);
				out.writeByte(buffer[1]);
			} else {
				buffer[1] += (byte) Integer.parseInt(temp_12.substring(0, 4), 2);
				buffer[2] = (byte) Integer.parseInt(temp_12.substring(4, 12), 2);
				for (int b = 0; b < buffer.length; b++) {
					out.writeByte(buffer[b]);
					buffer[b] = 0;
				}
			}
			read.close();
			out.close();
		}		
		
		
		
	}
	
	
	public void LZW_Decompress(String input, String output) throws IOException{
		SinglyBigLinkedList[] table = new SinglyBigLinkedList[127];
		for(int i = 0; i < 127; i ++) {
			table[i] = new SinglyBigLinkedList();		
		}
		for(int i = 0; i < 256; i ++) {
			table[Math.abs((String.valueOf((char) i).hashCode()))%127].addNodeEnd(i, String.valueOf((char) i));
		}
			
		count = 256;
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(input)));
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
		int cw;
		int pw;
		byte[] buffer = new byte[3];
		boolean leftone = true;
		try {
			//read priorword and output its corresponding character
			buffer[0] = in.readByte();
			buffer[1] = in.readByte();
			pw = getvalue(buffer[0], buffer[1], leftone);
			leftone = !leftone;
			out.writeBytes(String.valueOf((char) pw));
			while(true) {
				if(leftone) {
					buffer[0] = in.readByte();
					buffer[1] = in.readByte();
					cw = getvalue(buffer[0], buffer[1], leftone);
				}else {
					buffer[2] = in.readByte();
					cw = getvalue(buffer[1], buffer[2], leftone);
				}
				leftone = !leftone;
				if(cw >= count) {
					if(count < 4096) {
					  table[Math.abs((String.valueOf((char) count).hashCode()))%127].addNodeEnd(count, String.valueOf((char)pw)+String.valueOf((char)pw).charAt(0));					
					}
					count ++;
					out.writeBytes(String.valueOf((char)pw)+String.valueOf((char)pw).charAt(0));
				}else {
					if(count < 4096) {
					table[Math.abs((String.valueOf((char) count).hashCode()))%127].addNodeEnd(count, String.valueOf((char)pw)+String.valueOf((char)cw).charAt(0));					
					}
					count++;
					out.writeBytes(String.valueOf((char)cw));
				}
				
				pw = cw;						
			}			
		}catch(EOFException e) {
			in.close();
			out.close();
		}		
	}
	
	
	/**
	 * check if the table has that key
	 * @param table
	 * @param key
	 * @return
	 */
	public static boolean haskey(SinglyBigLinkedList[] table, String key) {
		boolean has = false;
		for(int i=0; i < table[Math.abs(key.hashCode())%127].getSize(); i++) {
			if(table[Math.abs(key.hashCode())%127].get(i).getKey().equals(key)) {
				has = true;
				break;
			}
		}		
		return has;
	}
	
	/**
	 * convert 8 to 12 bit
	 * @param args
	 */
	
	public String to12bit(int i) {
		String temp = Integer.toBinaryString(i);
		while (temp.length() < 12) {
			temp = "0" + temp;
		}
		return temp;
	}
    
	/**
	 * get value from linked list
	 * @param args
	 */
	public static int getdata(SinglyBigLinkedList[] table, String key) {

		int table_data = 0;
		for(int i=0; i < table[Math.abs((key.hashCode()) % 127)].getSize(); i++) {
			//System.out.print("#");
			if(table[Math.abs((key.hashCode()) % 127)].get(i).getKey().equals(key)) {
				table_data = table[Math.abs((key.hashCode()) % 127)].get(i).getData();
				//System.out.print(table_data);
				break;
			}
		}
		return table_data;
	
	}
	
	/**
	 * get key from linkedlist
	 * @param table
	 * @param data
	 * @return
	 */
	public static String getkey(SinglyBigLinkedList[] table, Integer data) {
		String table_key = null;
		
		for (int i=0; i < table[data%127].getSize();i++) {
			if (table[data % 127].get(i).getData() == data) {
				table_key = table[data % 127].get(i).getKey();
				break;
			}
		}
		return table_key;
	}
	
	
	/**
	 * extract 12bits from two bytes
	 * @param b1
	 * @param b2
	 * @param onleft
	 * @return
	 */
	public int getvalue(byte b1, byte b2, boolean leftone) {
		String temp1 = Integer.toBinaryString(b1);
		String temp2 = Integer.toBinaryString(b2);
		while (temp1.length() < 8) {
			temp1 = "0" + temp1;
		}
		if (temp1.length() == 32) {
			temp1 = temp1.substring(24, 32);
		}
		while (temp2.length() < 8) {
			temp2 = "0" + temp2;
		}
		if (temp2.length() == 32) {
			temp2 = temp2.substring(24, 32);
		}

		/** On left being true */
		if (leftone) {
			return Integer.parseInt(temp1 + temp2.substring(0, 4), 2);
		} else {
			return Integer.parseInt(temp1.substring(4, 8) + temp2, 2);
		}

	}

	public static void main(String[] args) throws IOException {
		 //TODO Auto-generated method stub
		LZWCompression lzw = new LZWCompression();

		System.out.println("Please enter the command\n ");
		System.out.println("Command for Compression is: java lzw c shortwords.txt zippedfile.txt");
		System.out.println("Command for Decmpression is : java lzw d zippedfile.txt unzippedfile.txt");

		/** Scanner object is created to read the values from the keyboard */

		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			String line = in.nextLine();
			String arg[] = line.split(" ");
			if (arg[2].equals("c")) {
				
				lzw.LZW_Compress(arg[3], arg[4]);
				

			}
			if (arg[2].equals("d")) {
				
				lzw.LZW_Decompress(arg[3], arg[4]);
			

			}
		}

	  in.close();
		
		

	     
	     
	     
		

	}

}
