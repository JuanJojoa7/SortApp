import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileGenerator {
    private static final int STRING_LENGTH = 10;
    private static final int MB = 1024 * 1024;
    private static final int TARGET_SIZE_MB = 100;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static String FILE_NAME;

    public FileGenerator(String fileName) {
        FILE_NAME = fileName;
    }

    /**
     * Create a file with random strings
     */
    public boolean createFile() {
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
                int commaCount = 0;
                while (fileSizeInMB(FILE_NAME) < TARGET_SIZE_MB) {
                    for (int i = 0; i < STRING_LENGTH; i++) {
                        writer.write(CHARACTERS[random.nextInt(CHARACTERS.length)]);
                    }
                    writer.write(",");
                    commaCount++;
                    if (commaCount == 6) {
                        writer.write(System.lineSeparator());
                        commaCount = 0;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileCreated;
    }

    /**
     * Get the file size in MB
     * 
     * @param fileName
     * @return
     */
    private long fileSizeInMB(String fileName) {
        File file = new File(fileName);
        return file.length() / MB;
    }
}
