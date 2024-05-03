import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

public class Client {
    // Constants
    public static Scanner input = new Scanner(System.in);
    private static final String FILE_NAME = "server/src/main/resources/randomStrings.txt";
    private static final int LIMIT = 85000;
    private static boolean localServers = false;
    private static long startTime;
    private static FileGenerator fileGenerator = new FileGenerator(FILE_NAME);
    private static LocalMergeSort localMergeSort = new LocalMergeSort();

    public static void main(String[] args) {
        // Array of ports
        List<Integer> ports = new ArrayList<>();
        List<String> ips = new ArrayList<>();

        String answer;

        // Connection to the servers
        System.out.print("¿Deseas crear servidores locales? (s/n): ");
        answer = input.nextLine();

        // Create threads
        List<Thread> serverThreads = new ArrayList<>();

        if (answer.equalsIgnoreCase("s")) {
            localServers = true;
            System.out.print("¿Cuántos servidores deseas crear?: ");
            int n = input.nextInt();
            input.nextLine();
            System.out.println("Creando " + n + " servidores...");
            for (int i = 0; i < n; i++) {
                int port = 10000 + i;
                ports.add(port);
                Thread thread = new Thread(() -> {
                    try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
                        com.zeroc.Ice.ObjectAdapter adapter =
                        communicator.createObjectAdapterWithEndpoints("SortingFileAdapter", "tcp -h localhost -p " + port);
                        com.zeroc.Ice.Object object = new SortFileI();
                        adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SortingFile"));
                        adapter.activate();
                        communicator.waitForShutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
                serverThreads.add(thread);
            }
        } else {
            do {
                System.out.print("¿Deseas agregar un servidor? (s/n): ");
                answer = input.nextLine();
                if (answer.equalsIgnoreCase("s")) {
                    System.out.println("Ingresa la dirección IP del servidor: ");
                    ips.add(input.nextLine());
                    System.out.print("Ingresa el puerto del servidor: ");
                    ports.add(input.nextInt());
                    input.nextLine();
                }
            } while (!answer.equalsIgnoreCase("n"));
        }

        setFileGenerator(fileGenerator);

        // Sort list of strings with no threads
        System.out.println("Ordenando archivo sin hilos");   

        String[] strings = serializeFile(FILE_NAME).toArray(new String[0]);
        startTime = System.currentTimeMillis();
        localMergeSort.mergeSort(strings);
        System.out.println("Archivo ordenado. Tiempo utilizado: " + (System.currentTimeMillis() - startTime) + "ms");

        // Save the sorted strings to a file
        saveFile(strings, 1);

        System.out.println("Archivo guardado.");

        // Sort list of strings with threads
        System.out.println("Ordenando archivo con hilos...");

        System.out.println("Dividiendo partes...");

        List<String[]> lists = divideFile();

        List<String[]> dividedStrings = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();

        BlockingQueue<String[]> queue = new LinkedBlockingQueue<>(lists);

        startTime = System.currentTimeMillis();

        for (int port : ports) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
                        com.zeroc.Ice.ObjectPrx base;
                        if (localServers) {
                            base = communicator.stringToProxy("SortingFile:tcp -h localhost -p " + port);
                        } else {
                            base = communicator.stringToProxy("SortingFile:tcp -h " + ips.get(ports.indexOf(port)) + " -p " + port);
                        }

                        Sorting.SortFilePrx sortProxy = Sorting.SortFilePrx.checkedCast(base);

                        if(sortProxy == null) {
                            throw new Error("Invalid proxy");
                        }

                        while (true) {
                            String[] stringsToSort = queue.poll();
                            if (stringsToSort == null) {
                                break;
                            }

                            String[] sortedStrings = sortProxy.sortFileList(stringsToSort);

                            dividedStrings.add(sortedStrings);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Error al unir los hilos");
            }
        }

        // Merge the divided strings
        String[] sortedStrings = sortFileList(dividedStrings);

        // Print the time elapsed
        System.out.println("Archivo ordenado. Tiempo utilizado: " + (System.currentTimeMillis() - startTime) + "ms");

        // Save the sorted strings to a file
        saveFile(sortedStrings, 2);

        System.out.println("Archivo guardado.");

        System.exit(0);
    }

    public static void setFileGenerator(FileGenerator fileGenerator) {
        System.out.print("¿Deseas crear el archivo de números? (s/n): ");
        String answer = input.nextLine();
        if (answer.equalsIgnoreCase("s")) {
            boolean response = fileGenerator.createFile();
            if (response) {
                System.out.println("Archivo creado exitosamente");
            } else {
                System.out.println("El archivo ya existe");
            }
        }

        // Delete files
        File file1 = new File("server/src/main/resources/sortedStrings1.txt");
        File file2 = new File("server/src/main/resources/sortedStrings2.txt");

        System.out.println("Borrando archivos anteriores...");

        if (file1.exists()) {
            file1.delete();
        }

        if (file2.exists()) {
            file2.delete();
        }
    }

    private static List<String> serializeFile(String fileName) {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by the comma delimiter
                String[] divs = line.split(",");
                for (String div : divs) {
                    stringList.add(div.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringList;
    }

    private static List<String[]> divideFile() {
        // Divide file implementation
        List<String> stringList = serializeFile(FILE_NAME);

        List<String[]> Nlist = new ArrayList<>();
        int divs = (int) Math.ceil((double) stringList.size() / LIMIT);
        for (int i = 0; i < divs; i++) {
            int size = Math.min(LIMIT, stringList.size() - i * LIMIT);
            String[] array = new String[size];
            Nlist.add(array);
            for(int j = 0; j < size; j++){
                if(!stringList.get(j + (i * LIMIT)).isEmpty()){
                    Nlist.get(i)[j] = stringList.get(j + (i * LIMIT));
                }
            }
        }

        return Nlist;
    }

    private static String[] sortFileList(List<String[]> strings) {
        List<String> allStrings = new ArrayList<>();
        for (String[] array : strings) {
            allStrings.addAll(Arrays.asList(array));
        }

        String[] allStringsArray = allStrings.toArray(new String[0]);
        localMergeSort.mergeSort(allStringsArray);
        return allStringsArray;
    }

    private static void saveFile(String[] strings, int number) {
        try (FileWriter writer = new FileWriter("server/src/main/resources/sortedStrings" + number + ".txt")) {
            for (String string : strings) {
                writer.write(string + ",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
