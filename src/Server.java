import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.cert.Certificate;

import javax.swing.JOptionPane;

/**
 * Class to act as the server
 */
class Server {
	public static void main(String[] args) {
		try {
			// Check to see if keystore exists, creates it if it doesn't
			File jks = new File("server_store.jks");

			if (!jks.exists()) {
				CertificateMaker newCert = new CertificateMaker();
				newCert.createKeysAndCertificate("server", InetAddress.getLocalHost().getHostName(), "server_store.jks");
			}

			// Set path to keystore
			System.setProperty("javax.net.ssl.keyStore", "custom_store.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "pp");
			System.setProperty("javax.net.ssl.trustStore", "custom_store.jks");
			System.setProperty("javax.net.ssl.trustStorePasword", "pp");
			

			// Create server
            ChatServer newServer = new ChatServer(50505);
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
