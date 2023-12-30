package socketFTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.w3c.dom.ls.LSOutput;


public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1000);
            System.out.println("FTP Server started...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String command;
                while ((command = reader.readLine()) != null) {
                    if (command.startsWith("RETRIEVE")) {
                        String filename = command.split(" ")[1];
                        File file = new File(filename);
                        try {
                            if (!file.exists()) {
                                writer.println("File not found");
                                break; 
                            }

                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String data;
                            while ((data = br.readLine()) != null) {
                                writer.println(data);
                            }
                            br.close();
                            break;
                        } catch (IOException e) {
                            writer.println("ERROR: File cannot be read");
                            break; // Exit loop if error occurs while reading file
                        }
                    }
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
