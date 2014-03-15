import java.net.*;
import java.io.*;

import javax.net.ssl.*;

public class ChatThread extends Thread {
    public DataOutputStream m_outStream;
    private DataInputStream m_inStream;
    private Socket m_clientSock;
    private ChatThread[] m_connections;
    private ChatServer m_server;

    public ChatThread() {
        m_outStream = null;
        m_inStream = null;
        m_clientSock = null;
        m_connections = null;
        m_server = null;
    }

    public ChatThread(Socket clientSock, ChatThread[] connections,
            ChatServer server) {
        m_connections = connections;
        m_server = server;
        try {
            m_clientSock = clientSock;
            m_outStream = new DataOutputStream(m_clientSock.getOutputStream());
            m_inStream = new DataInputStream(m_clientSock.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection cannot be established\n" + e);
        }
    }

    public void run() {
        try {
            // say someone joined the room
            /*
             * synchronized (this) { for(int c = 0; c < m_connections.length;
             * c++) { if(m_connections[c] != null && m_connections[c] != this) {
             * // do something like "____ has joined chat" } } }
             */
            while (true) {
                // Wait for message type byte
                byte[] msgType = new byte[1];
                m_inStream.readFully(msgType);
                String str = new String(msgType, "UTF-8");

                // Broadcast message
                if (str.equals("1")) {

                    // Get the text parameters for this object
                    byte[] textParams = new byte[5];
                    m_inStream.readFully(textParams);
                    
                    // Get the size of the username
                    byte[] nameSize = new byte[2];
                    m_inStream.readFully(nameSize);

                    // Get size of message
                    byte[] msgSize = new byte[10];
                    m_inStream.readFully(msgSize);

                    String nameSizeStr = new String(nameSize, "UTF-8");
                    int nameSizeInt = Integer.parseInt(nameSizeStr);
                    
                    String sizeStr = new String(msgSize, "UTF-8");
                    int sizeInt = Integer.parseInt(sizeStr);

                    // Get username
                    byte[] nme = new byte[nameSizeInt];
                    m_inStream.readFully(nme);
                    
                    // Get message
                    byte[] msg = new byte[sizeInt];
                    m_inStream.readFully(msg);

                    // Create message type byte
                    String serverMsgType = "1";
                    byte[] serverMsgByte = serverMsgType.getBytes("UTF-8");

                    byte[] fullMessage = createMessage(serverMsgByte,
                            textParams, nameSize, msgSize, nme, msg);

                    // all the talking
                    synchronized (this) {
                        m_connections = m_server.getThreads();
                        for (int c = 0; c < m_connections.length; c++) {
                            if (m_connections[c] != null
                                    && m_connections[c] != this) {
                                // broadcast message
                                m_connections[c].m_outStream.write(fullMessage);

                            }
                        }
                    }
                }
                
                if (str.equals("9"))
                {
                    break;
                }
                

            } // end while

        } catch (Exception e) {
            // send error to client
            try
            {
                m_outStream.close();
                m_inStream.close();
                m_clientSock.close();
                synchronized (this)
                {
                    for(int i = 0; i < m_connections.length; i++)
                    {
                        if (m_connections[i] == this)
                        {
                            m_connections = null;
                        }
                    }
                }
            }
            catch (Exception e2)
            {
                System.out.println("Could not close connection");
            }

        }
        finally
        {
            try
            {
                m_outStream.close();
                m_inStream.close();
                m_clientSock.close();
                synchronized (this)
                {
                    for(int i = 0; i < m_connections.length; i++)
                    {
                        if (m_connections[i] == this)
                        {
                            m_connections[i] = null;
                        }
                    }
                }
            }
            catch (Exception e3)
            {
                System.out.println("Could not close connection");
            }
        }
    }

    /**
     * Private helper method to create a fully formatted message
     * 
     * @return Message in byte array form
     */
    private byte[] createMessage(byte[] msgByte, byte[] textParams, byte[] nameSize,
            byte[] msgSize, byte[] nme, byte[] msg) {
    	int lenNme = nme.length;
        int lenMsg = msg.length;
        byte[] fullMsg = new byte[1 + 5 + 2 + 10 + lenNme + lenMsg];

        System.arraycopy(msgByte, 0, fullMsg, 0, 1);
        System.arraycopy(textParams, 0, fullMsg, 2, 5);
        System.arraycopy(nameSize, 0, fullMsg, 7, 2);
        System.arraycopy(msgSize, 0, fullMsg, 9, 10);
        System.arraycopy(nme, 0, fullMsg, 19, lenNme);
        System.arraycopy(msg, 0, fullMsg, 19+lenNme, lenMsg);

        return fullMsg;
    }
}
