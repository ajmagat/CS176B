import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * This class represents the text input area.
 */
public class ChatBox extends JPanel implements FocusListener {
    // Area where user enters input
    protected JTextArea m_inputArea;

    // TextView will be the text pane from ChatView
    protected JTextPane m_textView;

    // Current conversation
    protected ChatConvo m_currentConvo;

    private final static String newline = "\n";


    /** 
     * Default Constructor
     */
    ChatBox() {

    }


    /** 
     * Constructor that takes in a ChatView object
     */
    ChatBox(ChatView newView) {
        super(new GridLayout(1, 1));
        m_inputArea = new JTextArea();
        m_inputArea.setEditable(true);
        m_inputArea.addFocusListener(this);      
        JScrollPane scrollPane = new JScrollPane(m_inputArea);
        this.add(scrollPane);
        this.m_textView = newView.getTextPane();
    }


    /**
     * Setter method to set current conversation
     */
    public void setChatConvo(ChatConvo chatConvo) {
        m_currentConvo = chatConvo;
    }


    /** 
     * Method to turn ChatBox on
     */
    public void turnOn() {
        m_inputArea.setEditable(true);
    }    


    /** 
     * Method to turn ChatBox off
     */
    public void turnOff() {
        m_inputArea.setEditable(false);
    }


    @Override
    public void focusLost(FocusEvent evt) {
        // Get text from text field 
        String text = m_inputArea.getText();

        // Get underlying document of the ChatView object
        StyledDocument doc = m_currentConvo.getChatConvo();

        //SimpleAttributeSet boldBlue = new SimpleAttributeSet();
        //StyleConstants.setBold(boldBlue, true);
        //StyleConstants.setForeground(boldBlue, Color.blue);
        //StyleConstants.setFontSize(highAlert, 18);
        //StyleConstants.setItalic(highAlert, true);

        // Insert the text into the document
        try {
            doc.insertString(doc.getLength(), text + newline, null);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        //textArea.setCaretPosition(textArea.getDocument().getLength());
    }


    @Override
    public void focusGained(FocusEvent evt) {

    }

}

