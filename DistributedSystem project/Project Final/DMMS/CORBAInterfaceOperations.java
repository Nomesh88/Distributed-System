package DMMS;


/**
* DMMS/CORBAInterfaceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from DMMS.idl
* Saturday, April 1, 2023 12:18:39 o'clock PM EDT
*/

public interface CORBAInterfaceOperations 
{
  String addMovieSlots (String adminID, String movieID, String movieName, String quantity);
  String removeMovieSlots (String adminID, String movieID, String quantity);
  String listMovieShowsAvailability (String adminID, String movieName);
  String bookMovieTickets (String customerID, String movieID, String movieName, String numberOfTickets);
  String cancelMovieTickets (String customerID, String movieID, String movieName, String numberOfTickets);
  String getBookingSchedule (String customerID);
  String exchangeTickets (String customerID, String newmovieID, String oldmovieID, String newmovieName, String numberOfTickets, String oldmovieName);
  void shutdown ();
} // interface CORBAInterfaceOperations
