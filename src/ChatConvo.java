/*
 * This product includes GeoLite data created by MaxMind, available from
  <a href="http://www.maxmind.com">http://www.maxmind.com</a>.
 */
import javax.swing.JOptionPane;
import javax.swing.text.*;
import javax.net.ssl.*;

import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.jdesktop.swingx.mapviewer.GeoPosition;

/*
 import com.maxmind.geoip.Location;
 import com.maxmind.geoip.LookupService;
 import com.maxmind.geoip.regionName;
 */
/**
 * This class represents the chat conversation
 */
public class ChatConvo {

	// Document that will hold this chat
	private StyleContext m_context;
	private StyledDocument m_chatConvo;

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

	// Traceroute to generate route
	// private Traceroute tracer;

	public List<GeoPosition> geoList;

	/**
	 * Default Constructor
	 */
	ChatConvo() {
		m_context = new StyleContext();
		m_chatConvo = new DefaultStyledDocument(m_context);
		// m_sendSock = null;
		m_sslFactory = null;
		m_sslSendSock = null;
	}

	/**
	 * Constructor that takes in a username for P2P
	 */
	ChatConvo(String username) {
		try {
			// Set style context and document for this convo
			m_context = new StyleContext();
			m_chatConvo = new DefaultStyledDocument(m_context);

			// Create socket factory and socket
			m_sslServerSockFactory = (SSLServerSocketFactory) SSLServerSocketFactory
					.getDefault();
			m_sslServerSock = (SSLServerSocket) m_sslServerSockFactory
					.createServerSocket(50505, 1);

			// Create a thread that will act as the server for P2P connections
			ChatP2PThread p2pThread = new ChatP2PThread(m_sslServerSock, this);
			(new Thread(p2pThread)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor that takes in the address of a server, port number, and
	 * username for multiple chat
	 */
	ChatConvo(String hostname, int port, String username) {
		m_context = new StyleContext();
		m_chatConvo = new DefaultStyledDocument(m_context);

		createSSLSocketConnection(hostname, port);

		try {
			(m_chatConvo).remove(0, (m_chatConvo.getLength()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createSSLSocketConnection(String hostname, int port) {
		try {
			SSLContext context = SSLContext.getInstance("TLS");

			// Create a custom trust manager to handle SSL handshakes
			TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// Not used
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// Not used
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					try {
						// Create a keystore to hold private key and certificate
						KeyStore ks = KeyStore.getInstance("JKS");
						FileInputStream fos = new FileInputStream(
								"custom_store.jks");
						char[] password = { 'p', 'p' };
						ks.load(fos, password);

						String ipAlias = ((chain[0].getSubjectX500Principal())
								.getName());

						ipAlias = ipAlias.substring(3, ipAlias.length());
						// System.out.println("ADSFASDFAFDFADS " + ipAlias);
						Certificate checkCert = ks.getCertificate(ipAlias);

						if (checkCert == null) {
							// Custom button text
							Object[] options = { "Yes, please", "No, thanks" };
							int n = JOptionPane
									.showOptionDialog(
											null,
											"Untrusted Certificate, would you like to proceed with chat?",
											"Warning",
											JOptionPane.YES_NO_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE, null,
											options, options[0]);

							if (n == 0) {
								KeyStore.Entry newEntry = new KeyStore.TrustedCertificateEntry(
										chain[0]);
								ks.setEntry(ipAlias, newEntry, null);
								FileOutputStream ploop = new FileOutputStream(
										"custom_store.jks");
								ks.store(ploop, "pp".toCharArray());
								return;
							}
						} else {
							try {
								System.out
										.println("CHECK THIS CERT DOEEEEEEEEEE");
								chain[0].verify((checkCert.getPublicKey()));
								return;
							} catch (InvalidKeyException ike) {
								throw new CertificateException(
										"Invalid Certificate");
							} catch (Exception e) {

							}

						}
					} catch (Exception e) {
						throw new CertificateException(
								"Error Verifying Certificate");
					}
					throw new CertificateException("Untrusted Certificate");
				}
			};

			// Create custom socket ssl socket factory
			TrustManager[] tmList = new X509TrustManager[1];
			tmList[0] = tm;
			context.init(null, tmList, null);
			m_sslFactory = context.getSocketFactory();

			// Create an SSL socket to send messages
			m_sslSendSock = (SSLSocket) m_sslFactory.createSocket(hostname,
					port);

			/*
			 * //m_sslSendSock.getInetAddress().getHostName(); tracer.trace();
			 * 
			 * // Get routeInfo from tracer, which is a list of IP addresses,
			 * and get the latitude, longitude of every IP using maxMind
			 * database. List<String> route = tracer.getRouteInfo();
			 * 
			 * // Create Maxmind database File database = new
			 * File("/Users/alickxu/Documents/cs176b"); LookupService reader =
			 * new LookupService(database); List<Location> locList = new
			 * ArrayList<Location>(); geoList = new ArrayList<GeoPosition>();
			 * 
			 * // Get list of all locations visited from traceroute for(int i =
			 * 0; i < route.size(); i++) { String routeName = route.get(i);
			 * Location loc = reader.getLocation(routeName); locList.add(loc); }
			 * 
			 * // Using latitude, longitude of every IP, Use MapBox to draw a
			 * route between each IP // Create a list of GeoPositions using list
			 * of Locations for(int j = 0; j < locList.size(); j++) {
			 * geoList.add(new GeoPosition(locList.get(j).latitude,
			 * locList.get(j).longitude)); }
			 */
			// Get the output stream of the data socket
			m_outStream = new DataOutputStream(m_sslSendSock.getOutputStream());
			// Create a thread that will listen for messages from the server
			ChatServerListener listener = new ChatServerListener(m_sslSendSock,
					m_chatConvo);

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
	 * Setter method for data output stream
	 */
	public void setOutputStream(DataOutputStream oStream) {
		m_outStream = oStream;
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

			// m_inStream.close();
			// m_outStream.close();
			// m_sslSendSock.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
