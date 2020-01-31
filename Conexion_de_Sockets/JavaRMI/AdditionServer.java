    import java.rmi.*;
    import java.rmi.server.*;   
     
    public class AdditionServer {
    	   public static void main (String[] argv) {
    		   try {

                    System.setProperty("java.security.policy","file:./security.policy");

                    System.setProperty("java.rmi.server.hostname","192.168.1.111");

    			   System.setSecurityManager(new RMISecurityManager());
     
    			   Addition Hello = new Addition();			   		   
    			   Naming.rebind("rmi://localhost/ABC", Hello);
     
    			   System.out.println("Addition Server is ready.");
    			   }catch (Exception e) {
    				   System.out.println("Addition Server failed: " + e);
    				}
    		   }
    }