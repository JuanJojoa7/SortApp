public class Server {
    public static void main(String[] args) {
        int TCPPORT = 10000;
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("SimpleOrderAdapter", "tcp -h localhost -p " + TCPPORT);
            com.zeroc.Ice.Object object = new MyappI();
            adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SimpleOrder"));
            adapter.activate();
            // Mensaje de inicio
            System.out.println("Servidor iniciado. Esperando clientes...");
            communicator.waitForShutdown();
        }
    }
}
