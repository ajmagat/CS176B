import javax.net.ssl.*;

import java.net.*;
import java.io.*;

import javax.net.ssl.*;

public class ChatP2PThread implements Runnable {
	public DataOutputStream m_outStream;
	private DataInputStream m_inStream;
	private SSLServerSocket m_sSock;
	private ChatConvo m_convo;
	private static final int MAX_CONNECTIONS = 1;

	/**
	 * Default Constructor
	 */
	public ChatP2PThread() {
		m_outStream = null;
		m_inStream = null;
		m_sSock = null;
		m_convo = null;
	}

	/**
	 * Constructor that takes in a server socket and a chat convo
	 */
	public ChatP2PThread(SSLServerSocket ss, ChatConvo convo) {
		m_outStream = null;
		m_inStream = null;
		m_sSock = ss;
		m_convo = convo;
	}

	@Override
	public void run() {
		int connection_count = 0;

		// Run for life of server socket
		while (true) {
			// Create a null socket
			Socket s = null;
			try {
				// Wait for a connection and accept
				s = m_sSock.accept();
				if (connection_count >= MAX_CONNECTIONS) {
					// TODO tell user we can't accept them

					// Close socket
					s.close();
				}

				// Create a thread that will listen for messages from the other
				// client
				ChatServerListener listener = new ChatServerListener(s,
						m_convo.getChatConvo());
				(new Thread(listener)).start();

				// Set the convos output stream
				m_convo.setOutputStream(new DataOutputStream(s
						.getOutputStream()));

				// Clear any text in m_convo
				// (m_convo.getChatConvo()).remove(0,
				// (m_convo.getChatConvo().getLength()));

			} catch (Exception e) {
				System.out.println("Connection cannot be established\n" + e);
			}
 
		}
	}
}