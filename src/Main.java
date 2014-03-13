import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

class Application {
	public static void main(String[] args) {
		try {
			// Set path to keystore
			System.setProperty("javax.net.ssl.keyStore", "custom_store.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "pp");
System.out.println("HEY");
			//Check to see if certificate exists
			File cert = new File("keyStore/new.cert");
			File priv = new File("keyStore/private.key");
			if (!cert.exists() || !priv.exists()) {
				cert.delete();
				priv.delete();
				CertificateMaker newCert = new CertificateMaker();
				newCert.createKeysAndCertificate();
			}
			System.out.println("LOLOL");
		/*	KeyStore ks = KeyStore.getInstance("JKS");
			
			ks.load(null, null);
			
			FileInputStream fs = new FileInputStream("src/new.cert");
			BufferedInputStream bs = new BufferedInputStream(fs);
			
			ks.setKeyEntry("alias", priv, new char[] {'a', 's', 'd', 'f'}, null);

	//		ks.set
			FileOutputStream fos = new FileOutputStream("newKeyStoreName")
		//	ks.store*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Open up chat room
		ChatRoom test = new ChatRoom();
		System.out.println("YO");
	}
}
