package dawbird;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/** ClientExample class, to create a simple example to connect two computer via a 
 * TCPIP connection
 *  */
public class ClientExample {

    // Port 5005
    private final static int PORT = 5005;

    private final static String SERVER = "54.236.52.58";

    public static void main(String[] args) {
        Socket socket; // Socket para la comunicación cliente-servidor        
        try {
            System.out.println("Client -> Start");
            socket = new Socket(SERVER, PORT); // Abre el socket

            // Para leer del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Para escribir al servidor
            PrintStream output = new PrintStream(socket.getOutputStream());
            // Para leer del usuario (teclado)
            Scanner tec = new Scanner(System.in);

            ReadingFromStream r = new ReadingFromStream(input);
            r.start();

            while (true) {
                System.out.println("Client -> Write a sentence to send:");
                String line = tec.nextLine();

                // Enviar la línea al servidor
                output.println(line);

                // Leer una línea del servidor
                if (r != null) System.out.println("Client -> received message: " + r);

                if (line.equalsIgnoreCase("exit")) {
                    break; // Salir del bucle si el usuario escribe 'exit'
                }
            }

            System.out.println("Client -> se cerró");
            socket.close();
            tec.close(); // Cerrar el scanner
        } catch (IOException ex) {
            System.err.println("Client -> " + ex.getMessage());
        }
    }
}

