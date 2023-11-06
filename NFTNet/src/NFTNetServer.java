import java.io.*;
import java.net.*;


public class NFTNetServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(clientSocket)); // processes request with threading
                thread.start();
            }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            // TODO: Implement communication protocol. Handle client requests, query CoinGecko API, and send responses


            // TODO: Read data from the client
            String clientMessage = in.readLine();
            //System.out.println(clientRequest);
            String[] parts = clientMessage.split("\\|");

            if (parts.length >= 2 && parts[0].equals("REQ")) {
                String requestType = parts[1];

                if (requestType.equals("LIST")) {
                    // Implement logic to query CoinGecko API for NFT list
                    // Simulate a response for demonstration purposes
                    String responseData = "Your NFT list data in JSON format";
                    String responseMessage = "RES|SUCCESS|" + responseData;
                    out.println(responseMessage);
                } else if (requestType.equals("SEARCH")) {
                    // Implement logic to query CoinGecko API for a specific NFT by ID
                    // Simulate a response for demonstration purposes
                    String responseData = "NFT data for the specified ID in JSON format";
                    String responseMessage = "RES|SUCCESS|" + responseData;
                    out.println(responseMessage);
                } else {
                    // Handle unsupported request types
                    String errorMessage = "Unsupported request type";
                    String responseMessage = "RES|ERROR|" + errorMessage;
                    out.println(responseMessage);
                }
            } else {
                // Handle invalid request format
                String errorMessage = "Invalid request format";
                String responseMessage = "RES|ERROR|" + errorMessage;
                out.println(responseMessage);
            }





            clientSocket.close(); // Close the client socket
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
