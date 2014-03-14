import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

// Some code from http://stackoverflow.com/questions/2162170/jtextarea-new-line-on-shift-enter

/**
 * This class represents the text input area.
 */
public class ChatBox extends JPanel {
	// Conversation text parameters
	private Color m_color;
	private String m_colorStr;
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
	ChatBox() {
		// Set Layout
		super(new GridLayout(1, 1));

		// Create input area
		m_inputArea = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(m_inputArea);
		this.add(scrollPane);

		// Initialize parameters
		m_color = Color.BLACK;
		m_colorStr = "Black";
		m_textSize = 12;
		m_italic = false;
		m_bold = false;
	}

	/**
	 * Constructor that takes in a ChatView object
	 */
	ChatBox(ChatView newView) {
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
		m_colorStr = "Black";
		m_textSize = 12;
		m_italic = false;
		m_bold = false;
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

	/**
	 * Method to attach submit button
	 */
	public void attachBtn(ChatSubmit newBtn) {
		m_submitBtn = newBtn;
	}

	/**
	 * Setter method to set text color
	 */
	public void setTextColor(String color) {
		if (color.equals("Black")) {
			m_color = Color.BLACK;
			m_colorStr = "Black";
		}

		// NEEDS A COLOR
		if (color.equals("Purple")) {
			m_color = Color.MAGENTA;
			m_colorStr = "Purple";
		}

		if (color.equals("White")) {
			m_color = Color.WHITE;
			m_colorStr = "White";
		}

		if (color.equals("Red")) {
			m_color = Color.RED;
			m_colorStr = "Red";
		}

		if (color.equals("Blue")) {
			m_color = Color.BLUE;
			m_colorStr = "Blue";
		}

		if (color.equals("Yellow")) {
			m_color = Color.YELLOW;
			m_colorStr = "Yellow";
		}

		if (color.equals("Pink")) {
			m_color = Color.PINK;
			m_colorStr = "Pink";
		}

		if (color.equals("Orange")) {
			m_color = Color.ORANGE;
			m_colorStr = "Orange";
		}

		if (color.equals("Green")) {
			m_color = Color.GREEN;
			m_colorStr = "Green";
		}

		// Set text color for input area
		SimpleAttributeSet newAttr = new SimpleAttributeSet();
		StyleConstants.setForeground(newAttr, m_color);
		m_inputArea.setCharacterAttributes(newAttr, false);
	}

	/**
	 * Setter method to set text size
	 */
	public void setTextSize(String textSize) {
		m_textSize = Integer.parseInt(textSize);

		// Set text size for input area
		SimpleAttributeSet newAttr = new SimpleAttributeSet();
		StyleConstants.setFontSize(newAttr, m_textSize);
		m_inputArea.setCharacterAttributes(newAttr, false);
	}

	/**
	 * Setter method to set bold or italic
	 */
	public void setBold(boolean bold) {
		m_bold = bold;
		
		// Set bold for input area
		SimpleAttributeSet newAttr = new SimpleAttributeSet();
		StyleConstants.setBold(newAttr, m_bold);
		m_inputArea.setCharacterAttributes(newAttr, false);
	}

	/**
	 * Setter method to set bold or italic
	 */
	public void setItalic(boolean italic) {
		m_italic = italic;
		
		// Set bold for input area
		SimpleAttributeSet newAttr = new SimpleAttributeSet();
		StyleConstants.setItalic(newAttr, m_italic);
		m_inputArea.setCharacterAttributes(newAttr, false);
	}
	/**
	 * Method to submit text from input area
	 */
	public void submitInputText() {
		// Get text from text field
		String text = m_inputArea.getText() + newline;

		// Get underlying document of the ChatView object
		StyledDocument doc = m_currentConvo.getChatConvo();

		// Set attributes
		SimpleAttributeSet viewAttr = new SimpleAttributeSet();
		StyleConstants.setBold(viewAttr, m_bold);
		StyleConstants.setItalic(viewAttr, m_italic);
		StyleConstants.setForeground(viewAttr, m_color);
		StyleConstants.setFontSize(viewAttr, m_textSize);

		// Create StringBuilder to hold entire message, including header
		StringBuilder msgHeader = new StringBuilder();

		// First byte, says message is to be broadcast
		msgHeader.append("1");

		// Decide value for next four bytes
		// Italic
		if (m_italic) {
			msgHeader.append("1");
		} else {
			msgHeader.append("0");
		}

		// Bold
		if (m_bold) {
			msgHeader.append("1");
		} else {
			msgHeader.append("0");
		}

		// Color
		msgHeader.append(getColorByte());

		// Size
		msgHeader.append(Integer.toString(m_textSize));

		String msgSize = Integer.toString((text.length()));

		StringBuilder msgSizeBuilder = new StringBuilder();

		for (int j = 0; j < (10 - msgSize.length()); j++) {
			msgSizeBuilder.append("0");
		}
		msgSizeBuilder.append(msgSize);
		msgSize = msgSizeBuilder.toString();

		msgHeader.append(msgSize);

		// Create message header as string
		String msgHeaderStr = msgHeader.toString();
		System.out.println("I SAY SIZE IS " + msgSize);
		m_currentConvo.sendMessage(msgHeaderStr, text);

		// Insert the text into the client's viewing area
		try {
			doc.insertString(doc.getLength(), text, viewAttr);
		} catch (Exception e) {
			System.out.println(e);
		}

		// Clear text from input area
		m_inputArea.setText("");
	}

	/**
	 * Private helper method to combine two byte arrays
	 * http://stackoverflow.com/
	 * questions/80476/how-to-concatenate-two-arrays-in-java
	 */
	private byte[] combineByteArrays(byte[] b1, byte[] b2) {
		int b1Len = b1.length;
		int b2Len = b2.length;

		byte[] newArray = new byte[b1Len + b2Len];
		System.arraycopy(b1, 0, newArray, 0, b1Len);
		System.arraycopy(b2, 0, newArray, b1Len, b2Len);

		return newArray;
	}

	/**
	 * Private helper method to determine byte value for color
	 */
	private String getColorByte() {
		if (m_colorStr.equals("Black")) {
			return "0";
		}

		// NEEDS A m_COLOR
		if (m_colorStr.equals("Purple")) {
			return "1";
		}

		if (m_colorStr.equals("White")) {
			return "2";
		}

		if (m_colorStr.equals("Red")) {
			return "3";
		}

		if (m_colorStr.equals("Blue")) {
			return "4";
		}

		if (m_colorStr.equals("Yellow")) {
			return "5";
		}

		if (m_colorStr.equals("Pink")) {
			return "6";
		}

		if (m_colorStr.equals("Orange")) {
			return "7";
		}

		if (m_colorStr.equals("Green")) {
			return "8";
		}

		return "0";
	}
}
