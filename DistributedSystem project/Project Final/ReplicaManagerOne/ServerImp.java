package ReplicaManagerOne;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ServerImp {

    public static boolean fault = true;
    public static String RMNo = "1";
    private Lock lock = new ReentrantLock();

    class Admin {
        String adminID = " ";
    }

    class User {
        String customerID = " ";
        int borrowCount = 0;
    }

    class Item {
        String ID;
        String name;
        int num;

        @Override
        public String toString() {
            return "Item{" +
                    "itemName='" + name + '\'' +
                    ", itemQty='" + num + '\'' +
                    '}';
        }
    }

    //private HashMap<String, Movie> movies = new HashMap<>();
    // private HashMap<String, ArrayList<String> > waitList = new HashMap<>();
    private HashMap<String, ArrayList<String>> bookedMovies = new HashMap<>();

    HashMap<String, Integer> h1 = new HashMap<>();
    HashMap<String, Integer> h2 = new HashMap<>();
    HashMap<String, Integer> h3 = new HashMap<>();
    HashMap<String, HashMap<String, Integer>> h6 = new HashMap<>();
    HashMap<String, Integer> h7 = new HashMap<>();
    HashMap<String, HashMap<String, Integer>> h8 = new HashMap<>();

    private ArrayList<Admin> adminClients = new ArrayList<>();
    private ArrayList<User> userClients = new ArrayList<>();

    private String Theater = " ";
    private int portUdp = 0;


        

    public void StartServer(String theater, int portUdpFromSequencer) {
        Theater = theater;
        portUdp = portUdpFromSequencer;
        try {
            Log(Theater, getFormatDate() + " Server for " + Theater + " started");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if (Theater.equals("CON")) {
            Item i1 = new Item();
            i1.name = "Distributed System";
            i1.num = 10;
            i1.ID = "CON1010";
            Item i2 = new Item();
            i2.name = "Absoulate Java";
            i2.num = 11;
            i2.ID = "CON1011";
            Item i3 = new Item();
            i3.name = "Data Structure";
            i3.num = 5;
            i3.ID = "CON1012";
            items.put(i1.ID, i1);
            items.put(i2.ID, i2);
            items.put(i3.ID, i3);
        } else if (Theater.equals("MCG")) {
            Item i1 = new Item();
            i1.name = "Distributed System";
            i1.num = 20;
            i1.ID = "MCG1010";
            Item i2 = new Item();
            i2.name = "Absoulate Java";
            i2.num = 22;
            i2.ID = "MCG1011";
            Item i3 = new Item();
            i3.name = "Data Structure";
            i3.num = 10;
            i3.ID = "MCG1012";
            items.put(i1.ID, i1);
            items.put(i2.ID, i2);
            items.put(i3.ID, i3);
        } else if (Theater.equals("MON")) {
            Item i1 = new Item();
            i1.name = "Distributed System";
            i1.num = 20;
            i1.ID = "MON1010";
            Item i2 = new Item();
            i2.name = "Absoulate Java";
            i2.num = 22;
            i2.ID = "MON1011";
            Item i3 = new Item();
            i3.name = "Data Structure";
            i3.num = 10;
            i3.ID = "MON1012";
            items.put(i1.ID, i1);
            items.put(i2.ID, i2);
            items.put(i3.ID, i3);
        } else {
            Item i1 = new Item();
            i1.name = "a";
            i1.num = 1;
            i1.ID = Theater + "1111";
            Item i2 = new Item();
            i2.name = "b";
            i2.num = 2;
            i2.ID = Theater + "2222";
            items.put(i1.ID, i1);
            items.put(i2.ID, i2);
        }*/

        h1.put(Theater + "M" + "130423", 20);
        h1.put(Theater + "M" + "140423", 20);
        h2.put(Theater + "A" + "130423", 20);
        h2.put(Theater + "A" + "140423", 20);
        h3.put(Theater + "E" + "130423", 20);
        h3.put(Theater + "E" + "130423", 20);
        h3.put(Theater + "E" + "130423", 20);

        h6.put("1", h1);
        h6.put("2", h2);
        h6.put("3", h3);

        Runnable task2 = () -> {
            receiveFromSequencer();
        };
        Thread thread2 = new Thread(task2);
        thread2.start();
    }

    private static void Log(String serverID, String Message) throws Exception {
        final String dir = System.getProperty("user.dir");
        String path = "C:/Users/NOMESH/Desktop/Dsd lab/Project Final/" + serverID + "_Server.log";
        FileWriter fileWriter = new FileWriter(path, true);
        BufferedWriter bf = new BufferedWriter(fileWriter);
        bf.write(Message + "\n");
        bf.close();
    }

    private String getFormatDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    //    public boolean managerLogin(String managerID) {
    //
    //        Boolean exist = false;
    //
    //        for (Admin adminClient : adminClients) {
    //            if (adminClient.adminID.equals(managerID)) {
    //                exist = true;
    //                break;
    //            }
    //        }
    //
    //        if(!exist){
    //            Admin newAdmin = new Admin();
    //            newAdmin.adminID = managerID;
    //            adminClients.add(newAdmin);
    //        }
    //        System.out.println("ManagerClient [" + managerID + "] log in successfully");
    //        try {
    //            Log(Theater, getFormatDate() + " ManagerClient [" + managerID + "] log in successfully" );
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return true;
    //    }
    //
    //    public boolean userLogin(String studentID) {
    //
    //        Boolean exist = false;
    //
    //        for (User userClient : userClients) {
    //            if (userClient.userID.equals(studentID)) {
    //                exist = true;
    //                break;
    //            }
    //        }
    //        if(!exist){
    //            User newStudent = new User();
    //            newStudent.userID = studentID;
    //            newStudent.borrowCount = 0;
    //            userClients.add(newStudent);
    //        }
    //        System.out.println("UserClient " + studentID + " log in successfully");
    //        try {
    //            Log(Theater, getFormatDate() + " UserClient " + studentID + " log in successfully" );
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return true;
    //    }

    //manager operations

    public boolean adminLogin(String adminID) {

        Boolean exist = false;

        for (Admin adminClient : adminClients) {
            if (adminClient.adminID.equals(adminID)) {
                exist = true;
                break;
            }
        }

        if (!exist) {
            Admin newAdmin = new Admin();
            newAdmin.adminID = adminID;
            adminClients.add(newAdmin);
        }
        System.out.println("ManagerClient [" + adminID + "] log in successfully");
        try {
            Log(Theater, getFormatDate() + " ManagerClient [" + adminID + "] log in successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean customerLogin(String CustomerID) {

        Boolean exist = false;

        for (User userClient : userClients) {
            if (userClient.customerID.equals(CustomerID)) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            User newCustomer = new User();
            newCustomer.customerID = CustomerID;
            newCustomer.borrowCount = 0;
            userClients.add(newCustomer);
        }
        System.out.println("UserClient " + CustomerID + " log in successfully");
        try {
            Log(Theater, getFormatDate() + " UserClient " + CustomerID + " log in successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public String addMovieSlots(String adminID, String movieID, String movieName, String quantity) {
        int intQuantity = Integer.parseInt(quantity);
        int intmovie = Integer.parseInt(movieName);
        String result = "";
        String error = "";
        if (movieID.isEmpty() || movieName.isEmpty()) {
            return result;
        }

        synchronized (this) {
            if ((intQuantity >= 0) && (intmovie == 1 || intmovie == 2 || intmovie == 3)) {
                if (h6.get(movieName).containsKey(movieID)) {
                    int newcapacity;
                    if (intmovie == 1) {
                        // newcapacity = h6.get(movieName).get(movieID) + intQuantity;
                        newcapacity = intQuantity;
                        h1.put(movieID, newcapacity);
                        h6.put("1", h1);
                        result = "Manager [" + adminID + "] increase quantity of Movie [" + movieID + "] by [" + quantity
                                + "] success";
                    } else if (intmovie == 2) {
                        // newcapacity = h6.get(movieName).get(movieID) + intQuantity;
                        newcapacity = intQuantity;
                        h2.put(movieID, newcapacity);
                        h6.put("2", h2);
                        result = "Manager [" + adminID + "] increase quantity of Movie [" + movieID + "] by [" + quantity
                                + "] success";
                    } else if (intmovie == 3) {
                        // newcapacity = h6.get(movieName).get(movieID) + intQuantity;
                        newcapacity = intQuantity;
                        h3.put(movieID, newcapacity);
                        h6.put("3", h3);
                        result = "Manager [" + adminID + "] increase quantity of Movie [" + movieID + "] by [" + quantity
                                + "] success";
                    } else {
                        result = "";
                    }

                } else {
                    if (intmovie == 1) {

                        h1.put(movieID, intQuantity);
                        h6.put("1", h1);
                        result = "Manager [" + adminID + "] add [" + quantity + "] of Movie [" + movieID + "] success";

                    } else if (intmovie == 2) {
                        h2.put(movieID, intQuantity);
                        h6.put("2", h2);
                        result = "Manager [" + adminID + "] add [" + quantity + "] of Movie [" + movieID + "] success";

                    } else if (intmovie == 3) {
                        h3.put(movieID, intQuantity);
                        h6.put("3", h3);
                        result = "Manager [" + adminID + "] add [" + quantity + "] of Movie [" + movieID + "] success";

                    } else {
                        result = "";
                    }

                }
            } else {
                error = "Manager [" + adminID + "] add [" + quantity + "] of Movie [" + movieID
                        + "] failed : Qauntity must more than 0";
                result=error;
            }

            if (!result.isEmpty()) {
                System.out.println(result);
                try {
                    Log(Theater, getFormatDate() + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return result;
        }
    }


    public String removeMovieSlots(String adminID, String movieID, String movieName) {
        String result = "";
        String error = "";
        int intmovie = Integer.parseInt(movieName);
        synchronized (this) {
            if (intmovie == 1) {

                if (h1.containsKey(movieID)) {

                    String s = "";
                    s = s + "Cleared " + "Avatar";
                    h1.remove(movieID);
                    h6.put("1", h1);
                    result = " Manager [" + adminID + "] delete Movie [" + movieID + "] success. ";
                } else {
                    error = "Movie Not Found! ";
                    result=error;
                }

            } else if (intmovie == 2) {

                if (h2.containsKey(movieID)) {

                    String s = "";
                    s = s + "Cleared " + "Avengers";
                    h2.remove(movieID);
                    h6.put("2", h2);
                    result = " Manager [" + adminID + "] delete Movie [" + movieID + "] success. ";
                } else {
                    error = "Movie Not Found! ";
                }

            } else {

                if (h3.containsKey(movieID)) {

                    String s = "";
                    s = s + "Cleared " + "Titanic";
                    h3.remove(movieID);
                    h6.put("3", h3);
                    result = " Manager [" + adminID + "] delete Movie [" + movieID + "] success. ";
                } else {
                    error = "Movie Not Found! ";
                    result=error;
                }
            }

        }
        if (!result.isEmpty()) {
            try {
                Log(Theater, getFormatDate() + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String log = " Manager [" + adminID + "] remove ["
                    + movieName + "] of Movie [" + movieID + "] failed: ";
            log += error;
            try {
                Log(Theater, getFormatDate() + log);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public String listMovieShowsAvailability(String adminID, String movieName) {
        String result = "";
        result = findMovieLocal(movieName);
        String command = "listMovieShowsAvailability(" + movieName + ")";

        try {
            switch (Theater) {
                case "ATW": {
                    int serverport1 = 7777;
                    int serverport2 = 6666;
                    result = result + " " + UDPRequest.UDPlistMovie(command, serverport1);
                    result = result + " " + UDPRequest.UDPlistMovie(command, serverport2);
                    break;
                }
                case "VER": {
                    int serverport1 = 8888;
                    int serverport2 = 6666;
                    result = result + " " + UDPRequest.UDPlistMovie(command, serverport1);
                    result = result + " " + UDPRequest.UDPlistMovie(command, serverport2);
                    break;
                }
                default: {
                    int serverport1 = 8888;
                    int serverport2 = 7777;
                    result = result + " " + UDPRequest.UDPlistMovie(command, serverport1);
                    result = result + " " + UDPRequest.UDPlistMovie(command, serverport2);
                    break;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        if (result.isEmpty()) {
            String log = " Manager [" + adminID + "] list all of Movie failed";
            System.out.println(log);
            try {
                Log(Theater, getFormatDate() + log);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String log1 = " Manager [" + adminID + "] list all of Movie success. "
                    + "All Movies: " + result;
            System.out.println(log1);
            try {
                Log(Theater, getFormatDate() + log1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String findMovieLocal(String movieName) {
        String result = "";
        int intmovie = Integer.parseInt(movieName);
        synchronized (this) {
            if (intmovie == 1) {
                for (String movieID : h1.keySet()) {
                    String e;
                    e = movieID + " " + "Avatar" + " " + h1.get(movieID);
                    result = result + "  " + e;
                }
            } else if (intmovie == 2) {
                for (String movieID : h2.keySet()) {
                    String e;
                    e = movieID + " " + "Avengers" + " " + h2.get(movieID);
                    result = result + "  " + e;
                }

            } else {
                for (String movieID : h3.keySet()) {
                    String e;
                    e = movieID + " " + "Titanic" + " " + h3.get(movieID);
                    result = result + "  " + e;

                }

            }
            return result;
        }
    }

    // user operations

    public String bookMovieTickets(String customerID, String movieID, String movieName, String numberOfDays) {
        String result = "";
        String command = "bookMovieTickets(" + customerID + "," + movieID + "," + movieName + "," + numberOfDays + ")";
        String theaterName = movieID.substring(0, 3);
       // for (User userClient : userClients) {
            //result = "ASDFGH1";
            //if (userClient.customerID.equals(customerID)) {
               // result = "ASDFGH2";
                try {
                    if (theaterName.equals(Theater)) {
                        result = "ASDFGH3";
                        result = bookLocal(customerID, movieID, movieName, numberOfDays);
                    } else if (theaterName.equals("ATW")) {
                        int serverport = 8888;
                        result = UDPRequest.UDPbookMovieTickets(command, serverport);
                    } else if (theaterName.equals("VER")) {
                        int serverport = 7777;
                        result = UDPRequest.UDPbookMovieTickets(command, serverport);
                    } else if (theaterName.equals("OUT")) {
                        int serverport = 6666;
                        result = UDPRequest.UDPbookMovieTickets(command, serverport);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!theaterName.equals(Theater)) {
                    if (result.isEmpty()) {
                        String log = " Server borrow Movie [" + movieID + "] for user [" + customerID + "] from server ["
                                + theaterName + "] failed";
                        System.out.println(log);
                        try {
                            Log(Theater, getFormatDate() + log);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String log2 = " Server borrow Movie [" + movieID + "] for user [" + customerID
                                + "] from server [" + theaterName + "] success";
                        System.out.println(log2);
                        try {
                            Log(Theater, getFormatDate() + log2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

          //  }
       // }
        return result;
    }

    public String bookLocal(String customerID, String movieID, String movieName, String numberOfTickets) {
        int number = Integer.parseInt(numberOfTickets);
        int intmovie = Integer.parseInt(movieName);
        String result = "";
        String failReason = "";
        int flag = 0;
        int x =0;
        String userTheater = customerID.substring(0, 3);
        synchronized (this) {
             if (!userTheater.equals(Theater)) {
                //for (HashMap.Entry<String, ArrayList<String>> entry : bookedMovies.entrySet()) {
                    if (h8.containsKey(customerID)) {
                        flag = 1;
                        result = "User can only book 1 Movie from other theatres";
                    }
            // }
            

            } 
           
            
            if(flag==0) {  
                if (intmovie == 1) {

                    if (h1.containsKey(movieID)) {
                        if (h1.get(movieID) > number) {
                            
                             HashMap<String, Integer> h11 = new HashMap<String, Integer>();
                             if(!h8.containsKey(customerID))
                             {
                                h11.put(movieID,number);
                             }
                             else
                             {
                            h11 = h8.get(customerID);
                            h11.put(movieID, number);
                             }
                            h8.put(customerID, h11);
                            int newCapacity = h1.get(movieID) - number;
                            h1.put(movieID, newCapacity);
                            h6.put("1", h1);
                            result = customerID + " " + " " + movieID + " " + numberOfTickets;
                           // h11.clear();
                           // return result;
                        } else {
                            result ="Booked more than Capacity";
                        }
                    } else {
                        result ="Invalid movieID";
                    }
                } else if (intmovie == 2) {
                    if (h2.containsKey(movieID)) {
                        if (h2.get(movieID) > number) {
                             HashMap<String, Integer> h11 = new HashMap<String, Integer>();
                             if(!h8.containsKey(customerID))
                             {
                                h11.put(movieID,number);
                             }
                             else
                             {
                            h11 = h8.get(customerID);
                            h11.put(movieID, number);
                             }
                            h8.put(customerID, h11);
                            int newCapacity = h2.get(movieID) - number;
                            h2.put(movieID, newCapacity);
                            h6.put("2", h2);
                             result = customerID + " " + " " + movieID + " " + numberOfTickets;
                            // h11.clear();
                            //return s1;
                        } else
                        failReason ="Booked more than Capacity";
                        result=failReason;
                    } else
                    failReason ="Inavlid MovieID";
                     result=failReason;
                } else {
                    if (h3.containsKey(movieID)) {
                        if (h3.get(movieID) > number) {
                            HashMap<String, Integer> h11 = new HashMap<String, Integer>();
                            if(!h8.containsKey(customerID))
                             {
                                h11.put(movieID,number);
                             }
                             else
                             {
                            h11 = h8.get(customerID);
                            h11.put(movieID, number);
                             }
                            h8.put(customerID, h11);
                            int newCapacity = h3.get(movieID) - number;
                            h3.put(movieID, newCapacity);
                            h6.put("3", h3);
                            result = customerID + " " + " " + movieID + " " + numberOfTickets;
                           // h11.clear();
                           // return s1;
                        } else
                        failReason ="Booked more than Capacity";
                         result=failReason;
                    } else
                    failReason ="Invalid movieid";
                    result=failReason;
                }
            
            }
            
            if (result.isEmpty()) {
                String log = " User [" + customerID + "] book Movie [" + movieID + "] failed: ";
                System.out.println(log);
                try {
                    Log(Theater, getFormatDate() + log + failReason);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String log2 = " User [" + customerID + "] book Movie [" + movieID + "] success.";
                System.out.println(log2);
                try {
                    Log(Theater, getFormatDate() + log2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //h11.clear();
        return result;
    
    }


    public String cancelMovieTickets(String customerID, String movieID, String movieName,
            String numberOfDays) {
        String result = "";
        String command = "cancelMovieTickets(" + movieID + "," + customerID + "," + numberOfDays + "," + movieName
                + ")";
        int serverPort;
        String theaterName = movieID.substring(0, 3);
        try {
            if (theaterName.equals(Theater)) {
                result = cancelLocal(movieID, customerID, numberOfDays, movieName);

            } else if (theaterName.equals("ATW")) {
                serverPort = 8888;
                result = UDPRequest.UDPcancelMovieTickets(command, serverPort);

            } else if (theaterName.equals("VER")) {
                serverPort = 7777;
                result = UDPRequest.UDPcancelMovieTickets(command, serverPort);

            } else if (theaterName.equals("OUT")) {
                serverPort = 6666;
                result = UDPRequest.UDPcancelMovieTickets(command, serverPort);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!theaterName.equals(Theater)) {
            if (!result.isEmpty()) {
                String log = " Server return Movie [" + movieID + "] for user [" + customerID + "] to server ["
                        + theaterName + "] success";
                System.out.println(log);
                try {
                    Log(Theater, getFormatDate() + log);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                String log1 = " Server return Movie [" + movieID + "] for user [" + customerID + "] to server ["
                        + theaterName + "] failed";
                System.out.println(log1);
                try {
                    Log(Theater, getFormatDate() + log1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public String cancelLocal(String movieID, String customerID, String numberOfTickets, String movieName) {
        String result = "";
        int Tickets = Integer.parseInt(numberOfTickets);
        int intmovie = Integer.parseInt(movieName);
        synchronized (this) {
            if (intmovie == 1) {
                if (h8.containsKey(customerID)) {
                    HashMap<String,Integer> h11 = new HashMap<String,Integer>();
                    h11 = h8.get(customerID);
                    if (h11.get(movieID) > Tickets) {

                        int capacity = h11.get(movieID) - Tickets;
                        h11.put(movieID, capacity);
                        h8.put(customerID, h11);
                        int newCapacity = h1.get(movieID) + Tickets;
                        h1.put(movieID, newCapacity);
                        h6.put("1", h1);
                        result = customerID + " " + " " + movieID + " " + numberOfTickets;
                        return result;
                    } else {
                        int newCapacity = h1.get(movieID) + Tickets;
                        h1.put(movieID, newCapacity);
                        h6.put("1", h1);
                        h11.remove(movieID);
                        h8.put(customerID,h11);
                        String s1 = customerID + " " + " " + movieID + " " + numberOfTickets;
                        return s1;
                    }
                } else {
                    return ("");
                }
            } else if (intmovie == 2) {
                if (h8.containsKey(customerID)) {
                    HashMap<String,Integer> h11 = new HashMap<String,Integer>();
                    h11 = h8.get(customerID);
                    if (h11.get(movieID) > Tickets) {

                        int capacity = h11.get(movieID) - Tickets;
                        h11.put(movieID, capacity);
                        h8.put(customerID, h11);
                        int newCapacity = h2.get(movieID) + Tickets;
                        h2.put(movieID, newCapacity);
                        h6.put("2", h2);
                        String s1 = customerID + " " + " " + movieID + " " + numberOfTickets;
                        return s1;
                    } else {
                        int newCapacity = h2.get(movieID) + Tickets;
                        h2.put(movieID, newCapacity);
                        h6.put("2", h2);
                        h11.remove(movieID);
                        h8.put(customerID,h11);
                        String s1 = customerID + " " + " " + movieID + " " + numberOfTickets;
                        return s1;
                    }
                } else {
                    return ("");
                }
            } else  {
                if (h8.containsKey(customerID)) {
                    HashMap<String,Integer> h11 = new HashMap<String,Integer>();
                    h11 = h8.get(customerID);
                    if (h11.get(movieID) > Tickets) {

                        int capacity = h11.get(movieID) - Tickets;
                        h11.put(movieID, capacity);
                        h8.put(customerID, h11);
                        int newCapacity = h3.get(movieID) + Tickets;
                        h1.put(movieID, newCapacity);
                        h6.put("3", h3);
                        String s1 = customerID + " " + " " + movieID + " " + numberOfTickets;
                        return s1;
                    } else {
                        int newCapacity = h3.get(movieID) + Tickets;
                        h1.put(movieID, newCapacity);
                        h6.put("3", h3);
                        h11.remove(movieID);
                        h8.put(customerID,h11);
                        String s1 = customerID + " " + " " + movieID + " " + numberOfTickets;
                        return s1;
                    }
                } else {
                    return ("");
                }
            }
            

        }
    

    }

 
    public String getBookingSchedule(String customerID) {
        String result = "QWERTY";
       result = listBookingLocal(customerID);
        String theaterName = customerID.substring(0, 3);
       String command = "getBookingSchedule(" + customerID + ")";

        try {
            if (Theater.equals("ATW")) {
                int serverport1 = 7777;
                int serverport2 = 6666;
                result = result + UDPRequest.UDPgetBookingSchedule(command, serverport1);
                result = result + UDPRequest.UDPgetBookingSchedule(command, serverport2);
            } else if (Theater.equals("VER")) {
                int serverport1 = 8888;
                int serverport2 = 6666;
                result += UDPRequest.UDPgetBookingSchedule(command, serverport1);
                result += UDPRequest.UDPgetBookingSchedule(command, serverport2);
            } else if (Theater.equals("OUT")) {
                int serverport1 = 8888;
                int serverport2 = 7777;
                result += UDPRequest.UDPgetBookingSchedule(command, serverport1);
                result += UDPRequest.UDPgetBookingSchedule(command, serverport2);
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        } 

        return result;
    }

    public String listBookingLocal(String customerID) {
        String result = "";

        if (h8.containsKey(customerID)) {

            HashMap<String, Integer> h9 = new HashMap<>();

            h9 = h8.get(customerID);
            if (h9.size() != 0) {
                for (String movieID : h9.keySet()) {
                    String e;
                    e = movieID + " " + +h9.get(movieID);
                    result = result + "  " + e;
                }

            } else
                return ("No Booking");

        }
        else 
        System.out.println("No Booking");

        return result;
    }


    public String exchangeTickets(String CustomerID, String newMovieID, String oldMovieID, String newmovieName, String numberofTickets, String oldmovieName) {
        String result = "";
        String newTheater = newMovieID.substring(0, 3);
        String oldTheater = oldMovieID.substring(0, 3);
        String command = "exchangeTickets(" + CustomerID + "," + newMovieID + "," + oldMovieID + newmovieName + "," + numberofTickets + "," + oldmovieName + ")";
        int serverPort;

        if (newTheater.equals(oldTheater)) {
            try {
                if (newTheater.equals(Theater)) {
                    result = exchangeLocal(CustomerID, newMovieID, oldMovieID,  newmovieName, numberofTickets, oldmovieName);
                } else if (newTheater.equals("ATW")) {
                    serverPort = 8888;
                    result = UDPRequest.UDPexchangeTickets(command, serverPort);
                } else if (newTheater.equals("VER")) {
                    serverPort = 7777;
                    result = UDPRequest.UDPexchangeTickets(command, serverPort);
                } else if (newTheater.equals("OUT")) {
                    serverPort = 6666;
                    result = UDPRequest.UDPexchangeTickets(command, serverPort);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                lock.lock();
                String bookingResult = bookMovieTickets(CustomerID, newMovieID, newmovieName,
                        numberofTickets);
                if (!bookingResult.isEmpty()) {
                    cancelMovieTickets(CustomerID, oldMovieID, oldmovieName, numberofTickets);
                    result = bookingResult;
                }
            } finally {
                lock.unlock();
            }
        }
        return result;
    }

    public String exchangeLocal(String CustomerID, String newMovieID, String oldMovieID, String newmovieName, String numberofTickets, String oldmovieName) {
        String result = "";
        if (h8.containsKey(CustomerID) && h8.get(CustomerID).containsKey(oldMovieID)&&h8.get(CustomerID).get(oldMovieID)>=Integer.parseInt(numberofTickets)) {
            try {
                lock.lock();
                //bookedMovies.get(oldMovieID).remove(CustomerID);
                String bookingResult = bookLocal(CustomerID, newMovieID,newmovieName, numberofTickets);
                //bookedMovies.get(oldMovieID).add(CustomerID);

                if (!bookingResult.isEmpty()) {
                    cancelLocal(oldMovieID, CustomerID, numberofTickets, oldmovieName);
                    result = bookingResult;
                    String log = " User [" + CustomerID + "] exchange with Movie [" + oldMovieID + "] for Movie [" +
                            newMovieID + "] success.";
                    System.out.println(log);
                    try {
                        Log(Theater, getFormatDate() + log);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        } else {
            String log = " User [" + CustomerID + "] exchange Movie [" + oldMovieID + "] failed: ";
            String error = "Movie is not booked or is not booked by user [" + CustomerID + "]";
            result=error;
            try {
                Log(Theater, getFormatDate() + log + error);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    private void receiveFromSequencer() {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(portUdp);
            byte[] buffer = new byte[1000];
            System.out.println("Sequencer UDP Server " + portUdp + " Started............");
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                String sentence = new String(request.getData(), 0,
                        request.getLength());
                findNextMessage(sentence);

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

    public void findNextMessage(String sentence) {
        String message = sentence;
        String[] parts = message.split(";");
        String function = parts[0];
        System.out.println(function);
        String userID = parts[1];
        System.out.println(userID);
        String itemName = parts[2];
        String itemId = parts[3];
        String newItemId = parts[4];
        String number = parts[5];
        String newitemName = parts[6];
        System.out.println(message);
        String sendingResult = "";
        String result = "";
        if (function.equals("addMovieSlots")) {
            result = this.addMovieSlots(userID, itemId, itemName, number);
        } else if (function.equals("removeMovieSlots")) {
            result = this.removeMovieSlots(userID, itemId, itemName);
            //sendingResult = result;
        } else if (function.equals("listMovieShowsAvailability")) {
            result = this.listMovieShowsAvailability(userID, itemName);
            //sendingResult = result;
        } else if (function.equals("bookMovieTickets")) {
            result = this.bookMovieTickets(userID, itemId, itemName, number);
            //sendingResult = Boolean.toString(result);
        } else if (function.equals("getBookingSchedule")) {
            result = this.getBookingSchedule(userID);
        } else if (function.equals("cancelMovieTickets")) {
            result = this.cancelMovieTickets(userID, itemId, itemName, number);
            //sendingResult = Boolean.toString(result);
        } else if (function.equals("exchangeTickets")) {
            result = this.exchangeTickets(userID, newItemId, itemId, newitemName, number, itemName);
            //sendingResult = Boolean.toString(result);
        }

        result = result + ";" + RMNo + ";" + Theater;
        int FEPortNum = Integer.valueOf(parts[8]);
        String FEIpAddress = parts[9];

        sendMessageBackToFrontend(result, FEPortNum, FEIpAddress);
    }

    public void sendMessageBackToFrontend(String message,int FEPortNum,String FEIpAddress ) {
        System.out.println("Result : "+message);
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] m = message.getBytes();
            InetAddress aHost = InetAddress.getByName(FEIpAddress);

            DatagramPacket request = new DatagramPacket(m, m.length, aHost, FEPortNum);
            aSocket.send(request);
            aSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
