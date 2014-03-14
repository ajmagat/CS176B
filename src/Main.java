import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

class Application {
	public static void main(String[] args) {
		try {
			//Check to see if keystore exists, creates it if it doesn't
			File jks = new File("custom_store.jks");

			if (!jks.exists()) {
				CertificateMaker newCert = new CertificateMaker();
				newCert.createKeysAndCertificate();
			}
			
			// Set path to keystore
			System.setProperty("javax.net.ssl.keyStore", "custom_store.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "pp");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Open up chat room
		ChatRoom test = new ChatRoom();
	}
}
