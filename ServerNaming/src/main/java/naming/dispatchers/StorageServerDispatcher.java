package naming.dispatchers;

import commons.commands.Command;
import commons.commands.general.ErrorAck;
import commons.routines.IORoutines;
import naming.Node;
import naming.NodeStorage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.UUID;

import static naming.dispatchers.Constants.TIMER_SLEEP_TIME;

public class StorageServerDispatcher extends Thread {
    private class NodeTimer implements Comparable<NodeTimer> {
        private UUID nodeId;
        private long lastCall;

        public NodeTimer(UUID nodeId, long lastCall) {
            this.nodeId = nodeId;
            this.lastCall = lastCall;
        }

        public UUID getNodeId() {
            return nodeId;
        }

        public long getLastCall() {
            return lastCall;
        }

        public void setLastCall(long lastCall) {
            this.lastCall = lastCall;
        }

        @Override
        public int compareTo(NodeTimer nodeTimer) {
            if (this.getLastCall() == nodeTimer.getLastCall()) { return 0 }
            if (this.getLastCall() < nodeTimer.getLastCall()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private int listeningPort;
    private ServerSocket server;
    private Dispatcher dispatcher;

    private PriorityQueue<NodeTimer> servers;

    public StorageServerDispatcher(int listeningPort, Dispatcher dispatcher) {
        this.listeningPort = listeningPort;
        this.dispatcher = dispatcher;
        this.servers = new PriorityQueue<>();

//        new Thread(() -> checkServers()).start();
    }

    private void checkServers() {
        try {
            while (true) {
                sleep(Constants.TIMER_SLEEP_TIME);
                int n = servers.size();
                for (NodeTimer node : servers) {
                    if (node.getLastCall() + Constants.WAITING_TIME > new Date().getTime()) {
                        dispatcher.removeNode(node.getNodeId());
                    };
                }
            }
        } catch (InterruptedException ex) {
            System.err.println("Thread interrupted.");
        }

    }

    private void serve() throws IOException {
        while (true) {
            Socket conn = server.accept();
            new Thread(() -> dispatch(conn)).start();
        }
    }

    private void dispatch(Socket conn) {
        try {
            Command command = IORoutines.receiveSignal(conn);
            Command ack = new ErrorAck();

        } catch (IOException ex) {
            System.err.println("Connection reset.");
        } catch (ClassNotFoundException | ClassCastException ex) {
            ex.printStackTrace();
            System.err.println("Unable to dispatch the command.");
        }
    }
}
