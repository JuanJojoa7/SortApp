public class Settings {
    private int numNodes;
    private int port;

    public SystemConfig(int numNodes, int port) {
        this.numNodes = numNodes;
        this.port = port;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public void setNumNodes(int numNodes) {
        this.numNodes = numNodes;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
