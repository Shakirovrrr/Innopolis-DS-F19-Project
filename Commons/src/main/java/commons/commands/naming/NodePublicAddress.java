package commons.commands.internal;

import java.net.InetAddress;

public class NodePublicAddress {
    private InetAddress publicAddress;
    private int portNumber;

    public NodePublicAddress(InetAddress publicAddress, int portNumber) {
        this.publicAddress = publicAddress;
        this.portNumber = portNumber;
    }

    public InetAddress getPublicAddress() {
        return publicAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }
}
