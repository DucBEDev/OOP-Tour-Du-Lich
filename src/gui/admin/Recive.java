package gui.admin;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Recive extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea chatArea;
    private JTextField messageField;
    private DataOutputStream dataOutputStream;

    public Recive() {
        setTitle("Customer Service - Server Chat");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setCaretColor(Color.WHITE); // Đặt màu trỏ chuột là trắng
        chatArea.setBackground(new Color(30, 30, 30));
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        messageField = new JTextField(100);
        messageField.setEditable(true);
        messageField.setCaretColor(Color.WHITE); // Đặt màu trỏ chuột là trắng
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        messageField.setBackground(new Color(50, 50, 50));
        messageField.setForeground(Color.WHITE);

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.WHITE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // Server socket logic
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(1234)) {
                chatArea.append("Server is listening on port 1234...\n");
                Socket socket = serverSocket.accept();
                chatArea.append("Client connected.\n");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                sendButton.addActionListener(e -> sendMessage());
                messageField.addActionListener(e -> sendMessage());

                while (true) {
                    String message = dataInputStream.readUTF();
                    chatArea.append("Client: " + message + "\n");
                    if ("exit".equalsIgnoreCase(message)) {
                        chatArea.append("Client has disconnected.\n");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                chatArea.append("Me: " + message + "\n");
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                messageField.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
