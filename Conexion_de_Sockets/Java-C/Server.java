// SimpleServerLoop.java: a simple server program that runs forever in a single thead

import java.net.*;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;



public class Server {
  public static void main(String args[]) throws IOException {
    // Register service on port 1234
    ServerSocket s = new ServerSocket(1234);
    while(true)
    {
            Socket s1=s.accept(); // Wait and accept a connection
            // Get a communication stream associated with the socket

        //Recibe los datos
        InputStream s1In = s1.getInputStream();
        DataInputStream dis = new DataInputStream(s1In);

        byte[] buffer = new byte[255];
        dis.read(buffer,0,35);
        String st = new String (buffer);
        System.out.println(st);
        //Enviar datos
        OutputStream s1out = s1.getOutputStream();
        DataOutputStream dos = new DataOutputStream (s1out);
       
         int revisaChecksum = 0;
        
         //Se utiliza substring para dividir la cadena y tener mas control de la informacion
         String type = st.substring(0,1);

    
        if (type.equals("U")) {
            String checksum = st.substring(31,35);
            String datos = st.substring(0,31); //tomar toda la cadena menos el checksum 
        
            //Revisar el Checksum de lo que envio el cliente
            
             for (int i=0; i < datos.length(); i++){
                revisaChecksum = revisaChecksum+ datos.codePointAt(i); 
                }
             String cadena = Integer.toString(revisaChecksum); // pasar el checksum calculado a string
    

        if (cadena.equals(checksum)) {
                            
            String filepath="data.csv";
            String sensorName = st.substring(1,9);
            String data = st.substring(9,17);
            String time = st.substring(17,23);
            String date = st.substring(23,31);
          

            FileWriter fw = new FileWriter(filepath,true);
            BufferedWriter bw =new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(sensorName+","+data+","+time+","+date);
            pw.flush();
            pw.close();
             // Send a string!
            String text = "Registro completo";
            dos.write(text.getBytes("utf-8"),0,17); 
        } else {String text = "Error, el checksum no es igual"; dos.write(text.getBytes("utf-8"),0,30);};

      
      } else if (type.equals("R")){
            String checksum = st.substring(17,21);
            String datos = st.substring(0,17);
            //Revisar el Checksum de lo que envio el cliente
             for (int i=0; i < datos.length(); i++){
                revisaChecksum = revisaChecksum + datos.codePointAt(i); 
                }
               
            String cadena = Integer.toString(revisaChecksum); // pasar el checksum calculado a string

            if (cadena.equals(checksum)) {
            
            String filepath2="transaction.csv";
            String observerName = st.substring(1,9);
            String sensorName = st.substring(9,17);
        
            FileWriter fw2 = new FileWriter(filepath2,true);
            BufferedWriter bw2 =new BufferedWriter(fw2);
            PrintWriter pw2 = new PrintWriter(bw2);

            pw2.println(observerName+","+sensorName);

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

           
            String datosParaObsv;

                while(it.hasNext()){
                       datosParaObsv = it.next();
                    
                    if(datosParaObsv.contains(sensorName)) {
                        dos.write(datosParaObsv.getBytes("utf-8"),0,33); //envia los datos al Cliente
                        }
                }

               
            pw2.flush();
            pw2.close();
           
      }
  }else {String text = "Error, el checksum no es igual"; dos.write(text.getBytes("utf-8"),0,30);}

          
           
            // Close the connection, but not the server socket
            dos.close();
            s1out.close();
            s1.close();

            dis.close();
            s1In.close();
            s1.close();
    }
  }
}
