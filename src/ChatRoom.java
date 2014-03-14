import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JCheckBox;

import java.net.*;

import javax.swing.JPanel;
import javax.swing.JInternalFrame;

import java.util.ArrayList;

import javax.swing.JButton;

/**
 * This represents the overall chat applications
 */

public class ChatRoom extends JFrame implements ActionListener {
	private static final int NUM_PIXELS = 600;
	private static final String SERVER_IP = "169-231-93-247.wireless.ucsb.edu";
	private static final int DEFAULT_PORT = 50505;

	// Create the objects of this chat
	private ChatView newView = new ChatView();
	private ChatBox newBox = new ChatBox(newView);
	private ChatOptions colorOptions = new ChatOptions("Color", newBox);
	private ChatOptions sizeOptions = new ChatOptions("Size", newBox);
	private ChatSubmit btnSubmit = new ChatSubmit(newBox);
	private ChatCheckBox chckbxItalic = new ChatCheckBox("Italic", newBox);
	private ChatCheckBox chckbxBold = new ChatCheckBox("Bold", newBox);
	private MapBox internalFrame = new MapBox();
	private ArrayList<ChatConvo> m_convoList = new ArrayList<ChatConvo>();
	private String m_username = null;
	private String m_roomType = null;
	private ChatConvo m_p2pConvo = null;
	
	/**
	 * Default Constructor
	 */
	ChatRoom() {

	}

	/**
	 * Constructor that takes in a username and roomType
	 */
	ChatRoom(String username, String roomType) {
		m_username = username;
		m_roomType = roomType;

		// Set some box related stuff
		setTitle("ChatRoom");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(694, 600);

		newBox.attachBtn(btnSubmit);
		newBox.turnOff();

		internalFrame.setVisible(true);
		JButton btnNewButton = new JButton();
		if(m_roomType.equalsIgnoreCase("p2p"))
		{
			// Some sort of button
			// Will create a chatconvo
			btnNewButton = new JButton("Connect to IP");
			btnNewButton.addActionListener(this);
			m_p2pConvo = new ChatConvo(m_username);
		}

		if(m_roomType.equalsIgnoreCase("mc"))
		{
			// Some sort of button
	//		ChatConvo newConvo = new ChatConvo("169-231-93-247.wireless.ucsb.edu", 12321, "bob");
		//	switchConversation(newConvo);
			btnNewButton = new JButton("Join Room");
			btnNewButton.addActionListener(this);
		}
		

		
		JButton btnNewButton_1 = new JButton("New button");

		/*************************************************/
        /****          GUI STUFF DO NOT TOUCH         ****/
		/*************************************************/
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(newView, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(internalFrame, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(colorOptions, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(sizeOptions, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxItalic, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxBold, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
							.addComponent(btnNewButton_1))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(newBox, GroupLayout.PREFERRED_SIZE, 448, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(18)
									.addComponent(btnSubmit, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnNewButton)))))
					.addGap(22))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(internalFrame, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(newView, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(25)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(colorOptions, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(sizeOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(26)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(chckbxItalic, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxBold, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(29)
							.addComponent(newBox, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnNewButton_1)
							.addGap(18)
							.addComponent(btnNewButton)
							.addGap(18)
							.addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		getContentPane().setLayout(groupLayout);
		setVisible(true);
		/*************************************************/
        /****          GUI STUFF DO NOT TOUCH         ****/
		/*************************************************/

		this.getRootPane().setDefaultButton(btnSubmit.getButton());
/*
		try {
			ChatConvo newConvo = new ChatConvo("169-231-93-247.wireless.ucsb.edu", 12321, "bob");
			System.out.println(InetAddress.getLocalHost().getHostName());
			switchConversation(newConvo);
			
		} catch (IOException e) {
			System.out.println(e);
		}
*/
	/*	this.addWindowListener( new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				closeAllConnections();
				System.exit(0);
			}
			
		});*/
	}

	/**
	 * Method that will switch to a conversation between two or more people
	 */
	public void switchConversation(ChatConvo newConvo) {
		m_convoList.add(newConvo);
		newView.setChatConvo(newConvo);
		newBox.setChatConvo(newConvo);
	}
	
	public void makeP2PConvo(String hostname)
	{
		try
		{
		    m_p2pConvo.createSSLSocketConnection(hostname, DEFAULT_PORT);
		}
		catch (Exception e)
		{
			System.out.println("Error connecting");
			e.printStackTrace();
		}
	}
	
	public void makeMCConvo(int portNumber)
	{
		try
		{
			ChatConvo newConvo = new ChatConvo(SERVER_IP, portNumber, m_username);
			switchConversation(newConvo);
			newBox.turnOn();
		}
		catch (Exception e)
		{
			System.out.println("Error joining room");
			e.printStackTrace();
		}
	}
	
	public void closeAllConnections()
	{
		for(ChatConvo c : m_convoList)
		{
			c.closeConvo();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(m_roomType.equals("p2p"))
		{
			String ipAddr = JOptionPane.showInputDialog(null, "Enter Target IP: ");
			makeP2PConvo(ipAddr);
		}
		if(m_roomType.equals("mc"))
		{
			String message = JOptionPane.showInputDialog(null, "Enter Room Number (8000 - 10000): ");
			int roomNum = Integer.parseInt(message);
			makeMCConvo(roomNum);
		}
	}
}
