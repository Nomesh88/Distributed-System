package ReplicaManagerTwo;



import Utils.PortsAndIPs;

import java.net.DatagramSocket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {

        int udpPortNum = 0;
        int multiCastPortNum=0;
        String campus;
        if(args.length == 0){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Server");
            campus = sc.nextLine().toUpperCase();
        }else{
            campus = args[0].toUpperCase();
        }
        ServerImp serverImp = new ServerImp();
        try{
            switch (campus) {
                case "ATW":
                    udpPortNum = 2234;
                    multiCastPortNum = PortsAndIPs.RM2_CON_PortNum;
                    break;
                case "VER":
                    udpPortNum = 2235;
                    multiCastPortNum = PortsAndIPs.RM2_MCG_PortNum;
                    break;
                case "OUT":
                    udpPortNum = 2236;
                    multiCastPortNum = PortsAndIPs.RM2_MON_PortNum;
                    break;
                default:
                    System.out.println("Server started failed");
                    System.exit(0);
            }

            System.out.println("DLMS ready and waiting ...");
            DatagramSocket serversocket = new DatagramSocket(udpPortNum);
            startListening(campus, serverImp, serversocket);
            serverImp.StartServer(campus,multiCastPortNum);

        }
        catch (Exception re) {
            System.out.println("Exception in Server.main: " + re);
        }
        
    }

    private static void startListening(String campusName, ServerImp campusSever, DatagramSocket SeverSocket) {

        String threadName = campusName + "listen";
        Listening listen = new Listening(threadName, SeverSocket, campusSever);
        listen.start();
    }
    

}




