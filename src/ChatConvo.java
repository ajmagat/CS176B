import javax.swing.text.*;
import java.net.*;
import java.io.*;

/**
 * This class represents the chat conversation 
 */
public class ChatConvo 
{
	// Document that will hold this chat
	private StyleContext m_context;
    private StyledDocument m_chatConvo;

    // TCP socket representing outgoing connection
    private Socket m_sendSock;

    // Output stream to send response to outgoing connection
    private DataOutputStream m_outStream;

    // Input stream to receive response from connection
    private DataInputStream m_inStream;

    private static final int HEADER_SIZE = 10;


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
     * Constructor that takes in two participants WIP
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
            m_outStream = new DataOutputStream(m_sendSock.getOutputStream());
            ChatServerListener listener = new ChatServerListener(m_sendSock, m_chatConvo);    

            (new Thread(listener)).start();           
		}
        catch (UnknownHostException e)
        {
            System.out.println("Cannot find host " + hostname);
        }
		catch (IOException e)
		{
			System.out.println(e);
		}	
    }

    /**
     * Getter method to return chatDoc
     */
    public StyledDocument getChatConvo()
    {
    	return m_chatConvo;
    }            


    /**
     * Method to send message to server
     */
    public void sendMessage(String header, String message)
    {
    	// Split up the message according to self defined protocol (10 byte length limit)
        String[] messages = splitMessage(message);

        // Create string builder to hold the message
        StringBuilder messageBuilder = new StringBuilder();

        // Create message
        for(int i = 0; i < messages.length; i++)
        {
            messageBuilder.append(header);        	
            messageBuilder.append(messages[i]);
        }

        // Make the message a string
        String msgStr = messageBuilder.toString();

        // Turn string into a byte array and send
        try
        {
            byte[] msgBytes = msgStr.getBytes("UTF-8"); 
            m_outStream.write(msgBytes);        
        }
        catch (IOException e)
        {
            System.out.println("Unsupported encoding\n " + e);
        }
    }


    /**
     * Private method to split message depending on size
     */
    private String[] splitMessage(String message)
    {
        // Get length of message
        int msgLen = message.length();

        // Find how many times message should be split
        int msgSplits = msgLen / HEADER_SIZE;
        if(msgLen % HEADER_SIZE != 0)
        {
            msgSplits++;
        }

        // Create array of strings to hold message partitions 
        String[] messagePartitions = new String[msgSplits]; 

        // Fill up array
        int begin = 0;
        int end = msgLen;
        for(int i = 0; i < msgSplits; i++)
        {
            if(i < (msgSplits - 1))
            {
                messagePartitions[i] = message.substring(begin, end);
                begin += msgLen;
                end += msgLen;
            }
            else
            {
                messagePartitions[i] = message.substring(begin);
            }
        }

        return messagePartitions;
    }

    /**
     * Method to close connection
     */
    public void closeConvo()
    {
    	try
    	{
    		m_inStream.close();
    		m_outStream.close();
    		m_sendSock.close();
    	}
    	catch (IOException e)
    	{
    		System.out.println(e);
    	}
    }
}
