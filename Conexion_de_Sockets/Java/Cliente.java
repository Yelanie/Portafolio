// SimpleClient.java: a simple client program
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.text.ParseException;



public class Cliente {


  public static void main(String args[]) throws IOException {
    // Open your connection to a server, at port 1234
    Socket s1 = new Socket("localhost",1234);
    // Get an input file handle from the socket and read the input
    String type, nombreSensor, data, time,date, observerName, datosEnviar;

    //Menu de opcion
    Scanner scanner = new Scanner(System.in);
    System.out.println("1)UPDATE 2)REQUEST");
    int opcion = scanner.nextInt();
    scanner.nextLine();

    if (opcion == 1 || opcion == 2) {
        
    //UPDATE
    if (opcion == 1) {
        type = "U";
        
        System.out.println("Nombre del sensor: ");
        nombreSensor = scanner.nextLine();
        //Rellenar con espacion en blanco a la derecha si el nombre es menor a 8 bytes
        if(nombreSensor.length()<8)  nombreSensor = String.format("%-8s", nombreSensor);
        System.out.println("["+nombreSensor+"]");
        
        System.out.println("Data: ");
        data = scanner.nextLine();
        data = String.format("%-8s", data).replace(' ','0');  //rellenar con 0 a la derecha
        System.out.println("["+data+"]");
        
        System.out.println("Time HHMMSS: ");
        time = scanner.nextLine();

        System.out.println("Date DDMMAAAA: ");
        date = scanner.nextLine();

        datosEnviar = type + nombreSensor + data + time + date;

        int contador = 0; //esta variable contará la suma del valor ASCII de cada letra
          for (int i=0; i < datosEnviar.length(); i++){
            contador = contador + datosEnviar.codePointAt(i); 
           
            
        }
 
    
        datosEnviar = datosEnviar + contador;      

    }else {
        //REQUEST
        type = "R";
        System.out.println("Nombre del observer: ");
        observerName = scanner.nextLine();
         //Rellenar con espacion en blanco a la derecha si el nombre es menor a 8 bytes
        if(observerName.length()<8)  observerName = String.format("%-8s", observerName);

        System.out.println("Nombre del sensor: ");
        nombreSensor = scanner.nextLine();
        if(nombreSensor.length()<8)  nombreSensor = String.format("%-8s", nombreSensor);

        datosEnviar = type + observerName + nombreSensor;


           int contador = 0; //esta variable contará la suma del valor ASCII de cada letra
            for (int i=0; i < datosEnviar.length(); i++){
                contador = contador + datosEnviar.codePointAt(i); 
        }
        
   
            datosEnviar = datosEnviar + contador;

    }

            //Enviar los datos al servidor
            OutputStream s1out = s1.getOutputStream();
            DataOutputStream enviar = new DataOutputStream (s1out);
            enviar.writeUTF(datosEnviar);
       
            //Recibir los datos del servidor
            InputStream s1In = s1.getInputStream();
            DataInputStream dis = new DataInputStream(s1In);
            String st = new String (dis.readUTF());
            System.out.println(st);
        
    // When done, just close the connection and exit
    dis.close();
    s1In.close();
    s1.close();

    enviar.close();
    s1out.close();
    s1.close();

}// primer if 
else System.out.println("Error, opcion no valida");


   
  }
}
