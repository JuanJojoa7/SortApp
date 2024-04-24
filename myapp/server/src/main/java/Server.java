import settings.src.main.java.Settings;
public class Server {
    public static void main(String[] args) {
        int numNodes = 3; // Configurar el número de nodos
        int port = 10000; // Configurar el puerto
        
        Settings systemConfig = new Settings(numNodes, port);

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("SimpleOrderAdapter", "tcp -h localhost -p " + systemConfig.getPort());
            com.zeroc.Ice.Object object = new MyappI();
            adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SimpleOrder"));
            adapter.activate();
            // Mensaje de inicio
            System.out.println("Servidor iniciado. Esperando clientes...");
            communicator.waitForShutdown();
        }
    }
}
