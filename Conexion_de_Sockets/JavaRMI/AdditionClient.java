    import java.rmi.*;
    import java.util.Scanner;
     
    public class AdditionClient {
    	public static void main (String[] args) {
    		AdditionInterface hello;
    		try {
                    System.setProperty("java.security.policy","file:./security.policy");
                    System.setProperty("java.rmi.server.hostname","192.168.1.111");
                    
                    String type, nombreSensor, data, time,date,observerName;

                     System.setSecurityManager(new RMISecurityManager());
                     hello = (AdditionInterface)Naming.lookup("rmi://localhost/ABC");

                    //Menu de opcion
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("1)UPDATE 2)REQUEST");
                    int opcion = scanner.nextInt();
                    scanner.nextLine();

                    if (opcion == 1) {
                         type = "U";
                        System.out.println("Nombre del sensor: ");
                        nombreSensor = scanner.nextLine();
                        //Rellenar con espacion en blanco a la derecha si el nombre es menor a 8 bytes
                        if(nombreSensor.length()<8)  nombreSensor = String.format("%-8s", nombreSensor);
                      
        
                        System.out.println("Data: ");
                        data = scanner.nextLine();
                        data = String.format("%-8s", data).replace(' ','0');  //rellenar con 0 a la derecha
                        
        
                        System.out.println("Time HHMMSS: ");
                        time = scanner.nextLine();

                        System.out.println("Date DDMMAAAA: ");
                        date = scanner.nextLine();
                        

                    
    			     String mensaje = hello.update(type, nombreSensor,  data, time, date);
    			     System.out.println(mensaje);
                 }

                 if (opcion == 2) {
                        type = "R";
                        System.out.println("Nombre del observer: ");
                        observerName = scanner.nextLine();
                        //Rellenar con espacion en blanco a la derecha si el nombre es menor a 8 bytes
                        if(observerName.length()<8)  observerName = String.format("%-8s", observerName);

                        System.out.println("Nombre del sensor: ");
                        nombreSensor = scanner.nextLine();
                        if(nombreSensor.length()<8)  nombreSensor = String.format("%-8s", nombreSensor);

                         String mensaje = hello.request(type,observerName,nombreSensor);
                         System.out.println(mensaje);
                     
                 } 
     
    			     }catch (Exception e) {
    				    System.out.println("HelloClient exception: " + e);
    				}
    		}
    }