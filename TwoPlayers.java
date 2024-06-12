package dawbird;



import javax.swing.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintStream;

import java.net.ServerSocket;

import java.net.Socket;



public class TwoPlayers extends JFrame {

    private static final String SERVER_IP = "localhost";

    private static final int SERVER_PORT = 5005;



    private JButton btnPlayer1;

    private JButton btnPlayer2;

    boolean isServer = false;

    private boolean isClient = false;



    

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                TwoPlayers frame = new TwoPlayers();

                frame.setVisible(true);

            } catch (Exception e) {

                e.printStackTrace();

            }

        });

    }



    public TwoPlayers() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(100, 100, 450, 300);

        JPanel contentPane = new JPanel();

        setContentPane(contentPane);

        contentPane.setLayout(null);



        btnPlayer1 = new JButton("Player 1 (Server)");

        btnPlayer1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                btnPlayer1.setEnabled(false);

                isServer = true;

                checkGameStart();

            }

        });

        btnPlayer1.setBounds(63, 87, 150, 29);

        contentPane.add(btnPlayer1);



        btnPlayer2 = new JButton("Player 2 (Client)");

        btnPlayer2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                btnPlayer2.setEnabled(false);

                isClient = true;

                checkGameStart();

            }

        });

        btnPlayer2.setBounds(237, 87, 150, 29);

        contentPane.add(btnPlayer2);

    }



    private void checkGameStart() {

        if (isServer && isClient) {

            JOptionPane.showMessageDialog(this, "No se puede iniciar como servidor y cliente al mismo tiempo.", "Error", JOptionPane.ERROR_MESSAGE);

            btnPlayer1.setEnabled(true);

            btnPlayer2.setEnabled(true);

        } else if (isServer) {

            waitForClientConnection();

        } else if (isClient) {

            connectToServer();

        }

    }



    private void waitForClientConnection() {

        new Thread(() -> {

            try {

                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

                Socket clientSocket = serverSocket.accept();

                PrintStream output = new PrintStream(clientSocket.getOutputStream());

                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                openGame(true, output, input);

                handleGame(output, input);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }).start();

    }



    private void connectToServer() {

        try {

            Socket socket = new Socket(SERVER_IP, SERVER_PORT);

            PrintStream output = new PrintStream(socket.getOutputStream());

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            openGame(false, output, input);

            handleGame(output, input);

        } catch (IOException ex) {

            ex.printStackTrace();

        }

    }



    private void openGame(boolean isPlayer1, PrintStream out, BufferedReader in) {

        SwingUtilities.invokeLater(() -> {

            juego_1 frame = new juego_1(this, out, in, isPlayer1); // Pasa el tipo de jugador

            frame.setVisible(true);

        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }









    private void handleGame(PrintStream out, BufferedReader in) {

        new Thread(() -> {

            try {

                while (true) {

                    if (in.ready()) {

                        String message = in.readLine();

                        if (message.equals("lose")) {

                            JOptionPane.showMessageDialog(this, "¡Has ganado!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);

                            break;

                        } else if (message.equals("win")) {

                            JOptionPane.showMessageDialog(this, "¡Has perdido!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                            break;

                        }

                    }

                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }).start();

    }



}