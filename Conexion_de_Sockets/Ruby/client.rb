#!/usr/bin/ruby
require 'socket'

s = TCPSocket.new 'localhost', 2000


def update()
	typeMsg = 'U'

	puts 'Nombre del sensor: '
	sensorn = gets.chomp #Llenar con espacios 
	while sensorn.length < 8 do
    		sensorn += " "
    end		
	
	#valida que contenga un punto
	flag = true
	while flag == true do

		puts 'Data float: '
		data = gets.chomp
		if data.include? "."
			flag = false
			while data.length < 8
    				data += " "
    		end
		else puts "Ingrese un dato punto flotante" 
	    end
	end		

	#valida un formato de 24h tomando rangos de la cadena
	flag2 = true
	while flag2 == true
		puts 'Time 24h clock HHMMSS: '
		time = gets.chomp
		if time[0,2].to_i <24 and time[2,2].to_i <60 and time[4,2].to_i<60
			
			flag2 = false
			
		else puts "Formato no valido"

		end	
	end	
	#valida el formato ddmmaaaa	tomando rangos de la cadena
	flag3 = true	
	while flag3 == true do	

		puts 'Date DDMMAAAA: '			
		date = gets.chomp
		if date[0,2].to_i <31 and date[2,2].to_i <12 and date[4,4].to_i <9999  
			flag3 = false

		else puts "Formato no valido"			
		end	
	end
	
	enviar = typeMsg + sensorn + data + time + date

	contador = 0

	#calcula el checksum
	for n in enviar.each_char
		contador += n.ord
	end

	enviar = enviar + contador.to_s

	return enviar

end

def request()
	typeMsg = 'R'
	puts 'Nombre del observer: '
	observern = gets.chomp
	while observern.length < 8 do
    		observern += " "
    end

    puts 'Nombre del sensor:'
	sensorn = gets.chomp
	while sensorn.length < 8 do
    		sensorn += " "
    end
	
	enviar = typeMsg + observern + sensorn

	contador = 0

	for n in enviar.each_char 
		contador += n.ord
		
	end

	enviar = enviar + contador.to_s

	return enviar

end

puts 'Selecciona opcion: 1)UPDATE 2)REQUEST'
opcion = gets.chomp

if opcion == '1'
	datos = update
	s.puts datos

	buf = s.gets
	if buf.length> 0
		puts buf

	end
	
elsif opcion == '2'
	datos = request
	s.puts datos

	buf = s.gets
	if buf.length> 0
		puts buf

	end

	
else puts "Opcion no valida"

end

s.close  # close socket when done