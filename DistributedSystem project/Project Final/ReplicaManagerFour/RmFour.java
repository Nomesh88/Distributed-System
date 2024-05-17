package ReplicaManagerFour;

import ReplicaManagerOne.Message;
import ReplicaManagerOne.MessageComparator;
import Utils.PortsAndIPs;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import static java.lang.Thread.sleep;

public class RmFour {
	private static int nextSequence = 1;
	private static PriorityQueue<Message> pq = new PriorityQueue<Message>(20, new MessageComparator());
	private static final String rmName = "4";

	private static boolean testMode = false;
	private static String testServer = "";
	private static boolean recovering = false;
	private static int consecutiveError = 0;
	private static ArrayList<String> history = new ArrayList<>();

	private static Thread atwServer;
	private static Thread verServer;
	private static Thread outServer;
	public static void main(String[] args) {
		
		Runnable task = () -> {
			receive();
		};
		Thread thread = new Thread(task);
		thread.start();
		Runnable task2 = () ->{
		    receiveFeedback();
        };
		Thread thread1 = new Thread(task2);
		thread1.start();

		Runnable task3 = () ->{
			try {
				String[] args1 = new String[1];
				args1[0] = "ATW";
				Server.main(args1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		atwServer = new Thread(task3);
		atwServer.start();

		Runnable task4 = () ->{
			try {
				String[] args2 = new String[1];
				args2[0] = "VER";
				Server.main(args2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		verServer = new Thread(task4);
		verServer.start();

		Runnable task5 = () ->{
			try {
				String[] args3 = new String[1];
				args3[0] = "VER";
				Server.main(args3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		outServer = new Thread(task5);
		outServer.start();

		if(args.length != 0){
			testMode = true;
			testServer = args[0].toUpperCase();
		}
	}

	private static void receive() {
		MulticastSocket aSocket = null;
		try {

			aSocket = new MulticastSocket(PortsAndIPs.RM_Group_PortNum);

			aSocket.joinGroup(InetAddress.getByName(PortsAndIPs.RMGroupIPAddress));

			byte[] buffer = new byte[1000];
			System.out.println("RM4 UDP Server "+PortsAndIPs.RM_Group_PortNum+" Started............");

			while (true) {
			    while(recovering){
                    try {
                        sleep(1000);
						System.out.println("Waiting for recovering ...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);

				String sentence = new String( request.getData(), 0,
						request.getLength() );
						System.out.println(sentence);
				String[] parts = sentence.split(";");

				int sequencerId = Integer.parseInt(parts[7]);

				Message message = new Message(sentence,sequencerId);
				pq.add(message);
				findNextMessage();

				DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),
						request.getPort());
				String msg = new String(reply.getData());

				aSocket.send(reply);
			}

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
	
	public static void findNextMessage() {
		Iterator<Message> itr = pq.iterator();
		while (itr.hasNext()) {
			Message request = itr.next();
			if(request.getsequenceId()==nextSequence) {
				nextSequence = nextSequence+1;
				String message = request.getMessage();
				String[] parts = message.split(";");
				String userID = parts[1]; 
				
				System.out.println(message);
				history.add(message);
				sendMessage(userID,message);
				
			}
		} 			 
	}

	public static void sendMessage(String userID , String message) {
		String libraryPrefix = userID.substring(0, Math.min(userID.length(), 3)).toLowerCase();
		int port=0;
		if(libraryPrefix.equals("atw")) {
			port = PortsAndIPs.RM4_CON_PortNum;
		}else if(libraryPrefix.equals("ver")) {
			port = PortsAndIPs.RM4_MCG_PortNum;
		}else if(libraryPrefix.equals("out")) {
			port = PortsAndIPs.RM4_MON_PortNum;
		}
		if(testMode){
			System.out.println("Test mode start");
			if(libraryPrefix.equalsIgnoreCase(testServer)){
				simulateFailure(message);
			}
			testMode = false;
		}else {

            DatagramSocket aSocket = null;
            try {
                aSocket = new DatagramSocket();
                byte[] messageByte = message.getBytes();
                InetAddress aHost = InetAddress.getByName("localhost");
                DatagramPacket request = new DatagramPacket(messageByte, messageByte.length, aHost, port);
                aSocket.send(request);
                System.out.println(message);
            } catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO: " + e.getMessage());
            } finally {
                if (aSocket != null)
                    aSocket.close();
            }
        }
	}

	private static void simulateFailure(String message) {
		String[] parts = message.split(";");
		String serverName = parts[1].substring(0, Math.min(parts[1].length(), 3)).toLowerCase();
		String randomResult = "Random result"+";"+"1"+";"+serverName;
		int port = Integer.valueOf(message.split(";")[7]);
		String hostName = message.split(";")[8];
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();
			byte[] messageByte = randomResult.getBytes();
			InetAddress aHost = InetAddress.getByName(hostName);
			DatagramPacket request = new DatagramPacket(messageByte, messageByte.length, aHost, port);
			aSocket.send(request);
			System.out.println(message);
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}

	private static void receiveFeedback() {
		MulticastSocket fSocket = null;
		try {
			fSocket = new MulticastSocket(PortsAndIPs.RM_Feedback_Portnum);
			fSocket.joinGroup(InetAddress.getByName(PortsAndIPs.RM_Feedback_IPAddress));
			byte[] buffer = new byte[1000];
			System.out.println("RM4 receive feedbacks at port :"+PortsAndIPs.RM_Feedback_Portnum );

			while (true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				fSocket.receive(request);

				String sentence = new String( request.getData(), 0,
						request.getLength() );
				String[] parts = sentence.split(";");
				String erroType = parts[0];
				if(erroType.equalsIgnoreCase("crash")){
					crashHandle(parts);
				}else if(erroType.equalsIgnoreCase("fault")){
					faultHandle(parts);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void faultHandle(String[] parts) {
		String rmNum = parts[1];
		String serverName = parts[2];
        System.out.println("RM :"+rmNum+" server :"+serverName+" produce incorrect result ");
		if(rmNum.equalsIgnoreCase(rmName)){
			consecutiveError ++;
			System.out.println("Handling fault result...");
			if(consecutiveError >=3){
				System.out.println("Three consecutive incorrect result");
				crashHandle(parts);
			}
		}else{
			consecutiveError = 0;
		}
	}

	public static void crashHandle(String[] parts){
		recovering = true;
		String rmNum = parts[1];
		String serverName = parts[2];
        System.out.println("Potential replica crash at RM : "+rmNum+" Server :"+serverName);
		if(rmNum.equalsIgnoreCase(rmName)) {
			if (serverName.equalsIgnoreCase("atw")) {
                System.out.println("Replacing atw server ...");
                atwServer.interrupt();
				Runnable task = () -> {
					try {
						String[] args = new String[1];
						args[0] = "ATW";
						Server.main(args);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
				atwServer = new Thread(task);
				atwServer.start();
			} else if (serverName.equalsIgnoreCase("ver")) {
                System.out.println("Replacing VER server ...");
                verServer.interrupt();
				Runnable task = () -> {
					try {
						String[] args = new String[1];
						args[0] = "VER";
						Server.main(args);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
				verServer = new Thread(task);
				verServer.start();
			} else if (serverName.equalsIgnoreCase("out")) {
                System.out.println("Replacing out server ...");
                outServer.interrupt();
				Runnable task = () -> {
					try {
						String[] args = new String[1];
						args[0] = "OUT";
						Server.main(args);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
				outServer = new Thread(task);
				outServer.start();
			}
			recoverServerData(serverName);
            System.out.println("Replacing done");

		}
	}

	private static void recoverServerData(String serverName) {
		System.out.println("Recovering data for server "+serverName);
		for(String h:history){
			String targetServer = h.split(";")[1];
			if(targetServer.equalsIgnoreCase(serverName)){
				sendMessage(targetServer,h);
			}
		}
		recovering = false;
		System.out.println("Recover done");
	}
}