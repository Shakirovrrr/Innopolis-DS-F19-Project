package client;


import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClientAPI clientAPI = new ClientAPI();
        clientAPI.commandHandler();
    }


}