import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.*;
import java.util.Date;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class CertificateMaker {
	CertificateMaker()
	{

	}

	public void createKeysAndCertificate() {
		try {
			// adds the Bouncy castle provider to java security
			Security.addProvider(new BouncyCastleProvider());

			System.out.println("ghiasdf");
			// Date of start of certificate and expiration date of certificate
			Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60
					* 1000);
			Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60
					* 60 * 1000);

			System.out.println("asdf");
			// GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
					"RSA", "BC");
			keyPairGenerator.initialize(1024, new SecureRandom());
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			System.out.println("asdtewtt");
			// Create signature
			ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withRSA")
					.setProvider("BC").build(keyPair.getPrivate());

			// Get public key and public key info
			PublicKey publicKey = keyPair.getPublic();
			byte[] encoded = publicKey.getEncoded();
			SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(
					ASN1Sequence.getInstance(encoded));

			// Create certificate
			X509v3CertificateBuilder v3CertGen = new X509v3CertificateBuilder(
					new X500Name("CN=Test"), BigInteger.ONE, startDate,
					endDate, new X500Name("CN=Test"), subjectPublicKeyInfo);

			X509CertificateHolder certHolder = v3CertGen.build(sigGen);
			PEMWriter pemWriter = new PEMWriter(new FileWriter("keyStore/new.cert"));
			pemWriter.writeObject(certHolder);
			pemWriter.flush();
			pemWriter.close();
			
			PEMWriter pemWriter2 = new PEMWriter(new FileWriter("keyStore/private.key"));
			pemWriter2.writeObject(keyPair.getPrivate());
			pemWriter2.flush();
			pemWriter2.close();
			
			KeyStore ks = KeyStore.getInstance("JKS");
			
			ks.load(null, null);
			
			X509Certificate[] chain = new X509Certificate[1];
			
			X509Certificate certX = new JcaX509CertificateConverter().setProvider( "BC" ).getCertificate( certHolder );
			
           
            CertificateFactory fact = CertificateFactory.getInstance("X.509", "BC");
            ByteArrayInputStream    bIn = new ByteArrayInputStream(certX.getEncoded());
            chain[0] = (X509Certificate)fact.generateCertificate(bIn);

            //chain[0] = certX;
			
			FileInputStream fs = new FileInputStream("keyStore/new.cert");
			BufferedInputStream bs = new BufferedInputStream(fs);
			
			ks.setKeyEntry("myalias", (Key)keyPair.getPrivate(), new char[] {'p', 'p'}, chain);

			FileOutputStream fos = new FileOutputStream("custom_store.jks");
			
			char [] password = {'p', 'p'};
			
			ks.store(fos, password);
		

		}

		catch (Exception e) {
			System.out.println("YO " + e);
		}
	}
}

