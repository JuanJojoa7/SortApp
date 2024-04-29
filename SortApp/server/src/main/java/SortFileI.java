import com.zeroc.Ice.Current;
import Sorting.*;

public class SortFileI implements SortFile {
    
    @Override
    public void uploadFile(Current current) {
        System.out.println("Uploading file...");
    }

    @Override
    public void sendDataBlocks(Current current) {
        System.out.println("Sending data blocks...");
    }

    @Override
    public void sortFinalList(Current current) {
        System.out.println("Sorting final list...");
    }
}
