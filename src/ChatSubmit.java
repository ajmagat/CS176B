import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatSubmit extends JPanel implements ActionListener
{
    // Submit button
    JButton m_submitBtn;

    // Connected chat box
    ChatBox m_chatBox;

    /**
     * Default constructor
     */
    ChatSubmit()
    {
        // Set layout
        super(new GridLayout(1, 1));

        // Create submit button
        m_submitBtn = new JButton("Submit");
        m_chatBox = null;
        this.add(m_submitBtn);
    }


    /**
     * Constructor that takes in a ChatBox parameter
     */
    ChatSubmit(ChatBox newChatBox)
    {
        // Set Layout
        super(new GridLayout(1, 1));

        // Create submit button
        m_submitBtn = new JButton("Submit");
        m_submitBtn.addActionListener(this);
        this.add(m_submitBtn);

        // Initialize chatBox
        m_chatBox = newChatBox;     
    }


    /**
     * Method to click underlying JButton
     */
    public void doClick()
    {
        m_submitBtn.doClick();
    }


    /**
     * Getter method to return underlying JButton
     */
    public JButton getButton()
    {
        return m_submitBtn;
    }


    @Override
    public void actionPerformed(ActionEvent e) 
    {
        m_chatBox.submitInputText();
    }
}
