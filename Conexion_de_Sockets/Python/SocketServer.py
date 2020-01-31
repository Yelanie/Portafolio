#!/usr/bin/python

import socket


serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serversocket.bind(('localhost', 8089)) 
serversocket.listen(5)		#become a server socket, maximum 5 connections

while True:
	connection, address = serversocket.accept()
	buf = connection.recv(64)
	if len(buf)> 0:
		print buf
	
	contador = 0

	typeMsg = buf[:1]
	
	if typeMsg == "U":
		checksum = buf[-4:]
		datos = buf[:-4]

		for n in datos:
			contador += ord(n)

		if checksum == str(contador):
			
			sensorn = buf[1:9]
			data = buf[9:17]
			time = buf[17:23]
			date = buf[23:31]

			archivo = open("data.csv","a")
			archivo.write(sensorn+","+data+","+time+","+date+"\n")

			archivo.close()

			connection.send("Sensor registrado")
	
	elif typeMsg == "R":
		
		checksum = buf[-4:]
		datos = buf[:-4]

		for n in datos: 
			contador += ord(n)

		if checksum == str(contador):
			observern = buf[1:9]
			sensorn = buf[9:17]

			archivo = open("transactionLog.csv","a")
			archivo.write(observern+","+sensorn+"\n")

			archivo.close()
			#lee un archivo desde el fin a principio
			with open ("data.csv", "r") as archivo2:
				from file_read_backwards import FileReadBackwards

			with FileReadBackwards("data.csv") as archivo2:


    				for line in archivo2:
        				if sensorn in line:
        					connection.send(line)
        					break

        	
			archivo2.close()	



			

	
    