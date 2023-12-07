import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPServerThread extends Thread {
    private final String SERVER_ACK_MESSAGE = "server_ack";
    private Socket clientSocket;
    private String line = new String();
    private BufferedReader reader;
    private PrintWriter writer;

    public TCPServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream());
            line = reader.readLine();
            writer.write(SERVER_ACK_MESSAGE);
            writer.flush();
            System.out.println("Client " + clientSocket.getRemoteSocketAddress() + " sent: " + line);
        } catch (IOException e) {
            line = this.getClass().toString(); // Reused String line for getting thread name
            System.out.println("Server Thread. Run. IO Error/ Client " + line + " terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getClass().toString(); // Reused String line for getting thread name
            System.out.println("Server Thread. Run.Client " + line + " Closed");
        } finally {
            try {
                System.out.println("\nClosing the connection");
                if (reader != null) {
                    reader.close();
                    System.out.println("Socket Input Stream Closed");
                }

                if (writer != null) {
                    writer.close();
                    System.out.println("Socket Out Closed");
                }
                if (clientSocket != null) {
                    clientSocket.close();
                    System.out.println("Socket Closed");
                }
            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        } // end finally
    }
}

