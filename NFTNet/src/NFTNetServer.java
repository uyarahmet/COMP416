import java.awt.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.util.HashMap;


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

            clientSocket.setSoTimeout(10000); // set time out if client doesn't respond in 10 seconds

            while (true) {
                // TODO: Implement communication protocol. Handle client requests, query CoinGecko API, and send responses
                // TODO: Read data from the client

                String clientMessage;

                try {
                    clientMessage = in.readLine();
                } catch (SocketTimeoutException e) {
                    // Client did not send a request in 10 seconds
                    System.out.println("Client did not send a request within the timeout. Closing the connection.");
                    break;
                }

                if (clientMessage == null) {
                    break;  // Connection closed by the client
                }

                System.out.println("from client: " + clientMessage);
                String[] parts = clientMessage.split("\\|");

                if (parts.length >= 2 && parts[0].equals("REQ")) {
                    String requestType = parts[1];

                    if (requestType.equals("LIST")) {
                        // Implement logic to query CoinGecko API for NFT list
                        // Simulate a response for demonstration purposes

                        String nftListData = fetchNFTListData();

                        if (nftListData != null) {
                            String responseMessage = "RES|SUCCESS|" + nftListData; // Responding with fetched NFT list data
                            out.println(responseMessage);
                        } else {
                            String errorMessage = "Failed to retrieve NFT list data"; // Handling the case where data retrieval from CoinGecko API failed
                            String responseMessage = "RES|ERROR|" + errorMessage;
                            out.println(responseMessage);
                        }
                    } else if (requestType.equals("SEARCH")) {
                        // Implementing logic to query CoinGecko API for a specific NFT by ID
                        if (parts.length >= 3) {
                            String nftId = parts[2];
                            String nftData = fetchNFTIdData(nftId);

                            if (nftData != null) {
                                String responseMessage = "RES|SUCCESS|" + nftData;
                                out.println(responseMessage);
                            } else {
                                String errorMessage = "Failed to retrieve NFT data for the specified ID";
                                String responseMessage = "RES|ERROR|" + errorMessage;
                                out.println(responseMessage);
                            }
                        } else {
                            // Handling missing NFT ID in the request
                            String errorMessage = "NFT ID is missing in the request";
                            String responseMessage = "RES|ERROR|" + errorMessage;
                            out.println(responseMessage);
                        }
                    } else {
                        // Handling unsupported request types
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Close the client socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String fetchNFTListData() {
        try {
            URL url = new URL("https://api.coingecko.com/api/v3/nfts/list");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            if (status == 200) {
                BufferedReader connectionIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = connectionIn.readLine()) != null) {
                    content.append(inputLine);
                }
                connectionIn.close();
                connection.disconnect();
                return content.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fetchNFTIdData(String nftId) {
        try {
            // Construct the URL with the provided NFT ID
            String apiUrl = "https://api.coingecko.com/api/v3/nfts/" + nftId;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            if (status == 200) {
                BufferedReader connectionIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = connectionIn.readLine()) != null) {
                    content.append(inputLine);
                }
                connectionIn.close();
                connection.disconnect();
                return content.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
