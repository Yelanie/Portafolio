#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 

using namespace std;

void error(const char *msg)
{
    perror(msg);
    exit(0);
}

int main(int argc, char *argv[])
{
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    char buffer[256];
    if (argc < 3) {
       fprintf(stderr,"usage %s hostname port\n", argv[0]);
       exit(0);
    }
    portno = atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, 
         (char *)&serv_addr.sin_addr.s_addr,
         server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
        error("ERROR connecting");
    
    int opcion;
    string type, nombreSensor, data, time,date, observerName, datosEnviar,checksum;
    int cont;
    cout << "1)UPDATE 2)REQUEST: ";
    cin >> opcion;

     if (opcion == 1)
    {
        type = "U";

        cout << "Nombre del sensor: ";
        cin >>nombreSensor;
        if (nombreSensor.length() < 8 )
        {
            
        nombreSensor.insert(nombreSensor.end(), 8 - nombreSensor.size(), ' '); //rellena con espacio
        }

        cout << "Datos: ";
        cin >>data;
         if (data.length() < 8 )
        {
             data.insert(data.begin(), 8 - data.size(), '0'); //rellena con 0 
            
        }
        

        cout << "Time HHMMSS: " ;
        cin >>time;
       

        cout << "Date DDMMAAAA: ";
        cin >>date;

        datosEnviar = type + nombreSensor + data + time + date;
        

             for (int i = 0; i<datosEnviar.length(); ++i)
        {
            cont= cont+datosEnviar[i];
            

        }
         checksum = to_string(cont);

        datosEnviar = datosEnviar + checksum;
        
        
    }

    else if (opcion == 2)
    {
        type = "R";

        cout << "Nombre observador: ";
        cin >> observerName;
           if (observerName.length() < 8 )
        {
             observerName.insert(observerName.end(), 8 - observerName.size(), ' '); //rellena con espacios a la derecha
            
        }
        
    
       

        cout << "Nombre del sensor: ";
        cin >> nombreSensor;
           if (data.length() < 8 )
        {
             nombreSensor.insert(nombreSensor.end(), 8 - nombreSensor.size(), ' '); //rellena con espacios a la derecha
            
        }
        
        datosEnviar = type + observerName + nombreSensor;

           for (int i = 0; i<datosEnviar.length(); ++i)
        {
            cont = cont+datosEnviar[i];
            

        }

        checksum = to_string(cont);

        datosEnviar = datosEnviar + checksum;


       
    } else {error("opcion no valida");  close(sockfd);}

   
   
     bzero(buffer,256);
     strcpy(buffer,datosEnviar.c_str());

     n = write(sockfd,buffer,strlen(buffer));
    if (n < 0) 
         error("ERROR writing to socket");
    bzero(buffer,256);
    n = read(sockfd,buffer,255);
    if (n < 0) 
         error("ERROR reading from socket");
    printf("%s\n",buffer);
    close(sockfd);
 
    return 0;
}

