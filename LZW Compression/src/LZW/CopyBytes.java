/**
 * author mingyi liang
 */


package LZW;
import java.io.*;

public class CopyBytes {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		DataInputStream in =
				new DataInputStream(
				  new BufferedInputStream(
				    new FileInputStream(args[0])));
				DataOutputStream out =
				  new DataOutputStream(
				    new BufferedOutputStream(
				      new FileOutputStream(args[1])));
				byte byteIn; 
				try {
				   while(true) {
				       byteIn = in.readByte(); 
				       out.writeByte(byteIn);
				    } 
				 }
				catch(EOFException e) { 
					in.close();
				    out.close(); 
				 }
		}

	}


