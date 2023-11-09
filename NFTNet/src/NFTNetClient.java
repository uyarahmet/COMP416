import netscape.javascript.JSObject;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class NFTNetClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Server socket: " + socket.getInetAddress() + ":" + socket.getPort());

            boolean flag = true;

            while (flag) {
                System.out.println("Choose a request type:");
                System.out.println("1. List NFTs (Enter 'list')");
                System.out.println("2. Get NFT Details (Enter 'details')");
                System.out.println("3. Exit (Enter 'exit')");
                String requestType = scanner.nextLine();

                if ("list".equalsIgnoreCase(requestType)) {
                    String listRequest = "REQ|LIST";
                    out.println(listRequest);

                    processServerResponse(in.readLine());
                } else if ("details".equalsIgnoreCase(requestType)) {
                    System.out.print("Enter the NFT ID: ");
                    String nftId = scanner.nextLine();

                    String searchRequest = "REQ|SEARCH|" + nftId;
                    out.println(searchRequest);

                    processServerResponse(in.readLine());
                } else if ("exit".equalsIgnoreCase(requestType)) {
                    // Exit the loop if the user enters 'exit'
                    flag = false;
                } else {
                    System.out.println("Invalid request type. Please enter 'list', 'details', or 'exit'.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processServerResponse(String serverResponse) {
        if (serverResponse != null) {
            String[] responseParts = serverResponse.split("\\|");
            String responseType = responseParts[0];
            String responseStatus = responseParts[1];

            if (responseType.equals("RES") && responseStatus.equals("SUCCESS")) {
                // The response is successful; parse the JSON data
                String responseData = responseParts[2];
                parseAndPrintData(responseData);
            } else if (responseType.equals("RES") && responseStatus.equals("ERROR")) {
                // The server responded with an error message
                String errorMessage = responseParts[2];
                System.out.println("Server error: " + errorMessage);
            }
        } else {
            System.out.println("No response from the server. Exiting...");
        }
    }

    private static void parseAndPrintData(String responseData) {
        System.out.println("Parsed NFT Information:");

        //System.out.println(responseData);

        String[] keyValuePairs = responseData.split(",");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            if (entry.length >= 2) {
                String key = entry[0].trim().replace("\"", "");
                if(key.equalsIgnoreCase("asset_platform_id") ||
                key.equalsIgnoreCase("id") || key.equalsIgnoreCase("name")) {
                    String value = entry[1].trim().replace("\"", "");
                    System.out.println(key + ": " + value);
                }else{
                    //System.out.println(pair);
                }
            }

        }
    }






}
