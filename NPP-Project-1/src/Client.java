import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter server socket's port");
        int port = Integer.parseInt(clientInput.readLine());

        Socket socket = new Socket ( "localhost", port );

        System.out.println("Server socket: \n" + socket.getInetAddress() + ":" + port);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String player2Name = in.readLine();
        System.out.println(player2Name);

        int round = 1;

        //String serverPrompt;
        //serverPrompt = in.readLine();
        //out.println(serverPrompt);


        while (round <= 4) { // I have no idea why this should be 4

            String userGuess = clientInput.readLine();
            out.println(userGuess); // Send the guess to the server
            round++;
        }
        // exclude name from loop
        // add in readline seperately




    }
}
