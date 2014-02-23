// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * This class represents the area where you view the conversation
 */
public class ChatView extends JPanel
{
    // Current conversation
    protected ChatConvo m_currentConvo;

    // Area to view current conversation 
    protected JTextPane m_viewingPane;

    private final static String newline = "\n";


    /**
     * Default Constructor
     */
    ChatView( ) {
        super(new GridLayout(1, 1));
        m_viewingPane = new JTextPane();
        m_viewingPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(m_viewingPane);

        this.add(scrollPane);
    }

 
    /**
     * Getter method to return tet
     */
    public JTextPane getTextPane( ) {
        return this.m_viewingPane;
    }

    /**
     * Setter method to set current conversation
     */
    public void setChatConvo(ChatConvo chatConvo) {
        m_currentConvo = chatConvo;
        m_viewingPane.setDocument(m_currentConvo.getChatConvo());
    }    
}
