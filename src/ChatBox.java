import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

// Some code from http://stackoverflow.com/questions/2162170/jtextarea-new-line-on-shift-enter

/**
 * This class represents the text input area.
 */
public class ChatBox extends JPanel
{
    // Conversation text parameters
    private Color m_color;
    private int m_textSize;
    private boolean m_italic;
    private boolean m_bold;

    // Area where user enters input
    protected JTextPane m_inputArea;

    // TextView will be the text pane from ChatView
    protected JTextPane m_textView;

    // Current conversation
    protected ChatConvo m_currentConvo;

    // Attached submit button
    protected ChatSubmit m_submitBtn;

    private static final String TEXT_SUBMIT = "text-submit";
    private static final String INSERT_BREAK = "insert-break";
    private static final String newline = "\n";


    /** 
     * Default Constructor
     */
    ChatBox() 
    {
        // Set Layout
        super(new GridLayout(1, 1));

        // Create input area
        m_inputArea = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(m_inputArea);
        this.add(scrollPane);        

        // Initialize parameters
        m_color = Color.BLACK;
        m_textSize = 12;
        m_italic = false;
        m_bold = false;
    }


    /** 
     * Constructor that takes in a ChatView object
     */
    ChatBox(ChatView newView) 
    {
        // Set Layout
        super(new GridLayout(1, 1));

        // Create input area
        m_inputArea = new JTextPane();
        m_inputArea.setEditable(true);   
        JScrollPane scrollPane = new JScrollPane(m_inputArea);
        this.add(scrollPane);

        /****************************************************************/
        // This code from
        // http://stackoverflow.com/questions/2162170/jtextarea-new-line-on-shift-enter
        InputMap input = m_inputArea.getInputMap();
        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
        input.put(shiftEnter, INSERT_BREAK); 
        input.put(enter, TEXT_SUBMIT);

        ActionMap actions = m_inputArea.getActionMap();
        actions.put(TEXT_SUBMIT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_submitBtn.doClick();
            }
        });
        /****************************************************************/

        // Add a text view
        this.m_textView = newView.getTextPane();

        // Initialize parameters
        m_color = Color.BLACK;
        m_textSize = 12;
        m_italic = false;
        m_bold = false;        
    }


    /**
     * Setter method to set current conversation
     */
    public void setChatConvo(ChatConvo chatConvo) 
    {
        m_currentConvo = chatConvo;
    }


    /** 
     * Method to turn ChatBox on
     */
    public void turnOn() 
    {
        m_inputArea.setEditable(true);
    }    


    /** 
     * Method to turn ChatBox off
     */
    public void turnOff() 
    {
        m_inputArea.setEditable(false);
    }


    /**
     * Method to attach submit button
     */
    public void attachBtn(ChatSubmit newBtn)
    {
        m_submitBtn = newBtn;
    }


    /**
     * Setter method to set text color
     */
    public void setTextColor(String color)
    {
        if(color.equals("Black"))
        {
            m_color = Color.BLACK;
        }

        // NEEDS A COLOR
        if(color.equals("Purple"))
        {
            m_color = Color.MAGENTA;
        }

        if(color.equals("White"))
        {
            m_color = Color.WHITE;
        }

        if(color.equals("Red"))
        {
            m_color = Color.RED;
        }

        if(color.equals("Blue"))
        {
            m_color = Color.BLUE;
        }        

        if(color.equals("Yellow"))
        {
            m_color = Color.YELLOW;
        }

        if(color.equals("Pink"))
        {
            m_color = Color.PINK;
        }        

        if(color.equals("Orange"))
        {
            m_color = Color.ORANGE;
        }

        if(color.equals("Green"))
        {
            m_color = Color.GREEN;
        }        

        // Set text color for input area
        SimpleAttributeSet newAttr = new SimpleAttributeSet();        
        StyleConstants.setForeground(newAttr, m_color);
        m_inputArea.setCharacterAttributes(newAttr, false);          
    }


    /**
     * Setter method to set text size
     */
    public void setTextSize(String textSize)
    {
        m_textSize = Integer.parseInt(textSize);

        // Set text size for input area
        SimpleAttributeSet newAttr = new SimpleAttributeSet();        
        StyleConstants.setFontSize(newAttr, m_textSize);
        m_inputArea.setCharacterAttributes(newAttr, false);                
    }


    /**
     * Setter method to set text style
     */
    public void setTextStyle(boolean bold, boolean italic)
    {
        m_bold = bold;
        m_italic = italic;

        // Set bold/italic for input area
        SimpleAttributeSet newAttr = new SimpleAttributeSet();
        StyleConstants.setBold(newAttr, m_bold);
        StyleConstants.setItalic(newAttr, m_italic);
        m_inputArea.setCharacterAttributes(newAttr, false);
    }


    /**
     * Method to submit text from input area
     */
    public void submitInputText()
    {
        // Get text from text field 
        String text = m_inputArea.getText();

        // Get underlying document of the ChatView object
        StyledDocument doc = m_currentConvo.getChatConvo();

        // Set attributes
        SimpleAttributeSet viewAttr = new SimpleAttributeSet();
        StyleConstants.setBold(viewAttr, m_bold);
        StyleConstants.setItalic(viewAttr, m_italic);
        StyleConstants.setForeground(viewAttr, m_color);
        StyleConstants.setFontSize(viewAttr, m_textSize);

        // Insert the text into the document
        try 
        {
            doc.insertString(doc.getLength(), text + newline, viewAttr);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        // Clear text
        m_inputArea.setText("");        
    }
}
