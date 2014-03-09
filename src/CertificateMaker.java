import java.io.FileWriter;
import java.math.BigInteger;
import java.security.*;
import java.util.Date;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
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
			System.out.println("hey");
			System.out.println(certHolder);
			System.out.println(keyPair.getPublic());
			System.out.println(keyPair.getPrivate());
			PEMWriter pemWriter = new PEMWriter(new FileWriter("new.cert"));
			pemWriter.writeObject(certHolder);
			pemWriter.flush();
			pemWriter.close();

		}

		catch (Exception e) {
			System.out.println("YO " + e);
		}
	}
}
