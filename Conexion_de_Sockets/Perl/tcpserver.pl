#tcpserver.pl

use IO::Socket;

$| = 1;

$socket = new IO::Socket::INET (
                                  LocalHost => '127.0.0.1',
                                  LocalPort => '5000',
                                  Proto => 'tcp',
                                  Listen => 5,
                                  Reuse => 1
                               );
                                
die "Coudn't open socket" unless $socket;

print "\nTCPServer Waiting for client on port 5000";

while(1)
{
	$client_socket = "";
	$client_socket = $socket->accept();
	
	$peer_address = $client_socket->peerhost();
	$peer_port = $client_socket->peerport();
	
	print "\n I got a connection from ( $peer_address , $peer_port ) ";

	$client_socket->recv($buf,1024);
    print $buf,"\n";

    $contador = 0;

           

    $typeMsg = substr($buf,0,1);
    #print $typeMsg,"\n";
    
	
	if ($typeMsg eq 'U'){
	   $checksum = substr($buf, -4,4);
	   $datos = substr($buf,0,31);
	   #print $checksum,"\n";
	   #print $datos,"\n";

		$contador += ord $_ foreach split //, $datos;


		#print $contador,"\n";

		if ($checksum == $contador) {
			
			$sensorn = substr($buf,1,8);
			$data = substr($buf,9,8);
			$time = substr($buf,17,6);
			$date = substr($buf,23,8);
			open (fh, ">>", "data.csv"); 
  
			# Writing to the file 
			print  fh $sensorn,",",$data,",",$time,",",$date,"\n";
  
			# Closing the file 
			close(fh) or "Couldn't close the file";

			

			$client_socket->send("Datos registrados \n");
		}	
     } 


   elsif ($typeMsg eq 'R'){
		
		$checksum = substr($buf,-4,4);
		$datos = substr($buf,0,17);
		#print $checksum,"\n";
		#print $datos, "\n";

		


		$contador += ord $_ foreach split //, $datos;

		#print $contador,"\n";

		if ($checksum == $contador){
			$observern = substr($buf,1,8);
			$sensorn = substr($buf,9,8);
			#print $observern,"\n";
			#print $sensorn, "\n";


			open (fh2, ">>", "transactionLog.csv"); 
  
			# Writing to the file 
			print  fh2 $observern,",",$sensorn,"\n";
  
			# Closing the file 
			close(fh2) or "Couldn't close the file";

			open (fh, "<", "data.csv"); 
			
				@lines = reverse <fh>;
				foreach $line (@lines) {
					if(index($line, $sensorn ) != -1){

						$client_socket->send("Ultima medicion: $line");
						last

    				} 
    			 
			
  
			# Closing the file 
			close(fh) or "Couldn't close the file";
		}	     

    
	}
}

	 close $client_socket;
	
}
                                