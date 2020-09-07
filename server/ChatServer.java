package chatserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private static final int portNumber = 4444; // Ports lower than 1000 are likely to be in use

    private int serverPort;
    private List<ClientThread> clients;  

    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }

    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber); //Create instance of server with assigned port
        server.startServer();
    }

    private void startServer(){
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null; // Initialize server socket
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Unable to listen to port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("Server port: " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("Accepts: " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Was unable to accept Client on: "+serverPort);
            }
        }
    }
    
    public List<ClientThread> getClients(){
        return clients;
    }
}