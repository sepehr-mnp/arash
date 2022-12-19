package org.example;

import java.io.*;
import java.net.*;

// Server class
class Server {
    public static void main(String[] args)
    {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                        + client.getInetAddress()
                        .getHostAddress());

                // create a new thread object
                ClientHandler clientSock
                        = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try {

                // get the outputstream of client
                out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                // get the inputstream of client
                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {

                    System.out.printf(
                            " Sent from the client: %s\n",
                            line);
                    if(line.equals("Hi there!"))
                    {
                        out.println("Hi Client.");
                    }else if(line.equals("How are you?")){
                        out.println("Still working , Thanks!");
                    }else if(line.equals("My name is NAME")){
                        out.println("Nice to meet you NAME, my name is WHATEVER");
                    }else if(line.equals("Nice to meet you too, I Have to go, How can I turn you off?")){
                        out.println("Aww! could you please stay more, I just turned on.");
                    }else if(line.equals("I'm sorry I can't!")){
                        out.println("Okay, You can turn Me off by sending EXIT message.");
                    }else if(line.equals("EXIT")){
                        out.println("Bye friend. Sever turned off...");
                        break;
                    }else{
                        out.println("say again! couldn't understand.");
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
