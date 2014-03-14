import javax.swing.text.*;
import javax.net.ssl.*;

import java.net.*;
import java.awt.Color;
import java.io.*;

// This class will take a socket and listen for all incoming messages
public class ChatServerListener implements Runnable {
	private DataInputStream m_inStream;
	private StyledDocument m_chatConvo;

	/**
	 * Default Constructor
	 */
	ChatServerListener() {

	}

	/**
	 * Constructor that takes in a client socket and a StyledDocument
	 * representing current convo
	 */
	ChatServerListener(Socket clientSock, StyledDocument chatConvo) {
		try {
			m_inStream = new DataInputStream(clientSock.getInputStream());
		} catch (IOException e) {
			System.out.println("Problem connecting\n" + e);
		}

		m_chatConvo = chatConvo;
	}

	@Override
	public void run() {
		try {
			while (true) {
				// Wait for message type byte
				byte[] msgType = new byte[1];
				m_inStream.readFully(msgType);
				String str = new String(msgType, "UTF-8");

				System.out.println("The type is " + str);
				// Receiving message
				if (str.equals("1")) {
					// Get the text parameters for this object
					byte[] textParams = new byte[5];
					m_inStream.readFully(textParams);

					String params = new String(textParams, "UTF-8");

					boolean italic = false;
					if ((params.substring(0, 1)).equals("1")) {
						italic = true;
					}

					boolean bold = false;
					if ((params.substring(1, 2)).equals("1")) {
						bold = true;
					}

					Color color = getColorFromCode((params.substring(2, 3)));

					int textSize = Integer.parseInt(params.substring(3, 5));

					// Get the size of the username
					byte[] nmeSize = new byte[2];
					m_inStream.readFully(nmeSize);
					String nmeSizeStr = new String(nmeSize, "UTF-8");
					int nmeSizeInt = Integer.parseInt(nmeSizeStr);

					// Get size of message
					byte[] msgSize = new byte[10];
					m_inStream.readFully(msgSize);
					String sizeStr = new String(msgSize, "UTF-8");
					int sizeInt = Integer.parseInt(sizeStr);

					// Get username
					byte[] name = new byte[nmeSizeInt];
					m_inStream.readFully(name);

					// Get message
					byte[] msg = new byte[sizeInt];
					m_inStream.readFully(msg);

					String userName = new String(name, "UTF-8");
					String finalMsg = new String(msg, "UTF-8");

					finalMsg = userName + finalMsg;

					// Set attributes
					SimpleAttributeSet viewAttr = new SimpleAttributeSet();
					StyleConstants.setBold(viewAttr, bold);
					StyleConstants.setItalic(viewAttr, italic);
					StyleConstants.setForeground(viewAttr, color);
					StyleConstants.setFontSize(viewAttr, textSize);

					m_chatConvo.insertString(m_chatConvo.getLength(), finalMsg,
							viewAttr);
				}

			}
		} catch (IOException e) {
			System.out.println("Error receiving messsage\n" + e);
		} catch (Exception o) {

		} finally {
			try {
				m_inStream.close();
			} catch (Exception e) {

			}
		}
	}

	private Color getColorFromCode(String colorCode) {
		if (colorCode.equals("0")) {
			return Color.BLACK;
		}

		if (colorCode.equals("1")) {
			return Color.MAGENTA;
		}

		if (colorCode.equals("2")) {
			return Color.WHITE;
		}

		if (colorCode.equals("3")) {
			return Color.RED;
		}

		if (colorCode.equals("4")) {
			return Color.BLUE;
		}

		if (colorCode.equals("5")) {
			return Color.YELLOW;
		}

		if (colorCode.equals("6")) {
			return Color.PINK;
		}

		if (colorCode.equals("7")) {
			return Color.ORANGE;
		}

		if (colorCode.equals("8")) {
			return Color.GREEN;
		}

		return Color.black;
	}
}
