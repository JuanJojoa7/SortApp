import com.zeroc.Ice.Current;
import Sorting.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * SortFileI
 * Objetive: Implement the SortFile interface
 */
public class SortFileI implements SortFile {
    // Constants
    private static final int STRING_LENGTH = 10;
    private static final int MB = 1024 * 1024;
    private static final int TARGET_SIZE_MB = 2;
    private static final String FILE_NAME = "server/src/main/resources/randomStrings.txt";
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int TOTAL_CLIENTS = 2;

    /**
     * Create a file with random strings
     * @param current
     */
    @Override
    public boolean createFile(Current current) {
        System.out.println("Creating file...");

        File file = new File(FILE_NAME);
        boolean fileCreated = false;
        if (!file.exists()) {
            try {
                if (file.getParentFile().mkdirs()) {
                    System.out.println("Directory created");
                }
                if (file.createNewFile()) {
                    System.out.println("File created");
                    fileCreated = true;
                } else {
                    System.out.println("File already exists or failed to be created");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file");
                e.printStackTrace();
            }
        }

        if (fileCreated) {
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
    private long fileSizeInMB(String fileName) {
        File file = new File(fileName);
        return file.length() / MB;
    }

    private int serverCount(){
        return TOTAL_CLIENTS;
    }

    @Override
    public String[] sortFileList(String[] strings, Current current) {
        // Sort file implementation
        //throw new UnsupportedOperationException("Unimplemented method 'sortFileList'");
        strings = mergeSort(strings);
        return strings;
    }

    
    private String[] mergeSort(String[] arr) {
        if (arr.length <= 1) {
            return null;
        }

        int mid = arr.length / 2;
        String[] leftHalf = new String[mid];
        String[] rightHalf = new String[arr.length - mid];
        System.arraycopy(arr, 0, leftHalf, 0, mid);
        System.arraycopy(arr, mid, rightHalf, 0, arr.length - mid);

        mergeSort(leftHalf);
        mergeSort(rightHalf);

        return merge(arr, leftHalf, rightHalf);

    }

    private String[] merge(String[] arr, String[] left, String[] right) {
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

        return arr;
    }

    private static int connectionCount = 0;

    @Override
    public String register(Current current) {
        connectionCount++;
        String message = connectionCount >= TOTAL_CLIENTS ? "Recibido" : "Waiting for all clients to connect...";
        System.out.println(message);
        return message;
    }

    @Override
    public void waitForAllClients(Current current) {
        while (connectionCount < TOTAL_CLIENTS) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All clients connected");
    }
}
