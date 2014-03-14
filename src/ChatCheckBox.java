import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ChatCheckBox extends JPanel implements ActionListener {
	// The chat box area associated with this ChatOption
	protected ChatBox m_chatBox;
	
	// The combo box of this ChatOption
	protected JCheckBox m_checkBox;

	// The type of box
	private String m_boxType;
	
	// Whether check box is check or not
	private boolean m_checked;
	
	/**
	 * Default Constructor
	 */
	ChatCheckBox() {
		m_checkBox = new JCheckBox();
		m_boxType = null;
		m_checked = false;
	}

	/**
	 * Constructor that takes in a type
	 */
	ChatCheckBox(String boxType, ChatBox newChatBox) {
		super(new GridLayout(1, 1));

		m_checkBox = new JCheckBox(boxType);
		m_boxType = boxType;
		m_checked = false;
		m_chatBox = newChatBox;
		m_checkBox.addActionListener(this);
		this.add(m_checkBox);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(m_checked)
		{
			m_checked = false;
		}
		else
		{
			m_checked = true;
		}
		
		if(m_boxType.equals("Bold"))
		{
			m_chatBox.setBold(m_checked);
		}
		
		if(m_boxType.equals("Italic"))
		{
			m_chatBox.setItalic(m_checked);
		}

	}
}