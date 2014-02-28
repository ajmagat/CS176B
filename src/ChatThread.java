import java.net.*;
import java.io.*;


public class ChatThread extends Thread
{
	public DataOutputStream m_outStream;
	private DataInputStream m_inStream;
	private Socket m_clientSock;
	private ChatThread[] m_connections;

	public ChatThread()
	{
		m_outStream = null;
		m_inStream = null;
		m_clientSock = null;
		m_connections = null;
	}

	public ChatThread(Socket clientSock, ChatThread[] connections)
	{
		m_connections = connections;
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
				// Wait for message type byte
				byte msgType = m_inStream.readByte();
	            String str = new String(new byte[] { msgType }, "UTF-8");

	            // Broadcast message
	            if(str.equals("1"))
	            {
	            	// Get the text parameters for this object
	            	byte[] textParams = new byte[4];	            	
	            	m_inStream.readFully(textParams);
	            	
	            	// Get size of message
	            	byte[] msgSize = new byte[10];
	            	m_inStream.readFully(msgSize);
	            	String sizeStr = new String(msgSize, "UTF-8");
	            	int sizeInt = Integer.parseInt(sizeStr);
	            	
	            	// Get message
	            	byte[] msg = new byte[sizeInt];
	            	m_inStream.readFully(msg);
	            	
	            	// Create message type byte
	            	String serverMsgType = "11";
	                byte[] serverMsgByte = serverMsgType.getBytes("UTF-8"); 
	            	
	            	byte[] fullMessage = createMessage(serverMsgByte, textParams, msgSize, msg);
	            	
					// all the talking
					synchronized (this)
					{
						for(int c = 0; c < m_connections.length; c++)
						{
							if(m_connections[c] != null && m_connections[c] != this)
							{
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
		int lenMsg = msg.length;
		byte[] fullMsg = new byte[1 + 4 + 10 + lenMsg];
		
		System.arraycopy(msgByte, 0, fullMsg, 0, 1);
		System.arraycopy(textParams, 0, fullMsg, 1, 5);
		System.arraycopy(msgSize, 0, fullMsg, 5, 15);
		System.arraycopy(msg, 0, fullMsg, 15, 15 + lenMsg);
		
		return fullMsg;
	}
}
