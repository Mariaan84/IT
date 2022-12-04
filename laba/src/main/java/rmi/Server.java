package rmi;

import entity.DatabaseManagerImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class Server {
  public static void main(String[] args) throws NamingException, RemoteException {
      Context context = new InitialContext();
      context.bind("rmi://localhost:1099/database", new DatabaseManagerImpl());
  }
}
