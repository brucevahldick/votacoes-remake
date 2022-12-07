package controller;

import model.Vote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class Client {

    Map<String, String> inputInfo;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUser;

    public void conectar() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ip");
        String ip = scanner.nextLine();
        System.out.println("Enter your name");
        String clientName = scanner.nextLine();
        Socket socket = new Socket(ip, 80);
        Client voteThread = new Client(socket, clientName);
        voteThread.listenForData();
    }

    public Client(Socket socket, String clientUser) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUser = clientUser;
        } catch (IOException e) {
            closeConnection(this.socket, bufferedWriter, bufferedReader);
            e.printStackTrace();
        }
    }

    public void sendData(Vote vote) {
        try {
            bufferedWriter.write(clientUser);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            while (socket.isConnected()) {
                bufferedWriter.write(clientUser+";"+ vote.isParecer());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeConnection(socket, bufferedWriter, bufferedReader);
            e.printStackTrace();
        }
    }

    public void listenForData() {
        new Thread(() -> {
            String messageFromServer;

            while (socket.isConnected()) {
                try {
                    messageFromServer = bufferedReader.readLine();
                    // show message from server
                    System.out.println(messageFromServer);
                } catch (IOException e) {
                    closeConnection(socket, bufferedWriter, bufferedReader);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void closeConnection(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null && socket != null && bufferedWriter != null) {
                bufferedReader.close();
                bufferedWriter.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
