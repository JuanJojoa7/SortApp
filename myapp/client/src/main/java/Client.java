import java.util.Random;

public class Client {
    public static void main(String[] args) {
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimpleOrder: tcp -h localhost -p 10000");
            Demo.OrderPrx order = Demo.OrderPrx.checkedCast(base);
            if(printer == null) {
                throw new Error("Invalid proxy");
            }
            Random random = new Random();
            int[] numbers = random.ints(20).toArray();

            order.sortList(numbers, 2);
            
        }
    }
}
