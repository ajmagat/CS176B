import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class ChatOptions extends JPanel implements ActionListener {
	// The conversation currently associated with this ChatOption
	protected ChatConvo m_currentConvo;

	// The combo box of this ChatOption
	protected JComboBox m_comboBox;

    // The type of box
    private String m_boxType;

    // String arrays representing potential values for ChatBox
    private final String [] colorStrings = { "Black", "Purple", "White",
    										 "Red", "Blue", "Yellow", 
     										 "Pink", "Orange", "Green" };
    private final String [] textStrings = { "8", "10", "12", "14", "16", "18", "20" };


    /**
     * Default Constructor
     */
    ChatOptions() 
    {
    	m_comboBox = new JComboBox();
    }


    /**
     * Constructor that takes in a type
     */
	ChatOptions(String boxType) 
	{
        super(new GridLayout(1, 1));

		// Initialize combo box based on type
		if(boxType.equals("Color")) {
			m_comboBox = new JComboBox(colorStrings);
			m_boxType = boxType;
		}

		if(boxType.equals("Size")) {
			m_comboBox = new JComboBox(textStrings);
			m_comboBox.setSelectedIndex(2);
			m_boxType = boxType;
		}

		this.add(m_comboBox);
	}


    /**
     * Setter method to set current conversation
     */
    public void setChatConvo(ChatConvo chatConvo) 
    {
    	m_currentConvo = chatConvo;
    }

    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
    	JComboBox cb = (JComboBox)e.getSource();
    	String chosenValue = (String)cb.getSelectedItem();


    }

}
