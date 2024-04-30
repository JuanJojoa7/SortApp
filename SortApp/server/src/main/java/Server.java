import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the port number: ");
        int port = input.nextInt();
        System.out.println("Server is running on port " + port);
        input.close();
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectAdapter adapter =
            communicator.createObjectAdapterWithEndpoints("SortingFileAdapter", "tcp -h localhost -p " + port);
            com.zeroc.Ice.Object object = new SortFileI();
            adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SortingFile"));
            adapter.activate();
            communicator.waitForShutdown();
        }
    }
}
