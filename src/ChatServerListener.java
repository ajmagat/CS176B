import javax.swing.text.*;
import java.net.*;
import java.io.*;

public class ChatServerListener implements Runnable
{
    private DataInputStream m_inStream;
    private StyledDocument m_chatConvo;

    /**
     * Default Constructor
     */
    ChatServerListener()
    {

    }

    /**
     * Constructor that takes in a socket and a StyledDocument
     */
    ChatServerListener(Socket clientSock, StyledDocument chatConvo)
    {
        try
        {
            m_inStream = new DataInputStream(clientSock.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println("Problem connecting\n" + e);
        }
        
        m_chatConvo = chatConvo;
    }

    public void run()
    {

    }
}
