package chatserver.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private static final String host = "localhost";
	private static final int portNumber = 4444;

	private String userName;
	private String serverHost;
	private int serverPort;

	public static void main(String[] args) {
		String readName = null; // temp storage used to read username
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input username:");

		// Insures username is not equal to null or is blank
		while (readName == null || readName.trim().equals("")) {
			readName = scan.nextLine();
			if (readName.trim().equals("")) {
				System.out.println("Invalid. Please enter again:");
			}
		}

		Client client = new Client(readName, host, portNumber); // Initialize new client
		client.startClient(scan);
	}

	private Client(String userName, String host, int portNumber) {
		this.userName = userName;
		this.serverHost = host;
		this.serverPort = portNumber;
	}

	private void startClient(Scanner scan) {
		try {
			Socket socket = new Socket(serverHost, serverPort);
			Thread.sleep(1000); // waiting for network communicating.

			ServerThread serverThread = new ServerThread(socket, userName);
			Thread serverAccessThread = new Thread(serverThread);
			serverAccessThread.start();
			while (serverAccessThread.isAlive()) {
				if (scan.hasNextLine()) {
					serverThread.addNextMessage(scan.nextLine());
				} else {
					Thread.sleep(200);
				}
			}
		} catch (IOException ex) {
			System.err.println("Unable to connect!");
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			System.out.println("Interrupted Connection!");
		}
	}
}