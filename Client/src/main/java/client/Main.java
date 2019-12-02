package client;

import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        if (args.length >= 1) {
//            String namingAddress = args[0];
        String namingAddress = "3.125.123.71";
            System.out.println("Using naming address " + namingAddress);

            ClientAPI clientAPI = new ClientAPI(namingAddress);
            clientAPI.commandHandler();
//        } else {
//            System.out.println("Wrong number of arguments");
//            System.exit(0);
//        }
    }


}