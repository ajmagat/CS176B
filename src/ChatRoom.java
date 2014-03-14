import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.io.*;

import javax.swing.JCheckBox;

import java.net.*;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import java.util.ArrayList;

/**
 * This represents the overall chat applications
 */

public class ChatRoom extends JFrame {
	private static final int NUM_PIXELS = 600;

	// Create the objects of this chat
	private ChatView newView = new ChatView();
	private ChatBox newBox = new ChatBox(newView);
	private ChatOptions colorOptions = new ChatOptions("Color", newBox);
	private ChatOptions sizeOptions = new ChatOptions("Size", newBox);
	private ChatSubmit btnSubmit = new ChatSubmit(newBox);
	private ChatCheckBox chckbxItalic = new ChatCheckBox("Italic", newBox);
	private ChatCheckBox chckbxBold = new ChatCheckBox("Bold", newBox);
	
	private ArrayList<ChatConvo> m_convoList = new ArrayList<ChatConvo>();
	
	/**
	 * Default Constructor
	 */
	ChatRoom() {
		// Set some box related stuff
		setTitle("ChatRoom");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(694, 600);

		newBox.attachBtn(btnSubmit);
		
		MapBox internalFrame = new MapBox();
		internalFrame.setVisible(true);


		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(newView, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(internalFrame, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(newBox, GroupLayout.PREFERRED_SIZE, 448, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnSubmit, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(22))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(colorOptions, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(sizeOptions, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxItalic, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxBold, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(206))
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
							.addComponent(newBox, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(66)
							.addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		getContentPane().setLayout(groupLayout);
		setVisible(true);
		/*************************************************/

		this.getRootPane().setDefaultButton(btnSubmit.getButton());

		try {
			ChatConvo newConvo = new ChatConvo("169-231-93-247.wireless.ucsb.edu", 12321);
			System.out.println(InetAddress.getLocalHost().getHostName());
			// ChatConvo newConvo = new ChatConvo();
			switchConversation(newConvo);
			
			String test = "555";

			final byte[] inBytes = test.getBytes("UTF-8");
			System.out.println("Size in bytes " + inBytes.length);
			for (byte b : inBytes) {
				System.out.println("Byte: " + b);
			}
			String str = new String(inBytes, "UTF-8");
			System.out.println("Bytes back to string: " + str);
		} catch (IOException e) {
			System.out.println(e);
		}

		this.addWindowListener( new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				closeAllConnections();
				System.exit(0);
			}
			
		});
	}

	/**
	 * Method that will switch to a conversation between two or more people
	 */
	public void switchConversation(ChatConvo newConvo) {
		m_convoList.add(newConvo);
		newView.setChatConvo(newConvo);
		newBox.setChatConvo(newConvo);
	}
	
	public void closeAllConnections()
	{
		for(ChatConvo c : m_convoList)
		{
			c.closeConvo();
		}
	}

}
