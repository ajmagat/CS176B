import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.cert.Certificate;

import javax.swing.JOptionPane;

class Application {
	public static void main(String[] args) {
		try {
			// Prompts user for type of chat
			String message = JOptionPane.showInputDialog(null, "P2P or Multiple Chat");

			// Returns null if user chooses nothing
			if(message == null)
			{
				return;
			}
			
			if(message.equalsIgnoreCase("p2p") || message.equalsIgnoreCase("multiple chat"))
			{
				//Check to see if keystore exists, creates it if it doesn't
				File jks = new File("custom_store.jks");

				if (!jks.exists()) {
					CertificateMaker newCert = new CertificateMaker();
					newCert.createKeysAndCertificate("client", InetAddress.getLocalHost().getHostName(), "custom_store.jks");
				}

				// Set path to keystore
				System.setProperty("javax.net.ssl.keyStore", "custom_store.jks");
				System.setProperty("javax.net.ssl.keyStorePassword", "pp");
				System.setProperty("javax.net.ssl.trustStore", "custom_store.jks");
				System.setProperty("javax.net.ssl.trustStorePasword", "pp");
			

				if(message.equalsIgnoreCase("p2p"))
				{
					// Open up designated chat room
					String username = JOptionPane.showInputDialog(null, "Enter User Name");

					if(username == null)
					{
						return;
					}

					ChatRoom p2pChatRoom = new ChatRoom(username, "p2p");	
				}
				
				if(message.equalsIgnoreCase("multiple chat"))
				{	
					String username = JOptionPane.showInputDialog(null, "Enter User Name");

					if(username == null)
					{
						return;
					}

					ChatRoom mcChatRoom = new ChatRoom(username, "mc");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
