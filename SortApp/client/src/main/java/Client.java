import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static Scanner input = new Scanner(System.in);
    private static final String FILE_NAME = "server/src/main/resources/randomStrings.txt";

    public static void main(String[] args) {
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SortingFile:tcp -h 192.168.1.21 -p 10000");
            Sorting.SortFilePrx sortProxy = Sorting.SortFilePrx.checkedCast(base);
            if(sortProxy == null) {
                throw new Error("Invalid proxy");
            }

            System.out.println(sortProxy.register());

            sortProxy.waitForAllClients();
            System.out.println("All clients connected");

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

    private static List<String[]> divideFile(int parts) {
        // Divide file implementation
        List<String> stringList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by the comma delimiter
                String[] divs = line.split(",");
                for (String div : divs) {
                    stringList.add(div.trim()); // Trim any leading/trailing spaces
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String[]> Nlist = new ArrayList<>();
        int divs = stringList.size()/parts;
        for (int i = 0; i < divs; i++) {
            for(int j = 0; j < divs; j++){
                Nlist.get(i)[j] = stringList.get(j+(divs*i)); 
            }
        }

        return Nlist;
    }

    private static String[] divideMerge(int parts){
        List<String[]> fileList = divideFile(parts);

        //Thread mandando cada lista a cada server


        return null;
    }
}
