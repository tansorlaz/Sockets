package dawbird;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InterfazChat extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnClient;
	private JButton btnServer;
	private JButton btnDisconect;
	private JButton btnDisconect_1;
	private JButton btnSend;
	private final static int PORT = 5005;
	private final static String SERVER = "127.0.0.1";
	private Socket clientSocket;
	private ServerSocket serverSocket;
	private boolean isClient = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazChat frame = new InterfazChat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InterfazChat() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 517, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(12, 209, 320, 33);
		contentPane.add(textField);
		textField.setColumns(10);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(12, 12, 320, 185);
		contentPane.add(textArea);

		btnClient = new JButton("Client");
		btnClient.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					isClient = true;
					System.out.println("Client -> Start");
					clientSocket = new Socket(SERVER, PORT);

					BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintStream output = new PrintStream(clientSocket.getOutputStream());

					ReadingFromStream2 r = new ReadingFromStream2(input, textArea);
					r.start();

					String message = "Hello, server!";
					output.println(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				enableEdition(true);
			}
		});
		btnClient.setBounds(410, 79, 99, 30);
		contentPane.add(btnClient);

		btnDisconect = new JButton("Disconect");
		btnDisconect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				disconnectServerOrClient();
			}

		});
		btnClient.setBounds(365, 12, 117, 25);
		contentPane.add(btnClient);

		btnServer = new JButton("Server");
		btnServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					serverSocket = new ServerSocket(PORT);
					
					System.out.println("Server started");
					Socket client = serverSocket.accept();
					client.setSoLinger(true, 10);
					
					System.out.println("connected to client: " + client.getInetAddress());
					clientSocket = client;
					
					BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintStream output = new PrintStream(clientSocket.getOutputStream());

					ReadingFromStream2 r = new ReadingFromStream2(input, textArea);
					r.start();

					String message = "Hello, client!";
					output.println(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				enableEdition(true);
			}
		});
		btnServer.setBounds(365, 61, 117, 25);
		contentPane.add(btnServer);

		btnDisconect_1 = new JButton("Disconect");
		btnDisconect_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				disconnectServerOrClient();
			}
		});
		btnDisconect_1.setBounds(365, 142, 117, 25);
		contentPane.add(btnDisconect_1);

		btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String send = textField.getText();
				textArea.append(send + "\n");
				textField.setText("");

				if (clientSocket != null && !clientSocket.isClosed()) {
					try {
						PrintStream output = new PrintStream(clientSocket.getOutputStream());
						output.println(send);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else if (serverSocket != null && !serverSocket.isClosed()) {
					try {
						Socket client = serverSocket.accept();
						PrintStream output = new PrintStream(client.getOutputStream());
						output.println("ME:" + send);
					} catch (IOException e3) {
						e3.printStackTrace();
					}
				}
			}
		});
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnSend.doClick();
				}
			}
		});
		btnSend.setBounds(365, 213, 117, 25);
		contentPane.add(btnSend);
	}

	private void disconnectServerOrClient() {
		try {
			if (isClient) {
				if (clientSocket != null && !clientSocket.isClosed()) {
					serverSocket.close();
					clientSocket.close();
					System.exit(0);
				}
			} else {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
					System.exit(0);

				}

			}
		} catch (IOException | NullPointerException e) {
			System.out.println("Program closed");
		}

	}

	private void enableEdition(boolean b) {
		if (b) {
			btnServer.setEnabled(false);
			btnClient.setEnabled(false);
			btnDisconect_1.setEnabled(true);
			btnSend.setEnabled(true);
		} else {
			btnServer.setEnabled(true);
			btnClient.setEnabled(true);
			btnDisconect_1.setEnabled(false);
			btnSend.setEnabled(false);
		}
	}
}
