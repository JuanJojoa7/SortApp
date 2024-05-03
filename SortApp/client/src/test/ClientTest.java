import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void testCreateFile() {
        // Simulate file creation and verify it returns true
        assertTrue(Client.createFile());
    }

   /*@Test
    public void testDivideFile() {
        // Verify that the divided string list is not null
        assertNotNull(Client.divideFile());
    }

    @Test
    public void testMergeSort() {
        // Test the mergeSort method with an unsorted array
        String[] arr = {"z", "a", "c", "b"};
        Client.mergeSort(arr);
        assertArrayEquals(new String[]{"a", "b", "c", "z"}, arr);
    }*/
}

