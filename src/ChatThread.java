import java.net.*;
import java.io.*;


public class ChatThread extends Thread
{
	private DataOutputStream m_outStream;
	private DataInputStream m_inStream;
	private Socket m_clientSock;
	private final Thread[] m_connections;

	public ChatThread()
	{
		m_outStream = null;
		m_inStream = null;
		m_clientSock = null;
		m_connections = null;
	}

	public ChatThread(Socket clientSock, Thread[] connections)
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
			synchronized (this)
			{
				for(int c = 0; c < m_connections.length; c++)
				{
					if(m_connections[c] != null && m_connections[c] != this)
					{
						// do something like "____ has joined chat"
					}
				}
			}

			while(true)
			{
				// Wait for input 


				// all the talking
				synchronized (this)
				{
					for(int c = 0; c < m_connections.length; c++)
					{
						if(m_connections[c] != null && m_connections[c] != this)
						{
							// broadcast message
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
}
