import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {


    public static void main(String[] args) throws IOException {

        BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter welcoming socket's port");
        int port = Integer.parseInt(serverInput.readLine());

        ServerSocket server = new ServerSocket(port);
        Socket clientSocket = server.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("Client socket: \n" + clientSocket.getRemoteSocketAddress());
        System.out.println("Player 1, please enter your name:");

        String player1;
        String player2;

        player1 = serverInput.readLine();

        out.println("Player 2, you will be playing with " + player1 + ", please enter your name");

        player2 = in.readLine();

        System.out.println("You are playing with " + player2);


        int times = 3;
        int player1_score = 0;
        int player2_score = 0;

        int x = (int) (Math.random() * 256);
        int y = (int) (Math.random() * 256);

        while(times > 0){

            System.out.println(player1 + ", please enter your x and y guesses, comma separated:");

            // add waiting for player 1 guess
            out.println("Waiting for player 1 guess");


            String[] player1Guess = serverInput.readLine().split(", ");
            int player1X = Integer.parseInt(player1Guess[0]);
            int player1Y = Integer.parseInt(player1Guess[1]);

            System.out.println("Waiting for player 2 guess. . .");
            out.println(player2 + ", please enter your x and y guesses, comma separated:");



            String player2Guess = in.readLine();
            String[] player2GuessArr = player2Guess.split(", ");
            int player2X = Integer.parseInt(player2GuessArr[0]);
            int player2Y = Integer.parseInt(player2GuessArr[1]);

            double distPlayer1 = Math.sqrt(Math.pow(x - player1X, 2) + Math.pow(y - player1Y, 2));
            double distPlayer2 = Math.sqrt(Math.pow(x - player2X, 2) + Math.pow(y - player2Y, 2));

            String winner = "Both players";
            if (distPlayer1 < distPlayer2) {
                player1_score++;
                winner = player1;
            } else if (distPlayer2 < distPlayer1) {
                player2_score++;
                winner = player2;
            }

            System.out.println("Winner for round " + (4-times) + " is " + winner);
            out.println("Winner for round " + (4-times) + " is " + winner);

            times--;
        }

        String winner = (player1_score > player2_score) ? player1 : player2;
        System.out.println("Game Winner is " + winner);
        out.println("Game Winner is " + winner);

        out.close();
        in.close();
        clientSocket.close();
        server.close();

    }


}
