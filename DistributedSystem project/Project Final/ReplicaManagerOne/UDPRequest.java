package ReplicaManagerOne;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPRequest {

    public UDPRequest() {
        super();
    }

    public static String request(String command,int serverport) throws SocketException{

        String replyMsg = "";
        DatagramSocket requestSocket = new DatagramSocket();
        try {

            byte []m = command.getBytes();
            InetAddress ahost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(m,m.length,ahost,serverport);
            requestSocket.send(request);

            byte []buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            requestSocket.receive(reply);

            byte []message = new byte[reply.getLength()];
            System.arraycopy(buffer, 0, message, 0, reply.getLength());
            replyMsg = new String(message);
        }
        catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

        finally {
            if(requestSocket != null)
                requestSocket.close();
        }

        return replyMsg;
    }

    public static String UDPlistMovie(String command,int serverPort) throws SocketException{
        String result;
        result = (request(command, serverPort));
        return result;
    }

    public static String UDPbookMovieTickets(String command,int serverPort) throws SocketException{
        String result;
        result = (request(command, serverPort));
        return result;
    }

    public static String UDPcancelMovieTickets(String command,int serverPort) throws SocketException{
        String result = "";
        result = request(command, serverPort);
        return result;

    }

    public static String UDPgetBookingSchedule(String command,int serverPort) throws SocketException{
        String result;
        result = (request(command, serverPort));
        return result;
    }

    public static String UDPexchangeTickets(String command,int serverPort) throws SocketException {
        String result = "";
        result = request(command,serverPort);
        return result;
    }

    public static String UDPexchangeItem(String command, int serverPort) throws SocketException {
        String result;
        result = (request(command,serverPort));
        return result;
    }
}
