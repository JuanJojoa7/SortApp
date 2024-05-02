import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Test;

public class ClientTest {

    @Test
    public void testCreateFile() {
        // Simular la creación de un archivo y verificar que devuelve verdadero
        assertTrue(Client.createFile());
    }

    @Test
    public void testDivideFile() {
        // Verificar que la lista de cadenas divididas no sea nula
        assertNotNull(Client.divideFile());
    }

    @Test
    public void testMergeSort() {
        // Probar el método mergeSort con un arreglo desordenado
        String[] arr = {"z", "a", "c", "b"};
        Client.mergeSort(arr);
        assertArrayEquals(new String[]{"a", "b", "c", "z"}, arr);
    }

    
}
