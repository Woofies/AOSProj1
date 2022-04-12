import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.io.*;

public class Server 
{
	 //Initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
 
    // Constructor with port
    public Server(int port)
    {
        
        try
        {
        	// Starts server and waits for a connection
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client...");
            socket = server.accept();
            System.out.println("Client accepted");
            System.out.println();
 
            // Takes input in the form of four 75 byte messages from the client via socket connection
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
 
            /*Read P2.txt to append the messages received to the end of it name F1*/
            int element1 = 0;// Tracks the elements in the byte array sending 100 bytes at a time so create a range
            int msgLen = 0; // Tracks the length of the message so that when it reaches 300 break the loop
            int numMsg = 1; // Number of messages for printing to console debugging
            File file = new File("P2.txt");
            byte[] F2 = textToByteArray(file);
            
            /*Loop messages have not been sent yet, slice array into 100 bytes to be sent at a time and so on*/
            while (element1 != 300)
            {
            	byte[] slice = Arrays.copyOfRange(F2, element1, (element1 + 100));
            	out.write(slice);
            	element1 += 100;
            }
            
            /*Read messages from client into 75 byte buffer for the message bytes sent and write 
             *to F1. Receive until buffer reads % ending character*/
            ByteArrayOutputStream F1 = new ByteArrayOutputStream();
            ByteArrayOutputStream Output = new ByteArrayOutputStream();
            while(msgLen != 300)
            {
            	ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            	byte buffer[] = new byte[75];
            	msgLen = msgLen + 75;
            	byteOut.write(buffer, 0, input.read(buffer));
            	F1.write(buffer);
            
            	byte result[] = byteOut.toByteArray();
            
            	String res = Arrays.toString(result);
            	System.out.println("Message " + numMsg + " from Client: " + res);
            
            	String s = new String(result, StandardCharsets.UTF_8);
            	System.out.println(s);
    		
            	numMsg++;
            	System.out.println();
            	
            	if(buffer[74] == '%')
            	{
            		Output.write(F1.toByteArray()); // For the output - append complete message received from client to the front
            		Output.write(F2); // Output write second to complete message
            		System.out.println("Message from client ended complete message below: ");
            		System.out.println(Output);
            		System.out.println();
    			
            		// Write result to new file FileName_Append.txt
            		System.out.println("Writing to text file");
            		PrintWriter writer = new PrintWriter("P2_Append.txt", "UTF-8");
            		writer.println(Output);
            		writer.close();
            	}
            } 
            
            // Close connection
            System.out.println("Closing connection");
            socket.close();
            input.close();
            System.exit(0);
        }
        
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    
    /*Read the file and convert into byte array to transmit messages in byte size*/
    private static byte[] textToByteArray(File file)
   	{
   		FileInputStream fileInputStream = null;
   		byte[] bFile = new byte[(int) file.length()];
   		try
   		{
   			fileInputStream = new FileInputStream(file);
   			fileInputStream.read(bFile);
   			fileInputStream.close();
   			for(int i = 0; i < bFile.length; i++)
   			{
   				
   			}
   		}
   		
   		catch (Exception e)
   		{
   			e.printStackTrace();
   		}
   		return bFile;
   	}
    
    public static void main(String args[])
    {
        Server server = new Server(5000); // Runs on dc03.utdallas.edu
    }
}
