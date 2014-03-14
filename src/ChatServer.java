import java.net.*;
import java.io.*;

import javax.net.ssl.*;

// Multithreading example from: http://www.ase.md/~aursu/ClientServerThreads.html

public class ChatServer {
    // The maximum amount of connections the chat server will accept
    private static final int MAX_CONNECTIONS = 10;

    // Default port number
    private static final int DEFAULT_PORT = 8080;

    private int m_connection_count;

    // Array to hold all the connections
    private ChatThread[] m_connections;

    // The SSLServerSocketFactory and the actual SSLServerSocket
    private SSLServerSocketFactory m_sslServerSockFactory;
    private SSLServerSocket m_sslServerSock;

    /**
     * Default Constructor
     */
    ChatServer() {
        try {
            m_connections = null;
            m_sslServerSockFactory = null;
            m_sslServerSock = null;
            m_connection_count = 0;
        } catch (Exception e) {
            System.out.println("Chat Server failed to start up " + e);
        }
    }

    /**
     * Constructor that takes in a port
     */
    ChatServer(int port) {
        try {
            m_connections = new ChatThread[MAX_CONNECTIONS];
            m_sslServerSockFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            m_sslServerSock = (SSLServerSocket) m_sslServerSockFactory.createServerSocket(port);
            m_connection_count = 0;
        } catch (IOException e) {
            System.out.println("Chat Server failed to start up\n" + e);
        }

        runServer();
    }

    public ChatThread[] getThreads() {
        return m_connections;
    }

    /**
     * Method that will run the server
     */
    private void runServer() {
        // Socket to hold the actual socket to client
        Socket s = null;

        // Variable to keep track of how many people are using this server,
        // or in other words how many people are connected to the same room

        while (true) {
            try {
                // Wait for a connection and accept
                s = m_sslServerSock.accept();
                // Check if there are already to many connections to server
                if (m_connection_count >= MAX_CONNECTIONS) {
                    // tell user we can't accept them

                    // Close socket
                    s.close();

                } else {
                    ChatThread conn_thread = new ChatThread(s, m_connections,
                            this);
                    System.out.println("1 NEW CONNECTION #" + m_connection_count);
                    m_connections[m_connection_count] = conn_thread;
                    m_connection_count++;
                    conn_thread.start();
                }
            } catch (IOException e) {
                System.out.println("Connection cannot be established\n" + e);
            }
        }
    }
}
