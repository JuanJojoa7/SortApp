import java.util.Scanner;

public class Client {

    public static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SortingFile:tcp -h 192.168.39.144 -p 10000");
            Sorting.SortFilePrx sortProxy = Sorting.SortFilePrx.checkedCast(base);
            if(sortProxy == null) {
                throw new Error("Invalid proxy");
            }

            sortProxy.register();

            /**
            int option;
            do {
                option = showMenu();
                switch (option) {
                    case 1:
                        System.out.println("Creating file...");

                        boolean fileCreated = sortProxy.createFile();

                        if(fileCreated) {
                            System.out.println("File created at server.");
                        } else {
                            System.out.println("File already exists at server.");
                        }
                        break;
                    case 2:
                            
                        break;
                    case 3:
                        sortProxy.divideFile(1);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } while(option != 4);
            */
        }
    }

    private static int showMenu() {
        System.out.println("1. Create file");
        System.out.println("2. Sort file using 1 server");
        System.out.println("3. Sort file using multiple servers");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");

        return input.nextInt();
    }
}
