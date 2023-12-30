package socketFTP;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUI extends JFrame {
    private JTextField ipField, portField, filePathField;
    private JButton connectButton;
    private JTextArea displayArea;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientGUI() {
        setTitle("FTP Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Server IP:"));
        ipField = new JTextField();
        inputPanel.add(ipField);

        inputPanel.add(new JLabel("Port:(Server port is set to 1000"));
        portField = new JTextField();
        inputPanel.add(portField);

        inputPanel.add(new JLabel("File Path:"));
        filePathField = new JTextField();
        inputPanel.add(filePathField);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        inputPanel.add(connectButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void connectToServer() {
        try {
            String serverIP = ipField.getText();
            int serverPort = Integer.parseInt(portField.getText());
            String filePath = filePathField.getText();

            socket = new Socket(serverIP, serverPort);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Send command to retrieve file
            writer.println("RETRIEVE " + filePath);

            // Read response from server and display in TextArea
            String response;
            StringBuilder data = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                data.append(response + "\n");
            }
            displayArea.setText(data.toString());
            // Close connections
            reader.close();
            writer.close();
            socket.close();

        } catch (IOException ex) {
            displayArea.setText("Error: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}
