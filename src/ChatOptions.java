import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatOptions extends JPanel implements ActionListener {
	// The chat box area associated with this ChatOption
	protected ChatBox m_chatBox;

	// The combo box of this ChatOption
	protected JComboBox m_comboBox;

	// The type of box
	private String m_boxType;

	// String arrays representing potential values for ChatBox
	private final String[] colorStrings = { "Black", "Purple", "White", "Red",
			"Blue", "Yellow", "Pink", "Orange", "Green" };
	private final String[] textStrings = { "8", "10", "12", "14", "16", "18",
			"20" };

	/**
	 * Default Constructor
	 */
	ChatOptions() {
		m_comboBox = new JComboBox();
		m_boxType = null;
	}

	/**
	 * Constructor that takes in a type
	 */
	ChatOptions(String boxType, ChatBox newChatBox) {
		super(new GridLayout(1, 1));

		// Initialize combo box based on type
		if (boxType.equals("Color")) {
			m_comboBox = new JComboBox(colorStrings);
			m_boxType = boxType;
		}

		if (boxType.equals("Size")) {
			m_comboBox = new JComboBox(textStrings);
			m_comboBox.setSelectedIndex(2);
			m_boxType = boxType;
		}

		m_chatBox = newChatBox;
		m_comboBox.addActionListener(this);
		this.add(m_comboBox);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String chosenValue = (String) cb.getSelectedItem();

		if (m_boxType.equals("Color")) {
			m_chatBox.setTextColor(chosenValue);
		}

		if (m_boxType.equals("Size")) {
			m_chatBox.setTextSize(chosenValue);
		}
	}

}
