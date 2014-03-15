
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.util.Date;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;


// Information gathered from http://www.bouncycastle.org/wiki/display/JA1/BC+Version+2+API
// and https://code.google.com/p/xebia-france/wiki/HowToGenerateaSelfSignedX509CertificateInJava

public class CertificateMaker {
	CertificateMaker()
	{

	}

	public void createKeysAndCertificate(String sc, String ip, String storeName) {
		try {
			// Adds the Bouncy castle provider to java security
			Security.addProvider(new BouncyCastleProvider());

			// Date of start of certificate and expiration date of certificate
			// Starts the day before creation
			Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60
					* 1000);
			// Ends a year from the day of creation
			Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60
					* 60 * 1000);


			// Create the public and private key 
			// Uses the RSA algorithm provided by the Bouncy Castle Library
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
					"RSA", "BC");
			keyPairGenerator.initialize(1024, new SecureRandom());
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			
			// Get public key and get the information about the public key in encoded form 
			PublicKey publicKey = keyPair.getPublic();
			byte[] encodedPK = publicKey.getEncoded();
			SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo(
					ASN1Sequence.getInstance(encodedPK));
			
			
			// Create signature, which in this case will be used to self sign the certificate
			ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withRSA")
					.setProvider("BC").build(keyPair.getPrivate());



			// Create a certificate builder
			// Parameters:
			//		1. name of certificate issuer
			//		2. serial number of certificate
			//		3. the start date of the certificate
			//		4. the expiration date of the certificate
			//		5. the subject of the certificate
			//		6. the public key information that will come with certificate
			X509v3CertificateBuilder v3CertGen = new X509v3CertificateBuilder(
					new X500Name("CN=" + sc), BigInteger.ONE, startDate,
					endDate, new X500Name("CN=" + ip), publicKeyInfo);

			// Create a certificate holder, which will hold the actual certificate
			// Uses the certificate builder above, along with the signature to create a self 
			// signed certificate 
			X509CertificateHolder certHolder = v3CertGen.build(sigGen);
			
			// Create a keystore to hold private key and certificate
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(null, null);
			
			// Create a certificate chain to be passed in as a 
			// parameter when storing the private key in keystore
			X509Certificate[] chain = new X509Certificate[1];
			
			// Get the underlying certificate of the certificate holder and store it in chain
			// Information for this from: 
			// http://stackoverflow.com/questions/6370368/bouncycastle-x509certificateholder-to-x509certificate
			X509Certificate certX = new JcaX509CertificateConverter().setProvider( "BC" ).getCertificate( certHolder );
            chain[0] = certX;
			
            // Place private key into key store, as well as certificate
			ks.setKeyEntry(sc + "/" + ip, keyPair.getPrivate(), new char[] {'p', 'p'}, chain);

			// Keystore name and password
			FileOutputStream fos = new FileOutputStream(storeName);
			char [] password = {'p', 'p'};
			
			// Store keystore
			ks.store(fos, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

