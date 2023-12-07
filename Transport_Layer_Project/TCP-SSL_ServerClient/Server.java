import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class Server {
    private final int tcpPort;
    private final int sslPort;
    private ServerSocket tcpServerSocket;

    private final String SERVER_KEYSTORE_FILE = "keystore.jks";
    private final String SERVER_KEYSTORE_PASSWORD = "storepass";
    private final String SERVER_KEY_PASSWORD = "keypass";
    private SSLServerSocket sslServerSocket;
    private SSLServerSocketFactory sslServerSocketFactory;

    public Server(int tcpPort, int sslPort) {
        this.tcpPort = tcpPort;
        this.sslPort = sslPort;
        startServers();
    }

    private void startServers() {
        // Start TCP server
        try {
            tcpServerSocket = new ServerSocket(tcpPort);
            System.out.println("TCP server is up and running on port " + tcpPort);
            new Thread(() -> {
                while (true) {
                    listenAndAcceptTCP();
                }
            }).start();

            // Start SSL server
            /*
            Instance of SSL protocol with TLS variance
             */
            SSLContext sc = SSLContext.getInstance("TLS");

            /*
            Key management of the server
             */
            char ksPass[] = SERVER_KEYSTORE_PASSWORD.toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(SERVER_KEYSTORE_FILE), ksPass);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, SERVER_KEY_PASSWORD.toCharArray());
            sc.init(kmf.getKeyManagers(), null, null);

            /*
            SSL socket factory which creates SSLSockets
             */
            sslServerSocketFactory = sc.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(sslPort);

            System.out.println("SSL server is up and running on port " + sslPort);

            new Thread(() -> {
                while (true) {
                    listenAndAcceptSSL();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private void listenAndAcceptTCP() {
        Socket clientSocket;
        try {
            clientSocket = tcpServerSocket.accept();
            System.out.println("A connection was established with a client on the address of " + clientSocket.getRemoteSocketAddress());
            TCPServerThread tcpServerThread = new TCPServerThread(clientSocket);
            tcpServerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("TCP Server Class. Connection establishment error inside listen and accept function");
        }
    }

    private void listenAndAcceptSSL() {
        SSLSocket sslSocket;
        try {
            sslSocket = (SSLSocket) sslServerSocket.accept();
            System.out.println("An SSL connection was established with a client on the address of " + sslSocket.getRemoteSocketAddress());
            SSLServerThread st = new SSLServerThread(sslSocket);
            st.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SSL Server Class. Connection establishment error inside listen and accept function");
        }
    }
    public static void main(String[] args){
        Server s = new Server(4443, 4444);
    }
}
