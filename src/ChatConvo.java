import javax.swing.text.*;
import javax.net.ssl.*;

import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;

import java.net.*;
import java.security.cert.*;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.*;

/**
 * This class represents the chat conversation
 */
public class ChatConvo {
	// Document that will hold this chat
	private StyleContext m_context;
	private StyledDocument m_chatConvo;

	// TCP socket representing outgoing connection
//	private Socket m_sendSock;

	// SSL socket representing outgoing connection
	private SSLSocket m_sslSendSock;
	
	private SSLSocketFactory m_sslFactory;
	
	// Output stream to send response to outgoing connection
	private DataOutputStream m_outStream;

	// Input stream to receive response from connection
	private DataInputStream m_inStream;

	private SSLServerSocketFactory m_sslServerSockFactory;
	private SSLServerSocket m_sslServerSock;
	
	private static final int HEADER_SIZE = 10;
	
	

	/**
	 * Default Constructor
	 */
	ChatConvo() {
		m_context = new StyleContext();
		m_chatConvo = new DefaultStyledDocument(m_context);
	//	m_sendSock = null;
		m_sslFactory = null;
		m_sslSendSock = null;
	}

	/**
	 * Constructor that takes in an IP for P2P
	 */
	ChatConvo(InetAddress addr, String username)
	{
		m_sslServerSockFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		m_sslServerSock = (SSLServerSocket) m_sslServerSockFactory.createServerSocket(41334);		
	}

	/**
	 * Constructor that takes in the address of a server and port number for multiple chat
	 */
	ChatConvo(String hostname, int port, String username) {
		m_context = new StyleContext();
		m_chatConvo = new DefaultStyledDocument(m_context);

		try {
			SSLContext context = SSLContext.getInstance("TLS");
			
			
			TrustManager tm = new X509TrustManager() {
			    @Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// Not used
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					try{
					// Create a keystore to hold private key and certificate
					KeyStore ks = KeyStore.getInstance("JKS");
					FileInputStream fos = new FileInputStream("custom_store.jks");
					char [] password = {'p', 'p'};
					ks.load(fos, password);
					
					if(ks.size() == 1)
					{
						//System.out.println()
						System.out.println("ADDDDDINNG TO TRUST STORE");
						KeyStore.Entry newEntry = new KeyStore.TrustedCertificateEntry(chain[0]);
						ks.setEntry("superChatServer", newEntry, null);
						FileOutputStream ploop = new FileOutputStream("custom_store.jks");
						ks.store(ploop, "pp".toCharArray());
						return;
					}
					else
					{
						try
						{
							System.out.println("CHECK THIS CERT DOEEEEEEEEEE");
							Certificate checkCert = ks.getCertificate("superChatServer");
						    chain[0].verify( ( checkCert.getPublicKey() ) );
						    return;
						}
						catch (InvalidKeyException ike)
						{
							throw new CertificateException("Invalid Certificate");
						}
						catch (Exception e)
						{
							
						}
						
					}
						//System.out.println("IN THIS HOOOEEEE AGAIN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

						
						//chain[0].verify();

					// Keystore name and password
				//	FileOutputStream fos = new FileOutputStream("custom_store.jks");
			//		char [] password = {'p', 'p'};
					
					// Store keystore
				//	ks.store(fos, password);


					}
					catch (Exception e)
					{
						throw new CertificateException("Error Verifying Certificate"); 
					}
					
					throw new CertificateException("Untrusted Certificate");
				}


				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// Not used
					return null;
				}
			};
			
			TrustManager [] tmList = new X509TrustManager[1];
			tmList[0] = tm;
			
			context.init(null, tmList, null);
			//m_sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			m_sslFactory = context.getSocketFactory();
			
			m_sslSendSock = (SSLSocket) m_sslFactory.createSocket(hostname, port);
			System.out.println("NIOOOOOOOOOO");
			m_outStream = new DataOutputStream(m_sslSendSock.getOutputStream());

			ChatServerListener listener = new ChatServerListener(m_sslSendSock, m_chatConvo);
			
			(new Thread(listener)).start();
		} catch (UnknownHostException uhe) {
			System.out.println("Cannot find host " + hostname);
		} catch (IOException ioe) {
			System.out.println(ioe);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	/**
	 * Getter method to return chatDoc
	 */
	public StyledDocument getChatConvo() {
		return m_chatConvo;
	}

	/**
	 * Method to send message to server
	 */
	public void sendMessage(String header, String message) {
		// Split up the message according to self defined protocol (10 byte
		// length limit)
		// String[] messages = splitMessage(message);

		// Create string builder to hold the message
		StringBuilder messageBuilder = new StringBuilder();

		// Create message
		// for(int i = 0; i < messages.length; i++)
		// {
		messageBuilder.append(header);
		// messageBuilder.append(messages[i]);
		messageBuilder.append(message);
		// }

		// Make the message a string
		String msgStr = messageBuilder.toString();

		// Turn string into a byte array and send
		try {
			byte[] msgBytes = msgStr.getBytes("UTF-8");
			m_outStream.write(msgBytes);
		} catch (IOException e) {
			System.out.println("Unsupported encoding\n " + e);
		}
	}

	/**
	 * Private method to split message depending on size
	 */
	private String[] splitMessage(String message) {
		// Get length of message
		int msgLen = message.length();

		// Find how many times message should be split
		int msgSplits = msgLen / HEADER_SIZE;
		if (msgLen % HEADER_SIZE != 0) {
			msgSplits++;
		}

		// Create array of strings to hold message partitions
		String[] messagePartitions = new String[msgSplits];

		// Fill up array
		int begin = 0;
		int end = msgLen;
		for (int i = 0; i < msgSplits; i++) {
			if (i < (msgSplits - 1)) {
				messagePartitions[i] = message.substring(begin, end);
				begin += msgLen;
				end += msgLen;
			} else {
				messagePartitions[i] = message.substring(begin);
			}
		}

		return messagePartitions;
	}

	/**
	 * Method to close connection
	 */
	public void closeConvo() {
		try {
			StringBuilder messageBuilder = new StringBuilder();
			
			messageBuilder.append("9");
			// Make the message a string
			String msgStr = messageBuilder.toString();

			// Turn string into a byte array and send
			try {
				byte[] msgBytes = msgStr.getBytes("UTF-8");
				m_outStream.write(msgBytes);
			} catch (IOException e) {
				System.out.println("Unsupported encoding\n " + e);
				return;
			}
			
		//	m_inStream.close();
		//	m_outStream.close();
		//	m_sslSendSock.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
