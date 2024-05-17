package Client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import DMMS.CORBAInterface;
import DMMS.CORBAInterfaceHelper;

public class Client {
	public static void main(String args[])
	{
		
		try {
			ORB orb = ORB.init(args, null);
			// -ORBInitialPort 1050 -ORBInitialHost localhost
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			startSystem(ncRef);
		} catch (Exception e) {
			System.out.println("Hello Client exception: " + e);
			e.printStackTrace();
		}
		
	}
	// Initiating client site program 
	private static void startSystem(NamingContextExt ncRef) {
		System.out.println("Enter your username: ");
		Scanner scanner = new Scanner(System.in);
		String username = scanner.nextLine().toUpperCase();
		System.out.println("You are loging as " + username);
		if(username.length()!=8) {
			System.out.println("Wrong ID");
			startSystem(ncRef);
		}
		if(!username.substring(3,4).equals("C") && !username.substring(3,4).equals("A")){
			System.out.println("Invalid ID");
			startSystem(ncRef);
		}
		String accessParameter = username.substring(3, Math.min(username.length(), 4));
		System.out.println("You are loging as " + accessParameter);
		if(accessParameter.equals("C") || accessParameter.equals("c") ) {
			try {
				user(username,ncRef);
				startSystem(ncRef);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(accessParameter.equals("A") || accessParameter.equals("a")) {
			try {
				manager(username,ncRef);
//				startSystem();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("This user is not authorized");
			startSystem(ncRef);
		}
	}
	
	private static void user(String username,NamingContextExt ncRef) throws Exception
	{
		String serverPort = decideServerport(username);
		if(serverPort.equals("1")) {
			startSystem(ncRef);
		}
		CORBAInterface obj = (CORBAInterface) CORBAInterfaceHelper.narrow(ncRef.resolve_str(serverPort));
		System.out.println(" ");
		System.out.println("Select the option you want to do: ");
		System.out.println("1. Book Movie \n2.Booking Schedule \n3. Return movie \n4. Exchange \n5. Logout");
		Scanner scanner = new Scanner(System.in);
		String menuSelection = scanner.nextLine();

		if(menuSelection.equals("1")) {
			System.out.println("Enter The MovieID");
        Scanner input1 = new Scanner(System.in);
        String movieID = input1.nextLine();
        System.out.println("Enter number of tickets");
        Scanner input2 = new Scanner(System.in);
        int x = input2.nextInt();
        System.out.println("Enter name of Movie");
        System.out.println("1 : Avatar");
        System.out.println("2 : Avengers");
        System.out.println("3 : Titanic");
        Scanner input3 = new Scanner(System.in);
        String y = input3.nextLine();
        String tickets = String.valueOf(x);
			String n = obj.bookMovieTickets(username, movieID, y, tickets);
			System.out.println("Movie Booked : " + n);
			
			user(username,ncRef);
		}
		else if(menuSelection.equals("2")) {
			System.out.println("Movie List is given below. ");
			System.out.println(obj.getBookingSchedule(username));
			String exit = scanner.nextLine();
			
				user(username,ncRef);
			
		}
		else if(menuSelection.equals("3")) {
			System.out.println("Enter MovieID To Be Returned:");
        Scanner input4 = new Scanner(System.in);
        String returnMovieID = input4.nextLine();
        System.out.println("Enter name of the movie:");
        System.out.println("1 : Avatar");
        System.out.println("2 : Avengers");
        System.out.println("3 : Titanic");
        Scanner input6 = new Scanner(System.in);
        String returnMovieName = input6.nextLine();
        System.out.println("Enter numberof tickets:");
        Scanner input5 = new Scanner(System.in);
        String numberOfTickets = input5.nextLine();
			String n = obj.cancelMovieTickets(username, returnMovieID, returnMovieName,
			numberOfTickets);
			System.out.println("Movie Returend : " + n);
			user(username,ncRef);
		}
		else if(menuSelection.equals("4")) {

			System.out.println("Enter oldMovieID:");
        Scanner input = new Scanner(System.in);
        String oldMovieID = input.nextLine();
        System.out.println("Enter oldMovieName:");
        System.out.println("1 : Avatar");
        System.out.println("2 : Avengers");
        System.out.println("3 : Titanic");
        String oldmovieName = input.nextLine();
        System.out.println("Enter newMovieID:");
		
        String newMovieID = input.nextLine();
        System.out.println("Enter newmovieName:");
        System.out.println("1 : Avatar");
        System.out.println("2 : Avengers");
        System.out.println("3 : Titanic");
        String newmovieName = input.nextLine();
        System.out.println("Enter number of TIckets:");
        String numberOfTickets = input.nextLine();
			String n = obj.exchangeTickets(username, newMovieID, oldMovieID,newmovieName,numberOfTickets,oldmovieName);
			System.out.println("Movie Exchanged : " + n);
			user(username,ncRef);
		}
		else if (menuSelection.equals("5")) {
			startSystem(ncRef);
		}
		else {
			user(username,ncRef);
		}
	}
	
	private static void manager(String username,NamingContextExt ncRef) throws Exception
	{
		String serverPort = decideServerport(username);
		if(serverPort.equals("1")) {
			startSystem(ncRef);
		}
		CORBAInterface obj = (CORBAInterface) CORBAInterfaceHelper.narrow(ncRef.resolve_str(serverPort));

		System.out.println(" ");
		System.out.println("Select the option you want to do: ");
		System.out.println("1. Add Movies \n2. Remove Movie \n3. List of the movies \n4. Logout");
		Scanner scanner = new Scanner(System.in);
		String menuSelection = scanner.nextLine();
		if(menuSelection.equals("1")) {
			Scanner input1 = new Scanner(System.in);
			System.out.println("Enter Movieid");
                        String movieID = input1.nextLine();
						ArrayList<String> list = new ArrayList<String>();
        list.add("190423");
        list.add("130423");
        list.add("140423");
        list.add("150423");
        list.add("160423");
        list.add("170423");
        list.add("180423");
						String date = movieID.substring(4, 10);
                        if(!list.contains(date))
                        {
                            System.out.println("Error in the date");
                            System.exit(0);
                        }
						System.out.println("Enter MovieName");
                        System.out.println("1 : Avatar");
                        System.out.println("2 : Avengers");
                        System.out.println("3 : Titanic");
                        Scanner input2 = new Scanner(System.in);
                        String movieName = input2.nextLine();

                        System.out.println("Enter MovieQuantity");
                        Scanner input3 = new Scanner(System.in);
                        String quantity = input3.nextLine();
			String n = obj.addMovieSlots(username, movieID, movieName, quantity);
			System.out.println(n);
			manager(username,ncRef);
		}
		else if(menuSelection.equals("2")) {
			System.out.println("Enter Remove MovieID");
                        Scanner input4 = new Scanner(System.in);
                        String removeMovieID = input4.nextLine();
						ArrayList<String> list = new ArrayList<String>();
        list.add("190423");
        list.add("130423");
        list.add("140423");
        list.add("150423");
        list.add("160423");
        list.add("170423");
        list.add("180423");
						String date = removeMovieID.substring(4, 10);
                        if(!list.contains(date))
                        {
                            System.out.println("Error in the date");
                            System.exit(0);
                        }
						System.out.println("Enter MovieName");
                        System.out.println("1 : Avatar");
                        System.out.println("2 : Avengers");
                        System.out.println("3 : Titanic");
                        Scanner input5 = new Scanner(System.in);
                        String moviename = input5.nextLine();
			String n = obj.removeMovieSlots(username, removeMovieID, moviename);
			System.out.println(n);
			manager(username,ncRef);
		}
		else if(menuSelection.equals("3")) {
			System.out.println("Enter Movie Name");
                        System.out.println("1 : Avatar");
                        System.out.println("2 : Avengers");
                        System.out.println("3 : Titanic");
                        Scanner input9 = new Scanner(System.in);
                        String movieNames = input9.nextLine();
			System.out.println("Movie List is given below. ");
			System.out.println(obj.listMovieShowsAvailability(username, movieNames));
			manager(username,ncRef);
		}
		else if (menuSelection.equals("4")) {
			startSystem(ncRef);
		}
		else {
			manager(username,ncRef);
		}

	}
	
	private static String decideServerport(String username) {
		String serverPort="frontend";
		
		return serverPort;
	}
}
