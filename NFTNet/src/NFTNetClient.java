import java.io.*;
import java.net.Socket;

public class NFTNetClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("Server socket: " + socket.getInetAddress() + ":" + socket.getPort()); // 1. display information

        // Sending a "LIST" request to the server
        String listRequest = "REQ|LIST";
        out.println(listRequest);

        // Receiving and processing the server's response
        String serverResponse = in.readLine();
        String[] responseParts = serverResponse.split("\\|");
        String responseType = responseParts[0];
        String responseStatus = responseParts[1];

        if (responseType.equals("RES") && responseStatus.equals("SUCCESS")) {
            // The response is successful; parse the JSON data
            String responseData = responseParts[2];
            System.out.println("Received data: " + responseData);
        } else if (responseType.equals("RES") && responseStatus.equals("ERROR")) {
            // The server responded with an error message
            String errorMessage = responseParts[2];
            System.out.println("Server error: " + errorMessage);
        }
        // Implement the client-side code to send requests and receive responses
    }
}
