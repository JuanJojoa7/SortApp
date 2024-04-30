public class ServerN3 {
    public static void main(String[] args) {
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectAdapter adapter =
            communicator.createObjectAdapterWithEndpoints("SortingFileAdapter", "tcp -h 192.168.1.21 -p 10003");
            com.zeroc.Ice.Object object = new SortFileI();
            adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SortingFile"));
            adapter.activate();
            communicator.waitForShutdown();
        }
    }
}
