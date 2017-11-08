import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Random;


public class Ipv6Client{

	public static void main(String[] args)throws IOException{
		try(Socket socket = new Socket("18.221.102.182", 38004)){
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			byte[] packet;
			for(int i = 1; i < 13; i++){
				int dataSize = (int)Math.pow(2.0, i);
				short totalLength = (short)(40 + dataSize);
				packet = new byte[totalLength];
				new Random().nextBytes(packet); 
				
				packet[0] = 0b01100000; 
				packet[1] = 0; 
				packet[2] = 0; 
				packet[3] = 0; 
				packet[4] = (byte)((dataSize & 0xFF00) >> 8); 
				packet[5] = (byte)(dataSize & 0x00FF); 
				packet[6] = 17;
				packet[7] = 20; 
				byte[] ipv4Source = new byte[4];
				new Random().nextBytes(ipv4Source); 
				for(int j = 8; j < 18; j++) 
					packet[j] = 0;
				for(int j = 18; j < 20; j++) 
					packet[j] = (byte)0xFF;
				for(int j = 0; j < 4; j++) 
					packet[j+20] = ipv4Source[j];
				byte[] ipv4Dest = socket.getInetAddress().getAddress();
				for(int j = 24; j < 34; j++) 
					packet[j] = 0;
				for(int j = 34; j < 36; j++) 
					packet[j] = (byte)0xFF;
				for(int j = 0; j < 4; j++) 
					packet[j+36] = ipv4Dest[j];
				out.write(packet);
				byte[] code = new byte[4];
				in.read(code);
				System.out.println("data length: " + dataSize);
				System.out.print("0x");
/*				int fullCode = 0;
				for(int j = 0; j < 4; j++){
					int temp = 0;
					temp |= code[j];
					temp <<= 8*(3-j);
					fullCode |= temp;
				}
				System.out.printf("%X", fullCode);*/
				for(byte e: code)
					System.out.printf("%X", e);
				System.out.println();
			}
		}
	}
}