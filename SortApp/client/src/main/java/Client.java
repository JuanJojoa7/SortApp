import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

public class Client {
    // Constants
    public static Scanner input = new Scanner(System.in);
    private static final int STRING_LENGTH = 10;
    private static final int MB = 1024 * 1024;
    private static final int TARGET_SIZE_MB = 100;
    private static final String FILE_NAME = "server/src/main/resources/randomStrings.txt";
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static long startTime;

    public static void main(String[] args) {
        // Array of ports
        List<Integer> ports = new ArrayList<>();

        String answer;

        // Connection to the servers
        do {
            System.out.print("¿Deseas agregar un servidor? (s/n): ");
            answer = input.nextLine();
            if (answer.equalsIgnoreCase("s")) {
                System.out.print("Ingresa el puerto del servidor: ");
                ports.add(input.nextInt());
                input.nextLine();
            }
        } while (!answer.equalsIgnoreCase("n"));

        // Strings to sort
        System.out.println("¿Deseas crear el archivo de números? (s/n): ");
        answer = input.nextLine();
        if (answer.equalsIgnoreCase("s")) {
            boolean response = createFile();
            if (response) {
                System.out.println("Archivo creado exitosamente");
            } else {
                System.out.println("El archivo ya existe");
            }
        }

        System.out.println("Ordenando sin hilos...");

        // Load the file
        List<String> stringList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
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

        // Sort list of strings
        String[] strings = stringList.toArray(new String[0]);
        startTime = System.currentTimeMillis();
        mergeSort(strings);
        System.out.println("Archivo ordenado. Tiempo utilizado: " + (System.currentTimeMillis() - startTime) + "ms");

        // Save the sorted strings to a file
        saveFile(strings, 1);

        System.out.println("Archivo guardado.");

        System.out.println("Ordenando archivo con hilos...");

        // Charge parts
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
                        com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SortingFile:tcp -h localhost -p " + port);
                        Sorting.SortFilePrx sortProxy = Sorting.SortFilePrx.checkedCast(base);
                        if(sortProxy == null) {
                            throw new Error("Invalid proxy");
                        }

                        while (true) {
                            String[] stringsToSort = queue.poll();
                            if (stringsToSort == null) {
                                break;
                            }

                            // System.out.println("Sorting file using server on port " + port);
                            // System.out.println("Length: " + stringsToSort.length);

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
    }

    /**
     * Create a file with random strings
     */
    public static boolean createFile() {
        System.out.println("Creando archivo...");

        File file = new File(FILE_NAME);
        boolean fileCreated = false;
        if (!file.exists()) {
            try {
                if (file.getParentFile().mkdirs()) {
                    System.out.println("Directorio creado");
                }
                if (file.createNewFile()) {
                    System.out.println("Archivo creado exitosamente");
                    fileCreated = true;
                } else {
                    System.out.println("El archivo ya existe");
                }
            } catch (IOException e) {
                System.out.println("Error al crear el archivo");
                e.printStackTrace();
            }
        }

        if (fileCreated) {
            System.out.println("Escribiendo en el archivo...");
            Random random = new Random();
            try (FileWriter writer = new FileWriter(file)) {
                while (fileSizeInMB(FILE_NAME) < TARGET_SIZE_MB) {
                    for (int i = 0; i < STRING_LENGTH; i++) {
                        writer.write(CHARACTERS[random.nextInt(CHARACTERS.length)]);
                    }
                    writer.write(",");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileCreated;
    }

    /**
     * Get the file size in MB
     * @param fileName
     * @return
     */
    private static long fileSizeInMB(String fileName) {
        File file = new File(fileName);
        return file.length() / MB;
    }

    private static List<String[]> divideFile() {
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
        int divs = (int) Math.ceil((double) stringList.size() / 70000);
        for (int i = 0; i < divs; i++) {
            int size = Math.min(70000, stringList.size() - i * 70000);
            String[] array = new String[size];
            Nlist.add(array);
            for(int j = 0; j < size; j++){
                if(!stringList.get(j + (i * 70000)).isEmpty()){
                    Nlist.get(i)[j] = stringList.get(j + (i * 70000));
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
        mergeSort(allStringsArray);
        return allStringsArray;
    }

    private static void mergeSort(String[] arr) {
        if (arr.length <= 1) {
            return;
        }

        int mid = arr.length / 2;
        String[] leftHalf = new String[mid];
        String[] rightHalf = new String[arr.length - mid];
        System.arraycopy(arr, 0, leftHalf, 0, mid);
        System.arraycopy(arr, mid, rightHalf, 0, arr.length - mid);

        mergeSort(leftHalf);
        mergeSort(rightHalf);

        merge(arr, leftHalf, rightHalf);
    }

    private static void merge(String[] arr, String[] left, String[] right) {
        int leftIndex = 0, rightIndex = 0, arrIndex = 0;

        while (leftIndex < left.length && rightIndex < right.length) {
            if (left[leftIndex].compareTo(right[rightIndex]) < 0) {
                arr[arrIndex++] = left[leftIndex++];
            } else {
                arr[arrIndex++] = right[rightIndex++];
            }
        }

        while (leftIndex < left.length) {
            arr[arrIndex++] = left[leftIndex++];
        }

        while (rightIndex < right.length) {
            arr[arrIndex++] = right[rightIndex++];
        }
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
