package naming.dispatchers;

import com.google.gson.internal.$Gson$Preconditions;
import commons.commands.Command;
import commons.commands.general.ErrorAck;
import commons.commands.internal.FetchFiles;
import commons.commands.internal.Heartbeat;
import commons.commands.internal.RegisterNode;
import commons.routines.IORoutines;
import naming.Node;
import naming.NodeStorage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;

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
            if (this.getLastCall() == nodeTimer.getLastCall()) { return 0; }
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

    private Map<UUID, Long> serversHeartbeats;

    public StorageServerDispatcher(int listeningPort, Dispatcher dispatcher) {
        this.listeningPort = listeningPort;
        this.dispatcher = dispatcher;
//        this.servers = new PriorityQueue<>();
        this.serversHeartbeats = new HashMap<>();

//        new Thread(() -> checkServers()).start();
    }

    private void checkServers() {
        try {
            while (true) {
                sleep(Constants.TIMER_SLEEP_TIME);
                for (UUID nodeId : serversHeartbeats.keySet()) {
                    if (serversHeartbeats.get(nodeId) + Constants.WAITING_TIME > new Date().getTime()) {
                        dispatcher.removeNode(nodeId);
                        serversHeartbeats.remove(nodeId);
                    } else {
                        break;
                    }
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

            if (command instanceof RegisterNode) {
                UUID nodeId = ((RegisterNode) command).getNodeId();
                List<UUID> files = Arrays.asList(((RegisterNode) command).getFiles());
                InetAddress publicAddress = ((RegisterNode) command).getPublicAddress();
                InetAddress localAddress = ((RegisterNode) command).getLocalAddress();

                dispatcher.registerNode(nodeId, files, publicAddress, localAddress);
                serversHeartbeats.put(nodeId, new Date().getTime());

            } else if (command instanceof FetchFiles) {


            } else if (command instanceof Heartbeat) {

            } else {
                Command ack = new ErrorAck();
                IORoutines.sendSignal(conn, ack);
            }

        } catch (IOException ex) {
            System.err.println("Connection reset.");
        } catch (ClassNotFoundException | ClassCastException ex) {
            ex.printStackTrace();
            System.err.println("Unable to dispatch the command.");
        }
    }
}
