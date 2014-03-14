import javax.swing.text.*;
import javax.net.ssl.*;

import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;

import java.net.*;
import java.security.cert.*;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.*;

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
    
    /**
     * Default Constructor
     */
    ChatConvo() {
        m_context = new StyleContext();
        m_chatConvo = new DefaultStyledDocument(m_context);
    //  m_sendSock = null;
        m_sslFactory = null;
        m_sslSendSock = null;
    }

    /**
     * Constructor that takes in a username for P2P
     */
    ChatConvo(String username)
    {
        try
        {
            // Set style context and document for this convo
            m_context = new StyleContext();
            m_chatConvo = new DefaultStyledDocument(m_context);

            // Create socket factory and socket
            m_sslServerSockFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            m_sslServerSock = (SSLServerSocket) m_sslServerSockFactory.createServerSocket(50505, 1);

            // Create a thread that will act as the server for P2P connections
            ChatP2PThread p2pThread = new ChatP2PThread(m_sslServerSock, this);
            (new Thread(p2pThread)).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Constructor that takes in the address of a server and port number for multiple chat
     */
    ChatConvo(String hostname, int port, String username) {
        m_context = new StyleContext();
        m_chatConvo = new DefaultStyledDocument(m_context);

        createSSLSocketConnection(hostname, port);

        // Create a thread that will listen for messages from the server
        ChatServerListener listener = new ChatServerListener(m_sslSendSock, m_chatConvo);
            
        (new Thread(listener)).start();

                    try{
                        (m_chatConvo).remove(0, (m_chatConvo.getLength()));
                    }
                    catch (Exception e)
                    {

                    }
    }

    public void createSSLSocketConnection(String hostname, int port)
    {
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
                    try{
                        // Create a keystore to hold private key and certificate
                        KeyStore ks = KeyStore.getInstance("JKS");
                        FileInputStream fos = new FileInputStream("custom_store.jks");
                        char [] password = {'p', 'p'};
                        ks.load(fos, password);
                    
                        String ipAlias = ((chain[0].getSubjectX500Principal()).getName());
                        ipAlias = ipAlias.substring(3, apAlias.length() );

                        Certificate checkCert = ks.getCertificate(ipAlias);

                        if(checkCert == null)
                        {
                            //System.out.println()
                            System.out.println("ADDDDDINNG TO TRUST STORE");
                            KeyStore.Entry newEntry = new KeyStore.TrustedCertificateEntry(chain[0]);
                            ks.setEntry(ipAlias, newEntry, null);
                            FileOutputStream ploop = new FileOutputStream("custom_store.jks");
                            ks.store(ploop, "pp".toCharArray());
                            return;
                        }
                        else
                        {
                            try
                            {
                                System.out.println("CHECK THIS CERT DOEEEEEEEEEE");
                                chain[0].verify( ( checkCert.getPublicKey() ) );
                                return;
                            }
                            catch (InvalidKeyException ike)
                            {
                                throw new CertificateException("Invalid Certificate");
                            }
                            catch (Exception e)
                            {
                            
                            }
                        
                        }
                    }
                    catch (Exception e)
                    {
                        throw new CertificateException("Error Verifying Certificate"); 
                    }
                    throw new CertificateException("Untrusted Certificate");
                }
            };
            
            // Create custom socket ssl socket factory
            TrustManager [] tmList = new X509TrustManager[1];
            tmList[0] = tm;
            context.init(null, tmList, null);
            m_sslFactory = context.getSocketFactory();

            // Create an SSL socket to send messages            
            m_sslSendSock = (SSLSocket) m_sslFactory.createSocket(hostname, port);

            // Get the output stream of the data socket
            m_outStream = new DataOutputStream(m_sslSendSock.getOutputStream());

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
            
        //  m_inStream.close();
        //  m_outStream.close();
        //  m_sslSendSock.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
