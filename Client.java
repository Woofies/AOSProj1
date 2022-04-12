import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.io.*;

public class Client 
{
	 // Initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream  input = null;
    private DataOutputStream out = null;

    // Constructor to put ip address and port
    public Client(String address, int port)
    {
        // establish a connection
        try
        {
        	// Connect to server
            socket = new Socket(address, port);
            System.out.println("Connected");
 
            // input from socket server connection and output
            input  = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out    = new DataOutputStream(socket.getOutputStream());
        }
        
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        
        catch(IOException i)
        {
            System.out.println(i);
        }
 
        try
        {
        // string to read message from input
        int element1 = 0; // Tracks the elements in the byte array sending 100 bytes at a time so create a range
        int msgLen = 0; // Tracks the length of the message so that when it reaches 300 break the loop
        int numMsg = 1; // Number of messages for printing to console debugging
        File file = new File("P1.txt");
        byte[] F1 = textToByteArray(file);
        
        /*Loop while message have not been fully sent yet, slice array to 75 bytes to be sent and so on*/
        while (element1 != 300)
        {
        	byte[] slice = Arrays.copyOfRange(F1, element1, (element1 + 75));
        	out.write(slice);
        	element1 += 75;
        }
        
        /*Read messages from client into 100 byte buffer for the message bytes sent and write 
         *to F2. Receive until buffer reads % ending character*/
        ByteArrayOutputStream F2 = new ByteArrayOutputStream();
        ByteArrayOutputStream Output = new ByteArrayOutputStream();
        Output.write(F1); // Output write P1.txt to the front
        while (msgLen != 300)
        {
        		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                byte buffer[] = new byte[100];
                msgLen = msgLen + 100;
                byteOut.write(buffer, 0, input.read(buffer));
                F2.write(buffer);
             
                byte result[] = byteOut.toByteArray();
                
                String res = Arrays.toString(result);
                System.out.println("Message " + numMsg + " from Server: " + res);
                
                String s = new String(result, StandardCharsets.UTF_8);
        		System.out.println(s);
                
        		numMsg++;
        		System.out.println();
        		
        		if(buffer[99] == '%')
        		{
        			Output.write(F2.toByteArray()); // Output append message from server aka P2.txt to the end
        			System.out.println("Message from server ended complete message below: ");
        			System.out.println(Output);
        			System.out.println();
        			
        			// Write result to new file FileName_Append.txt
        			System.out.println("Writing to text file");
        			PrintWriter writer = new PrintWriter("P1_Append.txt", "UTF-8");
        			writer.println(Output);
        			writer.close();
        		}
        	}
     
        	// Close connection
        	System.out.println("Closing Connection");
        	input.close();
        	out.close();
        	socket.close();
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
        Client client = new Client("dc03.utdallas.edu", 5000); // Run on dc01.utdallas.edu and server runs on dc03.utdallas.edu port 5000
    }
 
}
