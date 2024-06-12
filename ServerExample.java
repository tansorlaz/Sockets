package dawbird;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * ServerExample class, to create a simple example to connect two computers via a TCPIP connection
 */
public class ServerExample {

    /* We keep the port in a constant */
    private final static int PORT = 5005;

    public static void main(String[] args) {

        try {
            Scanner tec = new Scanner(System.in);
            // Server Socket to wait for network requests
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started");

            // Client Socket
            Socket client;
            System.out.println("Server waiting for a client...");
            client = server.accept();
            System.out.println("connected to client: " + client.getInetAddress());

            // an input reader to read from the socket
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            // to print data out
            PrintStream output = new PrintStream(client.getOutputStream());
            boolean exit = false;
            ReadingFromStream r = new ReadingFromStream(input);
            
            r.start();
            do {
                // now we read a line from the keyboard
                System.out.print("Server -> type a sentence to send to the client: ");
                String line2 = tec.nextLine();

                if (line2.equals("exit")) {
                    exit = true;
                    System.out.println("Server -> client has closed the connection.");
                    // Interrupt the reading thread
                    r.interrupt();
                } else {
                    output.println(line2);
                    System.out.println("Server -> sent the line to the client: " + line2);
                }
            } while (!exit);

            // close connection
            client.close();
            server.close();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
