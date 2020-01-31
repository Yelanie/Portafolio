/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <fstream>
#include <list>

using namespace std;



void error(const char *msg)
{
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[])
{
     int sockfd, newsockfd, portno;
     socklen_t clilen;
     char buffer[256];
     struct sockaddr_in serv_addr, cli_addr;
     int n;

     if (argc < 2) {
         fprintf(stderr,"ERROR, no port provided\n");
         exit(1);
     }
      
    
     sockfd = socket(AF_INET, SOCK_STREAM, 0);
     if (sockfd < 0) 
        error("ERROR opening socket");
     bzero((char *) &serv_addr, sizeof(serv_addr));
     portno = atoi(argv[1]);
     serv_addr.sin_family = AF_INET;
     serv_addr.sin_addr.s_addr = INADDR_ANY;
     serv_addr.sin_port = htons(portno);
     if (bind(sockfd, (struct sockaddr *) &serv_addr,
              sizeof(serv_addr)) < 0) 
              error("ERROR on binding");

     listen(sockfd,5);

     clilen = sizeof(cli_addr);

  while ((newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen)) >= 0) {

     if (newsockfd < 0) 
          error("ERROR on accept");


     bzero(buffer,256);
     n = read(newsockfd,buffer,255);
     if (n < 0) error("ERROR reading from socket"); 
      printf("Here is the message: %s\n",buffer);
      string bufferStr = string(buffer);

       
        string type; 
        type = bufferStr.substr (0,1);
        int cont = 0;
       

        if (type.compare("U") == 0)
        {
            string tramaChecarChecksum;
            string checksum;

             tramaChecarChecksum = bufferStr.substr (0,31);
             

            checksum = bufferStr.substr (31,4);
            
             
             for (int i = 0; i<tramaChecarChecksum.length(); ++i)
                 {
                    cont= cont+tramaChecarChecksum[i];
                    

                 }

            string calcCheck;
            calcCheck= to_string(cont);

        if (checksum.compare(calcCheck)==0)
         {
   
            string sensorName,data,time, date;
            sensorName = bufferStr.substr(1,8);
            
            data = bufferStr.substr(9,8);
             
            time = bufferStr.substr(17,6);
             
            date = bufferStr.substr(23,8);
             

             ofstream archivo;  // objeto de la clase ofstream

            archivo.open("datos.csv",ios::app);

            archivo << sensorName << "," << data << "," << time << ","<< date << endl;
            

            archivo.close();

             n = write(newsockfd,"Registro completo",17);
                if (n < 0) error("ERROR writing to socket");


            

         } else printf("El checksum no es igual\n" );
        }

        
     else if (type.compare("R") == 0)
         {
            string tramaChecarChecksum;
            string checksum;

             tramaChecarChecksum = bufferStr.substr (0,17);
             

            checksum = bufferStr.substr (17,4);
           
        
             
             for (int i = 0; i<tramaChecarChecksum.length(); ++i)
                 {
                    cont= cont+tramaChecarChecksum[i];
                 }

            string calcCheck;
            calcCheck= to_string(cont);
      
           
     

        if (checksum.compare(calcCheck)==0)
        {

            string observerName,sensorName;
            observerName = bufferStr.substr(1,8);
            sensorName = bufferStr.substr(9,8);

            cout<<""<<endl;

            ofstream archivo;  // objeto de la clase ofstream

            archivo.open("transaction.csv",ios::app);

            archivo << observerName << "," << sensorName << endl;

            archivo.close();
               
            ifstream lectura;
            lectura.open("datos.csv");
            string str;
            list<string> lista;

            //Guardar los datos del archivo en una lista
            while (getline(lectura, str))
                {
        
                    if(str.size() > 0)
                     lista.push_front(str); //push front para que registro sea el primero
                }   

            for(string & line : lista){
                cout<<""<<endl;
            
                if(line.find(sensorName)!= string::npos) {
                    strcpy(buffer,line.c_str());
            
                    n = write(newsockfd,buffer,31);
                    if (n < 0) error("ERROR writing to socket");
                }


            }
 
            lectura.close();


           

         }else printf("El checksum no es igual" );
     }

         
     

 }
 
     close(newsockfd);
     close(sockfd);

    return 0; 
}
