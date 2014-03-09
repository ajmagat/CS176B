import java.io.File;

class Application {
	public static void main(String[] args) {
		
		// Somehow run test that looks for a valid certificate.
		// If there is none, create one using the CertificateMaker class
		// Else go straight to the chat room
		
		// Open up chat room
		ChatRoom test = new ChatRoom();
	/*	CertificateMaker newCert = new CertificateMaker();
		newCert.createKeysAndCertificate();
		System.setProperty("javax.net.ssl.keyStore","/Home");
		File f = new File("src/Main2.java");
		System.out.println(f.getAbsolutePath());
		if(f.exists())
		{
			System.out.println("I EXIST");
		}
		else
		{
			System.out.println("I DON'T EXIST");
		}*/
	}
}
