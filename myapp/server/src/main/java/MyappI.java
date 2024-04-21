import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Current;
import java.util.*;
import java.util.concurrent.*;


public class MyappI implements Demo.Order {
    
    public void sortList(int[] numbers, int numServers, Current current) {
        int listSize = numbers.length;
        int sublistSize = listSize / numServers;
    
        // Create an ExecutorService with a fixed thread pool size
        ExecutorService executor = Executors.newFixedThreadPool(numServers);
        List<Future<List<Integer>>> futures = new ArrayList<>();
    
        // Distribute workload among connected clients
        for (int i = 0; i < numServers; i++) {
            final int startIndex = i * sublistSize;
            final int endIndex = Math.min((i + 1) * sublistSize, listSize);
    
            // Submit a sorting task to the executor
            Future<List<Integer>> future = executor.submit(() -> {
                List<Integer> sublist = new ArrayList<>();
                for (int j = startIndex; j < endIndex; j++) {
                    sublist.add(numbers[j]);
                }
                Collections.sort(sublist); // Sort the sublist
                return sublist;
            });
    
            futures.add(future); // Store the Future object for later retrieval
        }
    
        // Collect sorted sublists
        List<List<Integer>> sortedSublists = new ArrayList<>();
        for (Future<List<Integer>> future : futures) {
            try {
                sortedSublists.add(future.get()); // Retrieve the sorted sublist
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    
        // Shutdown the ExecutorService
        executor.shutdown();
    
        // Combine and sort the final list
        List<Integer> sortedNumbers = new ArrayList<>();
        for (List<Integer> sublist : sortedSublists) {
            sortedNumbers.addAll(sublist);
        }
        Collections.sort(sortedNumbers);
        // Use 'sortedNumbers' as the sorted result
    }

   

    // public void sortFinalList(List<List<Integer>> numbers){ //compares first value and adds smallest to list until all numbers are in 1 list}

    
}
