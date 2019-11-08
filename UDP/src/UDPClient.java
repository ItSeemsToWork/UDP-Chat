import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;

class UDPClient {
	
	static LinkedList<String> tmplist = new LinkedList<>();
	
	public static void main(String args[]) throws Exception {
		
		
		String name = null;
		DatagramSocket clientSocket = new DatagramSocket(Integer.parseInt(args[2]));
		System.out.println("Client start on " +  args[2] + " port");
		
		new Thread() {
            @Override
            public void run() {
            	while(true) {
            		try {
						byte[] receiveData = new byte[1024];
						DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
						clientSocket.receive(receivePacket);
						String answer = new String(receivePacket.getData());
						tmplist.addLast(clearString("from server:" + answer));
					    System.out.println("from server:" + answer);
					} catch (SocketException e) {
						e.printStackTrace();
					}catch (IOException e) {
						e.printStackTrace();
					}
            	  }
            	}
            }.start();
            
            
		
		while(true) {
			byte[] sendData = new byte[1024];
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    String message = reader.readLine();
		    if(message.startsWith("@dump")) {
		    	dump(message.substring(6, message.length()));
		    }
		    else {
		    	tmplist.addLast(clearString(message));
		    	InetAddress ipAddress = InetAddress.getByName(args[0]);
		        sendData = message.getBytes();
		        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, Integer.parseInt(args[1]));
		        clientSocket.send(sendPacket);
		    }
		}
		
	}
	
	private static void dump(String filename) {
		    try {
				DataOutputStream writer = new DataOutputStream( new FileOutputStream(filename));
				Iterator<String> iter = tmplist.iterator();
				while(iter.hasNext()) {
					writer.writeBytes("["+iter.next()+"]\r\n");
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private static String clearString(String str) {
		str = str.trim().replaceAll("(?u)[^\\p{L}., ]"," ");
		str = str.replaceAll("[^a-zA-Z .,]", " ");
		str = str.replaceAll("\\s{2,}", " ");
		return str;
		}
}
