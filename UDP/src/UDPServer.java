import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


class UDPServer {
	
	public static void main(String args[]) throws Exception {
		
		String name = null;
		DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(args[1]));
		System.out.println("Server start on " +  args[1] + " port");
		
		
		new Thread() {
            @Override
            public void run() {
            	while(true) {
        			try {
        				byte[] receiveData = new byte[1024];
            			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
						serverSocket.receive(receivePacket);
						String answer = new String(receivePacket.getData());
	        			System.out.println("from client: " + answer);
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            }
        }.start();
		
            
		while(true) {
			byte[] sendData = new byte[1024];
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String message = reader.readLine();
			InetAddress ipAddress = InetAddress.getByName(args[0]);
			sendData = message.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, Integer.parseInt(args[2]));
			serverSocket.send(sendPacket);
			}
		}
}
