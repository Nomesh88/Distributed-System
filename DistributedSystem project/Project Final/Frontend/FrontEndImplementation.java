package Frontend;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Utils.CONSTANTS;
import Utils.PortsAndIPs;
import org.omg.CORBA.ORB;

import DMMS.CORBAInterfacePOA;

import static java.lang.Thread.getAllStackTraces;
import static java.lang.Thread.sleep;

public class FrontEndImplementation extends CORBAInterfacePOA {
    private ORB orb;
    private static HashMap<String, Integer> incorrectRM = new HashMap<>();
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    private void receiveFromAllRM(String request, ArrayList<String> result) {
        Runnable task = () -> {
            //System.out.println("receiveFromAllRM");
            sendMessageAndReceive(request, result);
        };
        Thread thread = new Thread(task);
        thread.start();

    }

    private void sendMessageAndReceive(String dataFromClient, ArrayList<String> result) {
        ArrayList<String> messages = new ArrayList<>();
        HashMap<String, Boolean> rmCheck = new HashMap<>();
        HashMap<String, Boolean> serverCheck = new HashMap<>();
        serverCheck.put("ATW", false);
        serverCheck.put("VER", false);
        serverCheck.put("OUT", false);
        rmCheck.put("1",false);
        rmCheck.put("2",false);
        rmCheck.put("3",false);
        rmCheck.put("4",false);

        String methodName = dataFromClient.split(";")[0];
        System.out.println(methodName);

        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] message = dataFromClient.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(message, message.length, aHost, PortsAndIPs.Sequencer_PortNum);
            aSocket.send(request);

            //set time out
            aSocket.setSoTimeout(CONSTANTS.FE_TIME_OUT);

            byte[] buffer = new byte[1000];

            while (messages.size() < CONSTANTS.TOTAL_REPLICA-1) {
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                String mess = new String(reply.getData(), 0, reply.getLength());
                synchronized (this) {

                    messages.add(mess);
                }
                System.out.println("reply : " +mess);
                String[] parts = mess.split(";");
                String rmNum = parts[1];
                String serverName = parts[2];
                serverCheck.put(serverName, true);
                rmCheck.put(rmNum,true);
                System.out.println("FE receive reply in method : "+methodName +" from  RM " +rmNum+" , server "+serverName );
            }

            //check reply nums
            if (messages.size() < CONSTANTS.TOTAL_REPLICA-1) {
                ArrayList<String> lostRMs = new ArrayList<>();
                for (Map.Entry<String, Boolean> entry : rmCheck.entrySet()) {
                    if (!entry.getValue()) {
                        for (Map.Entry<String, Boolean> entry1 : serverCheck.entrySet()) {
                            if (!entry1.getValue()) {
                                lostRMs.add("crash"+entry.getKey()+";"+entry1.getKey()+";"+methodName);
                                System.out.println("Lost Reply From RM "+entry.getKey()+" server "+entry1);

                            }
                        }
                    }
                }
                for(String info : lostRMs) {
                    informAllRMs(info);
                }
            } else {//receive all three packets
                chooseCorrectResult(messages, result);
            }

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

    private void chooseCorrectResult(ArrayList<String> messages, ArrayList<String> result) {
        assert (messages.size() == CONSTANTS.TOTAL_REPLICA) : "Wrong packets size: " + messages.size();
        String[] reply = new String[CONSTANTS.TOTAL_REPLICA];
        String[] rmNames = new String[CONSTANTS.TOTAL_REPLICA];
        String[] serverNames = new String[CONSTANTS.TOTAL_REPLICA];
        for (int i = 0; i < messages.size(); i++) {
            String[] parts = messages.get(i).split(";");
//            for(String s:parts){
//                System.out.println("s : "+s);
//            }
            reply[i] = parts[0];
            rmNames[i] = parts[1];
            serverNames[i] = parts[2];
        }
        findMajorityElement(reply, messages.size(), rmNames, serverNames, result);
    }

    private void findMajorityElement(String[] reply, int n, String[] rmNames, String[] serverNames, ArrayList<String> result) {
        String major = null;
        // check if A[i] is majority element or not
        for (int i = 0; i <= n / 2; i++) {
            int count = 1;
            for (int j = i + 1; j < n; j++) {
                if (reply[j].equals(reply[i])) {
                    count++;
                }
            }

            if (count > n / 2) {
                major = reply[i];
                //check diff result rm
                ArrayList<String> diffRMs = new ArrayList<>();
                if (count < n) {
                    for (int k = 0; k < reply.length-1; k++) {
                        if (!reply[k].equals(major)) {
                            diffRMs.add("fault"+";"+rmNames[k]+";"+serverNames[k]);
                            System.out.println("Different Results From RM :" + rmNames[k]+" server " + serverNames[k]);
//                            checkConsecutiveErrorRM(diffRMs);
                            for(String diff:diffRMs){
                                informAllRMs(diff);
                            }
                        }
                    }

                }
            }

        }
        System.out.println("major :" + major);
        result.add(major);

    }
//    private void checkConsecutiveErrorRM(ArrayList<String> rmNames) {
//        if (incorrectRM.isEmpty()) {
//            for (String s : rmNames)
//                incorrectRM.put(s, 1);
//        } else {
//            for (Map.Entry<String, Integer> entry : incorrectRM.entrySet()) {
//                String rmName = entry.getKey();
//                if (rmNames.contains(rmName)) {
//                    if (incorrectRM.get(rmName) >= 3) {
//                        informAllRMs();//
//                        incorrectRM.remove(rmName);
//                    } else {
//                        incorrectRM.put(rmName, incorrectRM.get(rmName) + 1);
//                    }
//                } else {
//                    incorrectRM.remove(rmName);
//                }
//            }
//            for (String newName : rmNames) {
//                if (!incorrectRM.containsKey(newName)) {
//                    incorrectRM.put(newName, 1);
//                }
//            }
//        }
//    }

    private void informAllRMs(String information) {
        DatagramSocket fSocket = null;
        try {
            fSocket = new DatagramSocket();
            byte[] message = information.getBytes();
            InetAddress aHost = InetAddress.getByName(PortsAndIPs.RM_Feedback_IPAddress);
            DatagramPacket feedback = new DatagramPacket(message,message.length,aHost,PortsAndIPs.RM_Feedback_Portnum);
            fSocket.send(feedback);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public String addMovieSlots(String managerId, String itemID, String itemName, String quantity) {
        String request = "addMovieSlots" + ";" + managerId + ";" + itemName + ";" + itemID + ";" + null + ";" + quantity + ";" + null + ";";
        ArrayList<String> result = new ArrayList<>();
        receiveFromAllRM(request, result);
        while (result.size() == 0) {
            System.out.println("Waiting for result to return ...");
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result.get(0);
    }

    public String removeMovieSlots(String managerID, String itemID, String itemName) {
        String request = "removeMovieSlots" + ";" + managerID + ";" + itemName + ";" + itemID + ";" + null + ";" + null + ";"+ null + ";";
        ArrayList<String> result = new ArrayList<>();
        receiveFromAllRM(request, result);
        while (result.size() == 0) {
            System.out.println("Waiting for result to return ...");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result.get(0);
    }
    public String listMovieShowsAvailability(String managerID, String itemName) {
        String request = "listMovieShowsAvailability" + ";" + managerID + ";" + itemName + ";" + null + ";" + null + ";" + null + ";" + null + ";";
        ArrayList<String> result = new ArrayList<>();
        receiveFromAllRM(request, result);
        while (result.size() == 0) {
            System.out.println("Waiting for result to return ...");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result.get(0);

    }

    public String bookMovieTickets(String userID, String itemID, String itemName, String numberOfDay) {
        String request = "bookMovieTickets" + ";" + userID + ";" + itemName + ";" + itemID + ";" + null + ";" + numberOfDay + ";" + null + ";";
        ArrayList<String> result = new ArrayList<>();
        receiveFromAllRM(request, result);
        while (result.size() == 0) {
            System.out.println("Waiting for result to return ...");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return (result.get(0));
    }

    public String getBookingSchedule(String userID) {
        String request = "getBookingSchedule" + ";" + userID + ";" + null + ";" + null + ";" + null + ";" + null + ";"+ null + ";";
        ArrayList<String> result = new ArrayList<>();
        receiveFromAllRM(request, result);
        while (result.size() == 0) {
            System.out.println("Waiting for result to return ...");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result.get(0);
    }

    public String cancelMovieTickets(String userID, String itemID, String ItemName, String number) {
        String request = "cancelMovieTickets" + ";" + userID + ";" + ItemName + ";" + itemID + ";" + null + ";" + number + ";" + null + ";";
        ArrayList<String> result = new ArrayList<>();
        receiveFromAllRM(request, result);
        while (result.size() == 0) {
            System.out.println("Waiting for result to return ...");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return (result.get(0));
    }

  
    public String exchangeTickets(String CustomerID, String newMovieID, String oldMovieID, String newmovieName, String numberofTickets, String oldmovieName) {
        String request = "exchangeTickets" + ";" + CustomerID + ";" + oldmovieName + ";" + oldMovieID + ";" + newMovieID + ";" + numberofTickets + ";"+ newmovieName + ";";
        ArrayList<String> result = new ArrayList<>();
        receiveFromAllRM(request, result);
        while (result.size() == 0) {
            System.out.println("Waiting for result to return ...");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return (result.get(0));
    }


    // implement shutdown() method
    public void shutdown() {
        orb.shutdown(false);
    }
}