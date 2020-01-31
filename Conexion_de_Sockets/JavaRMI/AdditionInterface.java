import java.rmi.*;
 
   public interface AdditionInterface extends Remote {
	   public String update(String type, String nombreSensor, String data, String time, String date) throws RemoteException;
	   public String request(String type, String observerName, String nombreSensor) throws RemoteException;
   }