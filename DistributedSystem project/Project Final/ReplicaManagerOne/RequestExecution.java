package ReplicaManagerOne;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RequestExecution extends Thread{

    public Thread t;
    public ServerImp server;
    public DatagramSocket serversocket;
    public InetAddress address;
    public int clientport;
    String message;

    public RequestExecution(ServerImp server, DatagramSocket serversocket, InetAddress address, int clientport, String message) {
        super();
        this.address = address;
        this.message = message;
        this.server = server;
        this.clientport = clientport;
        this.serversocket = serversocket;
    }

    public void start(){
        if(t == null){
            t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        String result = "";
        
        if (message.startsWith("bookMovieTickets")) {
            result = UDPbookMovieTickets(message);
        }else if    (message.startsWith("listMovieShowsAvailability")) {
            result = UDPlistMovie(message);
        } else if (message.startsWith("cancelMovieTickets")) {
            result = UDPcancelMovieTickets(message);
        } else if (message.startsWith("getBookingSchedule")) {
            result = UDPgetBookingSchedule(message);
        } else if (message.startsWith("exchangeTickets")) {
            result = UDPexchangeTickets(message);
        }
        byte[] buffer = result.getBytes();
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length, address, clientport);
        try {
            serversocket.send(reply);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String UDPbookMovieTickets(String message) {
        String arguments = message.substring(message.indexOf("(") + 1, message.length()-1);
        String arg[] = arguments.split(",");
        String userID = arg[0];
        String MovieID = arg[1];
        String movieName = arg[2];
        String numberOfDays = arg[3];
        String result = server.bookLocal(userID, MovieID,movieName, numberOfDays);
        return result;
    }

    public String UDPlistMovie(String message){
        String data = message.substring(message.indexOf("(") + 1,message.length() - 1);
        String result = server.findMovieLocal(data);
        return result;

    }

    
    public String UDPcancelMovieTickets(String message) {
        String arguments = message.substring(message.indexOf("(") + 1, message.length() - 1);
        String arg[] = arguments.split(",");
        String MovieID = arg[0];
        String userID = arg[1];
        String numberOfDays = arg[2];
        String movieName = arg[3];

        String result = server.cancelLocal(MovieID, userID,numberOfDays,movieName);
        return result;
    }

    private String UDPgetBookingSchedule(String message) {
        String customerID = message.substring(message.indexOf("(") + 1,message.length() - 1);
        String result = server.listBookingLocal(customerID);
        return result;
    }

    private String UDPexchangeTickets(String message) {
        String arguments = message.substring(message.indexOf("(") + 1, message.length() - 1);
        String arg[] = arguments.split(",");
        String customerID = arg[0];
        String newMovieID = arg[1];
        String oldMovieID = arg[2];
        String newmovieName = arg[3];
        String numberofTickets = arg[4];
        String oldmovieName = arg[4];
        String result = server.exchangeLocal(customerID, newMovieID, oldMovieID,newmovieName,numberofTickets,oldmovieName);
        return result;
    }
}
