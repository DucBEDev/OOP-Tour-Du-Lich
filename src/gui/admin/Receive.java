package gui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receive extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JTextArea chatArea;
    private static JTextField messageField;
    private static DataOutputStream dataOutputStream;

    public Receive() {
        super(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        chatArea.setCaretColor(Color.WHITE);
        chatArea.setBackground(Color.WHITE);
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        messageField = new JTextField(100);
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        messageField.setPreferredSize(new Dimension(300, 40));
        messageField.setBackground(new Color(240, 240, 240));
        messageField.setForeground(Color.BLACK);
        messageField.setBorder(new EmptyBorder(0, 10, 0, 0)); 

        JButton sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 40));
        sendButton.setBackground(new Color(0, 0, 139));
        sendButton.setForeground(Color.WHITE);
        sendButton.setMaximumSize(new Dimension(100, 30));
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(1234)) {
            	chatArea.setForeground(Color.BLACK);
                Socket socket = serverSocket.accept();
                chatArea.append("Đã kết nối.\n");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                sendButton.addActionListener(e -> sendMessage());
                messageField.addActionListener(e -> sendMessage());

                while (true) {
                    String message = dataInputStream.readUTF();
                    chatArea.append("KH: " + message + "\n");
                    if ("exit".equalsIgnoreCase(message)) {
                        chatArea.append("Khách hàng đã ngắt kết nối.\n");
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