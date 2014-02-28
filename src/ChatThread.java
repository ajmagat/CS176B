import java.net.*;
import java.io.*;


public class ChatThread extends Thread
{
	public DataOutputStream m_outStream;
	private DataInputStream m_inStream;
	private Socket m_clientSock;
	private ChatThread[] m_connections;
	private ChatServer m_server;

	public ChatThread()
	{
		m_outStream = null;
		m_inStream = null;
		m_clientSock = null;
		m_connections = null;
		m_server = null;
	}

	public ChatThread(Socket clientSock, ChatThread[] connections, ChatServer server)
	{
		m_connections = connections;
		m_server = server;
		try
		{
			m_clientSock = clientSock;
			m_outStream = new DataOutputStream(m_clientSock.getOutputStream());
			m_inStream = new DataInputStream(m_clientSock.getInputStream());	
		}
		catch (IOException e)
		{
			System.out.println("Connection cannot be established\n" + e);
		}
	}

	public void run() 
	{
		try
		{
			// say someone joined the room
/*			synchronized (this)
			{
				for(int c = 0; c < m_connections.length; c++)
				{
					if(m_connections[c] != null && m_connections[c] != this)
					{
						// do something like "____ has joined chat"
					}
				}
			}
*/
			while(true)
			{
System.out.println("AM I HERE");				
				// Wait for message type byte
				byte [] msgType = new byte[1];
				m_inStream.readFully(msgType);
	            String str = new String(msgType, "UTF-8");
System.out.println("HOW asdfasfas");
	            // Broadcast message
	            if(str.equals("1"))
	            {
System.out.println("GETTING THE MESSAGE1");
	            	// Get the text parameters for this object
	            	byte[] textParams = new byte[5];	            	
	            	m_inStream.readFully(textParams);
System.out.println("GETTING THE MESSAGE2 " + textParams.length);	            	
	            	// Get size of message
	            	byte[] msgSize = new byte[10];
	            	m_inStream.readFully(msgSize);
System.out.println("GETTING THE MESSAGE3");	     	            	
	            	String sizeStr = new String(msgSize, "UTF-8");
	            	int sizeInt = Integer.parseInt(sizeStr);
System.out.println("GETTING MESSAGE3.5 " + sizeInt);
	            	// Get message
	            	byte[] msg = new byte[sizeInt];
	            	m_inStream.readFully(msg);
System.out.println("GETTING THE MESSAGE4");	     
	            	
	            	// Create message type byte
	            	String serverMsgType = "11";
	                byte[] serverMsgByte = serverMsgType.getBytes("UTF-8"); 
System.out.println("GETTING THE MESSAGE5");	    	            	
	            	byte[] fullMessage = createMessage(serverMsgByte, textParams, msgSize, msg);
	            	
					// all the talking
					synchronized (this)
					{
System.out.println("DO I GET IN HERE");
						m_connections = m_server.getThreads();
						for(int c = 0; c < m_connections.length; c++)
						{
System.out.println("TRYING TO SEND STUFF");							
							if(m_connections[c] != null && m_connections[c] != this)
							{
System.out.println("AYE SENDING SHIT");
								// broadcast message
								m_connections[c].m_outStream.write(fullMessage);

							}
						}
					}		            	
	            }			

			}
		}
		catch (Exception e)
		{
			// send error to client
		}
	}
	
	
	/**
	 * Private helper method to create a fully formatted message
	 * @return Message in byte array form
	 */
	private byte[] createMessage(byte[] msgByte, byte[] textParams, byte[] msgSize, byte[] msg)
	{
System.out.println("in create message");
		int lenMsg = msg.length;
System.out.println("message lenght " + lenMsg);		
		byte[] fullMsg = new byte[1 + 5 + 10 + lenMsg];
		
		System.arraycopy(msgByte, 0, fullMsg, 0, 1);
		System.out.println("in create message 1");
		System.arraycopy(textParams, 0, fullMsg, 1, 5);
		
		
		
		System.out.println("in create message 2");
		
		System.arraycopy(msgSize, 0, fullMsg, 6, 10);
		System.out.println("in create message 3");
		System.arraycopy(msg, 0, fullMsg, 16, lenMsg);
System.out.println("Leaving create message");
		return fullMsg;
	}
}
