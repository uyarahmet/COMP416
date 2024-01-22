import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int TCP_PORT = 4443;
    private static final int SSL_PORT = 4444;

    private static final String TRUST_STORE_NAME = "client-truststore.jks";
    private static final String TRUST_STORE_PASSWORD = "truststorepassword";

    public static void main(String[] args) {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Choose a service: 1 for SSL, 2 for TCP");
            int choice = Integer.parseInt(consoleInput.readLine());

            //for(int i=1; i<=5; i++) {
            //    long startTime = System.currentTimeMillis();

                if (choice == 1) {
                    // SSL
                    try {
                        // Load the client's truststore
                        System.setProperty("javax.net.ssl.trustStore", TRUST_STORE_NAME);
                        System.setProperty("javax.net.ssl.trustStorePassword", TRUST_STORE_PASSWORD);

                        // Load the server's certificate into the truststore
                        KeyStore truststore = KeyStore.getInstance("JKS");
                        try (InputStream in = new FileInputStream("server_crt.crt")) {
                            truststore.load(null, TRUST_STORE_PASSWORD.toCharArray());
                            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(in);
                            truststore.setCertificateEntry("server-certificate", certificate);
                        }

                        // Create an SSL context with the custom truststore
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        trustManagerFactory.init(truststore);
                        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

                        // Set the custom SSL context
                        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(SERVER_ADDRESS, SSL_PORT);
                        sslSocket.startHandshake(); // start the handshake
                        performSSLEcho(sslSocket);
                    } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException |
                             KeyManagementException e) {
                        throw new RuntimeException(e);
                    }
                } else if (choice == 2) {
                    // TCP
                    try (Socket tcpSocket = new Socket(SERVER_ADDRESS, TCP_PORT)) {
                        performEcho(tcpSocket);
                    }
                } else {
                    System.out.println("Invalid choice. Please choose 1 for SSL or 2 for TCP.");
                }

               // long endTime = System.currentTimeMillis();
               // long delay = endTime - startTime;
                //System.out.println("Execution " + i + " Delay: " + delay + " ms");

            //}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void performEcho(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a message for echo:");
        String message = consoleInput.readLine();

        // Send the message to the server
        writer.println(message);

        // Receive and print the echo response from the server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
    }

    private static void performSSLEcho(SSLSocket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a message for echo:");
        String message = consoleInput.readLine();

        // Send the message to the server
        writer.println(message);

        // Receive and print the echo response from the server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
    }
}
