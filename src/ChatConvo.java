import javax.swing.text.*;
import java.net.*;
import java.io.*;

/**
 * This class represents the chat conversation 
 */
public class ChatConvo {
	// Document that will hold this chat
	private StyleContext m_context;
    private StyledDocument m_chatConvo;

    // TCP socket representing outgoing connection
    private Socket m_sendSock;

    /**
     * Default Constructor
     */
	ChatConvo()
	{
		m_context = new StyleContext( );
        m_chatConvo = new DefaultStyledDocument(m_context);
        m_sendSock = null;
	}

    /**
     * Constructor that takes in two parctipants WIP
     */
    // ChatConvo(two people)

    /**
     * Constructor that takes in the address of a server and port number
     */
    ChatConvo(String hostname, int port)
    {
		m_context = new StyleContext( );
        m_chatConvo = new DefaultStyledDocument(m_context);
        try
        {
			m_sendSock = new Socket(hostname, port);
		}
		catch(IOException e)
		{

		}	

    }

    /**
     * Getter method to return chatDoc
     */
    public StyledDocument getChatConvo()
    {
    	return m_chatConvo;
    }            
}
