import java.rmi.*;
import java.rmi.server.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;

     
    public class Addition extends UnicastRemoteObject
             implements AdditionInterface {
     
          public Addition () throws RemoteException {   }
     
          public String update(String type, String nombreSensor, String data, String time, String date) throws RemoteException {
             String mensaje = "Registro completo";
            try{
             String filepath="data.csv";
             FileWriter fw = new FileWriter(filepath,true);
            BufferedWriter bw =new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(nombreSensor+","+data+","+time+","+date);
            pw.flush();
            pw.close();
           
            
           } catch(IOException e){e.printStackTrace();} 

        	  
        	  return mensaje;
          }

           public String request(String type,String observerName, String nombreSensor) throws RemoteException {

              String enviar = "";
            try{
            String filepath2="transaction.csv";
            FileWriter fw2 = new FileWriter(filepath2,true);
            BufferedWriter bw2 =new BufferedWriter(fw2);
            PrintWriter pw2 = new PrintWriter(bw2);

            pw2.println(observerName+","+nombreSensor);

            //enviar los datos del sensor al cliente
            FileReader fr2 = new FileReader("data.csv");
            BufferedReader bf2= new BufferedReader(fr2);

            //se guardan los datos en una lista para poder recorrer desde el ultimo registro al primero
            LinkedList<String> list = new LinkedList<String>();
            String sCadena;
            while ((sCadena = bf2.readLine())!=null) {
                list.add(sCadena);                
            }


            Iterator<String> it = list.descendingIterator();

           
            String leer;
            boolean c = true;


                while(it.hasNext() && c == true){
                       leer = it.next();
                    
                    if(leer.contains(nombreSensor)) {
                         enviar = leer;
                         c = false;
                        }
                }
              

               
               
            pw2.flush();
            pw2.close();

          }catch(IOException e){e.printStackTrace();} 

            return enviar;
          }
     }