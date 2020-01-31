#tcpclient.pl

use IO::Socket;

$socket = new IO::Socket::INET (
                                  PeerAddr  => '127.0.0.1',
                                  PeerPort  =>  5000,
                                  Proto => 'tcp',
                               )                
or die "Couldn't connect to Server\n";

                              
sub update{
    $typeMsg = 'U';

    print 'Nombre del sensor: ';
    
    $sensorn = <STDIN>;
    chop($sensorn);
    #Llenar con espacios 
    while (length($sensorn) < 8) {
            $sensorn = $sensorn . " ";
    }
    
    #valida que contenga un punto
    $flag = 1;
    while ($flag == 1){

        print 'Data float: ';
        $data = <STDIN>;
        chop($data);
        if (index($data, ".") != -1){
            $flag = 2;
            while (length($data) < 8){
                    $data = $data . " ";
            }
        }else {print "Ingrese un dato punto flotante \n";} 
        
    }  

    #valida un formato de 24h tomando rangos de la cadena
    $flag2 = 1;
    while ($flag2 == 1){
        print 'Time 24h clock HHMMSS: ';
        $time = <STDIN>;
        chop($time);
        if (substr($time,0,2) <24 and substr($time,2,2) <60 and substr($time,4,2) <60){
            
            $flag2 = 2;
        }
        else { print "Formato no valido \n";}

        
    }
    #valida el formato ddmmaaaa tomando rangos de la cadena
    $flag3 = 1 ;  
    while ($flag3 == 1){  

        print 'Date DDMMAAAA: ';          
        $date = <STDIN>;
        
        chop($date);
        
        if (substr($date,0,2) <31 and substr($date,2,2) <12 and substr($date,4,4) <9999){  
            $flag3 = 2;
            
        }

        else{ print "Formato no valido \n";}           
        
    }
    
    $enviar = $typeMsg . $sensorn . $data . $time . $date;
    $contador = 0;
    
    $contador += ord $_ foreach split //, $enviar;
    
    $enviar = $enviar . $contador;
    #print $enviar;

    $socket->send($enviar);
            
            

    $socket->recv($recv_data,1024);
    print $recv_data;
    close $socket;
 }

 sub request{
    $typeMsg = 'R';
    print 'Nombre del observer: ';
    $observern = <STDIN>;
    chop($observern);
    while (length($observern) < 8) {
            $observern = $observern . " ";
    }

    print 'Nombre del sensor: ';
    $sensorn = <STDIN>;
    chop($sensorn);
     while (length($sensorn) < 8) {
            $sensorn = $sensorn . " ";
    }
    
    $enviar = $typeMsg . $observern . $sensorn;

    $contador = 0;
    
    $contador += ord $_ foreach split //, $enviar;
    
    $enviar = $enviar . $contador;
    #print $enviar;

     $socket->send($enviar);
            
            

    $socket->recv($recv_data,1024);
    print $recv_data;
    close $socket;

}


 print "Selecciona opcion: 1)UPDATE 2)REQUEST \n";
 $opcion = <STDIN>;
 chop($opcion);

if ($opcion == 1){

    update();

}
    
elsif ($opcion == 2){
   
   request();

}

    
else {print "Opcion no valida \n";}


    
    
    
    
           
                                                          


    