package DMMS;

/**
* DMMS/CORBAInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from DMMS.idl
* Saturday, April 1, 2023 12:18:39 o'clock PM EDT
*/

public final class CORBAInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public DMMS.CORBAInterface value = null;

  public CORBAInterfaceHolder ()
  {
  }

  public CORBAInterfaceHolder (DMMS.CORBAInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = DMMS.CORBAInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    DMMS.CORBAInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return DMMS.CORBAInterfaceHelper.type ();
  }

}
