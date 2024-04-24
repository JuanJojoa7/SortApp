import settings.src.main.java.Settings;

public class Client {
    public static void main(String[] args) {
        Settings systemConfig = new Settings(3, 10000); // Configurar el número de nodos y el puerto

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimpleOrder: tcp -h localhost -p " + systemConfig.getPort());
            Demo.OrderPrx order = Demo.OrderPrx.checkedCast(base);
            if(order == null) {
                throw new Error("Invalid proxy");
            }
            Random random = new Random();
            int[] numbers = random.ints(20).toArray();

            order.sortList(numbers, systemConfig.getNumNodes());
            
        }
    }
}
