require 'socket'

server = TCPServer.new 2000 # Server bound to port 2000

loop do
  client = server.accept    # Wait for a client to connect
  buf = client.gets

   if buf.length > 0 # read data send by client
 	puts buf
   end

   contador = 0

	typeMsg = buf[0]
	
	if typeMsg == 'U'
		checksum = buf[-5,4]
		datos = buf[0,31]

		for n in datos.each_char
			contador += n.ord
		end

		if checksum == contador.to_s
			
			sensorn = buf[1,8]
			data = buf[9,8]
			time = buf[17,6]
			date = buf[23,8]

			aFile = File.new("data.csv", "a")
			if aFile
   			aFile.syswrite(sensorn+","+data+","+time+","+date+"\n")
   			
			else
  			 puts "Unable to open file!"
			end

			client.puts'Sensor registrado'
		end
	
	elsif typeMsg == 'R'
		
		checksum = buf[-5,4]
		datos = buf[0,17]

		for n in datos.each_char
			contador += n.ord
		end

		if checksum == contador.to_s
			observern = buf[1,8]
			sensorn = buf[9,8]

			aFile2 = File.new("transactionLog.csv", "a")
			if aFile2
   			aFile2.syswrite(observern+","+sensorn+"\n")
   			
			else
  			 puts "Unable to open file!"
			end


			
			#lee un archivo desde el fin a principio

			aFile3 = File.new("data.csv", "r")
			
   			aFile3.each_line.reverse_each do |line|
  			
  			if line.include? sensorn
  				client.puts line
  				break
  				
  			end


			
        		

    	end


	end

  client.close
end
end
