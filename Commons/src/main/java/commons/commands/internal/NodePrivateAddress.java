package commons.commands.internal;

import java.net.InetAddress;

public class NodePrivateAddress {
    private InetAddress privateAddress;
    private int portNumber;

    public NodePrivateAddress(InetAddress privateAddress, int portNumber) {
        this.privateAddress = privateAddress;
        this.portNumber = portNumber;
    }

    public InetAddress getPrivateAddress() {
        return privateAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }
}
