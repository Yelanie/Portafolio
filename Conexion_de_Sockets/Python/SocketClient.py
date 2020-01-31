#!/usr/bin/python

import socket

#Python 2.7

clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
clientsocket.connect(('localhost', 8089)) 

  

contador = 0

 
def update():
	global contador
	typeMsg = "U"
	sensorn = raw_input("Nombre del sensor: ") #Llenar con espacios 
	while len(sensorn) < 8:
    		sensorn += " "
	#valida que contenga un punto
	flag = True
	while flag == True:
		data = raw_input("Data float: ")
		if "." in data:
			flag = False
			while len(data) < 8:
    				data += " "
		else: print "Ingrese un dato punto flotante"		

	#valida un formato de 24h tomando rangos de la cadena
	flag2 = True
	while flag2 == True:
		time = raw_input("Time 24h clock HHMMSS: ")
		if int(time[:2]) <24 and int(time[2:4]) <60 and int(time[4:])<60:
			flag2 = False
			
		else: print "Formato no valido"

	#valida el formato ddmmaaaa	tomando rangos de la cadena
	flag3 = True	
	while flag3 == True:				
		date = raw_input("Date DDMMAAAA: ")
		if int(date[:2]) <31 and int(date[2:4]) <12 and int(date[4:]) <9999  :
			flag3 = False
		
		else: print "Formato no valido"			

	enviar = typeMsg+sensorn+data+time+date

	#calcula el checksum
	for n in enviar: 
		contador += ord(n)

	enviar = enviar + str(contador)

	
	clientsocket.send(enviar)

	buf = clientsocket.recv(64)
	if len(buf)> 0:
		print buf
	
	return 

def request():
	global contador
	typeMsg = "R"
	observern = raw_input("Nombre del observer: ")
	while len(observern) < 8:
    		observern += " "
	sensorn = raw_input("Nombre del sensor: ")
	while len(sensorn) < 8:
    		sensorn += " "

	enviar = typeMsg + observern + sensorn

	for n in enviar: 
		contador += ord(n)

	
	enviar = enviar + str(contador)

	

	clientsocket.send(enviar)
	buf = clientsocket.recv(64)
	if len(buf)> 0:
		print "Ultima medicion: "+buf

	return 

print "Selecciona opcion: 1)UPDATE 2)REQUEST"
opcion = input()

if opcion == 1:
	update()
	
elif opcion == 2:
	request()
else: print "Opcion no valida"


