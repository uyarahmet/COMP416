import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NFTNetClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner scanner = new Scanner(System.in);

        System.out.println("Server socket: " + socket.getInetAddress() + ":" + socket.getPort()); // 1. display information

        // TODO: Write the humane prompt, make the user select from list.


        // Sending a "LIST" request to the server
        // String listRequest = "REQ|LIST";
        // out.println(listRequest);

        // Receiving and processing the server's response
        System.out.println("Choose a request type:");
        System.out.println("1. List NFTs (Enter 'list')");
        System.out.println("2. Get NFT Details (Enter 'details')");
        String requestType = scanner.nextLine();

        if (requestType.equalsIgnoreCase("list")) {
            // Send a "LIST" request to the server
            String listRequest = "REQ|LIST";
            out.println(listRequest);
        } else if (requestType.equalsIgnoreCase("details")) {
            // Prompt the user to enter an NFT ID for details
            System.out.print("Enter the NFT ID: ");
            String nftId = scanner.nextLine();

            // Send a "SEARCH" request to the server with the NFT ID
            String searchRequest = "REQ|SEARCH|" + nftId;
            out.println(searchRequest);
        } else {
            System.out.println("Invalid request type. Please enter 'list' or 'details'.");
        }

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
