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

        String serverMessage = in.readLine();
        System.out.println(serverMessage);

        String player2 = clientInput.readLine();
        out.println(player2);

        int round = 1;




        while (round <= 3) {

            String guessPrompt;
            guessPrompt = in.readLine();
            System.out.println(guessPrompt);

            String serverPrompt;
            serverPrompt = in.readLine();
            System.out.println(serverPrompt);

            String userGuess = clientInput.readLine();
            out.println(userGuess); // Send the guess to the server

            String winnerPrompt; // announce round winner
            winnerPrompt = in.readLine();
            System.out.println(winnerPrompt);
            round++;
        }

        String finalWinnerPrompt; // announce winner
        finalWinnerPrompt = in.readLine();
        System.out.println(finalWinnerPrompt);
        round++;

    }
}
