package DMMS;


/**
* DMMS/_CORBAInterfaceStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from DMMS.idl
* Saturday, April 1, 2023 12:18:39 o'clock PM EDT
*/

public class _CORBAInterfaceStub extends org.omg.CORBA.portable.ObjectImpl implements DMMS.CORBAInterface
{

  public String addMovieSlots (String adminID, String movieID, String movieName, String quantity)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("addMovieSlots", true);
                $out.write_string (adminID);
                $out.write_string (movieID);
                $out.write_string (movieName);
                $out.write_string (quantity);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return addMovieSlots (adminID, movieID, movieName, quantity        );
            } finally {
                _releaseReply ($in);
            }
  } // addMovieSlots

  public String removeMovieSlots (String adminID, String movieID, String quantity)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("removeMovieSlots", true);
                $out.write_string (adminID);
                $out.write_string (movieID);
                $out.write_string (quantity);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return removeMovieSlots (adminID, movieID, quantity        );
            } finally {
                _releaseReply ($in);
            }
  } // removeMovieSlots

  public String listMovieShowsAvailability (String adminID, String movieName)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("listMovieShowsAvailability", true);
                $out.write_string (adminID);
                $out.write_string (movieName);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return listMovieShowsAvailability (adminID, movieName        );
            } finally {
                _releaseReply ($in);
            }
  } // listMovieShowsAvailability

  public String bookMovieTickets (String customerID, String movieID, String movieName, String numberOfTickets)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("bookMovieTickets", true);
                $out.write_string (customerID);
                $out.write_string (movieID);
                $out.write_string (movieName);
                $out.write_string (numberOfTickets);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return bookMovieTickets (customerID, movieID, movieName, numberOfTickets        );
            } finally {
                _releaseReply ($in);
            }
  } // bookMovieTickets

  public String cancelMovieTickets (String customerID, String movieID, String movieName, String numberOfTickets)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("cancelMovieTickets", true);
                $out.write_string (customerID);
                $out.write_string (movieID);
                $out.write_string (movieName);
                $out.write_string (numberOfTickets);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return cancelMovieTickets (customerID, movieID, movieName, numberOfTickets        );
            } finally {
                _releaseReply ($in);
            }
  } // cancelMovieTickets

  public String getBookingSchedule (String customerID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getBookingSchedule", true);
                $out.write_string (customerID);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getBookingSchedule (customerID        );
            } finally {
                _releaseReply ($in);
            }
  } // getBookingSchedule

  public String exchangeTickets (String customerID, String newmovieID, String oldmovieID, String newmovieName, String numberOfTickets, String oldmovieName)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("exchangeTickets", true);
                $out.write_string (customerID);
                $out.write_string (newmovieID);
                $out.write_string (oldmovieID);
                $out.write_string (newmovieName);
                $out.write_string (numberOfTickets);
                $out.write_string (oldmovieName);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return exchangeTickets (customerID, newmovieID, oldmovieID, newmovieName, numberOfTickets, oldmovieName        );
            } finally {
                _releaseReply ($in);
            }
  } // exchangeTickets

  public void shutdown ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("shutdown", false);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                shutdown (        );
            } finally {
                _releaseReply ($in);
            }
  } // shutdown

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:DMMS/CORBAInterface:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _CORBAInterfaceStub