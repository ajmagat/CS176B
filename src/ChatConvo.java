import javax.swing.text.*;

/**
 * This class represents the chat conversation 
 */

public class ChatConvo {
	// Conversation text parameters
	private String m_color;
	private String m_textSize;
	private boolean m_italic;
	private boolean m_bold;

	// Document that will hold this chat
	private StyleContext m_context;
    private StyledDocument m_chatConvo;

    /**
     * Default Constructor
     */
	ChatConvo()
	{
		m_context = new StyleContext( );
        m_chatConvo = new DefaultStyledDocument(m_context);
	}

    /**
     * Constructor that takes in two parctipants WIP
     */
    // ChatConvo(two people)

    /**
     * Constructor that takes in more than two people WIP
     */
    // ChatConvo(Group of people)

    /**
     * Getter method to return chatDoc
     */
    public StyledDocument getChatConvo()
    {
    	return m_chatConvo;
    }


    /**
     * Method to set color 
     */
    /**
     * Method to set
     */
    /**
     * Method to set
     */
    /**
     * Method to set
     */              
}
