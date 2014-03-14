import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

import javax.swing.JOptionPane;

class Application {
	public static void main(String[] args) {
		try {
			
			String message = JOptionPane.showInputDialog(null, "P2P or Multiple Chat");
			String username = JOptionPane.showInputDialog(null, "Enter User Name");
		
			//Check to see if keystore exists, creates it if it doesn't
			File jks = new File("custom_store.jks");

			if (!jks.exists()) {
				CertificateMaker newCert = new CertificateMaker();
				newCert.createKeysAndCertificate();
			}
			
			// Set path to keystore
			System.setProperty("javax.net.ssl.keyStore", "custom_store.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "pp");
			System.setProperty("javax.net.ssl.trustStore", "custom_store.jks");
			System.setProperty("javax.net.ssl.trustStorePasword", "pp");
			
			// Open up chat room
			if(message != null)
			{
				if(message.equalsIgnoreCase("p2p"))
				{
					String ip = JOptionPane.showInputDialog(null, "Enter Invite IP");
				//	ChatRoom test = new ChatRoom();	
				}
				
				if(message.equalsIgnoreCase("multiple chat"))
				{
					String roomNumber = JOptionPane.showInputDialog(null, "Enter Room Number (8000 - 10000)");
				}

			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
}
