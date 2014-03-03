import java.net.*;
import java.io.*;

// Multithreading example from: http://www.ase.md/~aursu/ClientServerThreads.html


public class ChatServer 
{
    private static final int MAX_CONNECTIONS = 10;
    private static final int DEFAULT_PORT = 8080;
    private ChatThread[] m_connections; 
    private ServerSocket m_serverSock;


    /**
     * Default Constructor
     */
    ChatServer()
    {
        try
        {
            m_serverSock = new ServerSocket(DEFAULT_PORT);
            m_connections = new ChatThread[MAX_CONNECTIONS];
        }
        catch (IOException e)
        {
            System.out.println("Chat Server failed to start up " + e);
        }       
    }


    /**
     * Constructor that takes in a port
     */
    ChatServer(int port)
    {
        try
        {
            m_serverSock = new ServerSocket(port);
            m_connections = new ChatThread[MAX_CONNECTIONS];

        }
        catch (IOException e)
        {
            System.out.println("Chat Server failed to start up\n" + e);
        }

        runServer();
    }

    
    public ChatThread[] getThreads()
    {
        return m_connections;
    }

    /**
     * Method that will run the server
     */
    private void runServer()
    {
        // Socket to hold the actual socket to client
        Socket s = null;

        // Variable to keep track of how many people are using this server,
        // or in other words how many people are connected to the same room
        int connection_count = 0;

        while(true)
        {
            try
            {
                // Wait for a connection and except
                s = m_serverSock.accept();
                // Check if there are already to many connections to server
                if(connection_count >= MAX_CONNECTIONS)
                {
                    // tell user we can't accept them

                    // Close socket
                    s.close();

                }
                else
                {   
                    ChatThread conn_thread = new ChatThread(s, m_connections, this);    
System.out.println("1 NEW CONNECTION #" + connection_count);
                    m_connections[connection_count] = conn_thread;
                    connection_count++;             
                    conn_thread.start();                    
                }
            }
            catch (IOException e)
            {
                System.out.println("Connection cannot be established\n" + e);
            }
        }
    }
}
