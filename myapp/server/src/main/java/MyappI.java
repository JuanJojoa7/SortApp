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
        List<Future<Stack<Integer>>> futures = new ArrayList<>();
    
        // Distribute workload among connected clients
        for (int i = 0; i < numServers; i++) {
            final int startIndex = i * sublistSize;
            final int endIndex = Math.min((i + 1) * sublistSize, listSize);
    
           // Submit a sorting task to the executor
           Future<Stack<Integer>> future = executor.submit(() -> {
                List<Integer> sublist = new ArrayList<>();
                for (int j = startIndex; j < endIndex; j++) {
                    sublist.add(numbers[j]);
                }
                Collections.sort(sublist); // Sort the sublist
                return listaAStack(sublist);
            });

            futures.add(future);
        }
    
        // Collect sorted sublists
        List<Stack<Integer>> sortedSublists = new ArrayList<>();
        for (Future<Stack<Integer>> future : futures) {
            try {
                sortedSublists.add(future.get()); // Retrieve the sorted sublist
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    
        // Shutdown the ExecutorService
        executor.shutdown();
    
        // Combine and sort the final list
        sortFinalList(sortedSublists);
    }

   

    public List<Integer> sortFinalList(List<Stack<Integer>> numbers){ 
        //compares first value and adds smallest to list until all numbers are in 1 list
        boolean end = false;
        int last = 0;
        List<Integer> OrderedList = new ArrayList<>();
        while(end != true){
            int min = encontrarMinimoEntrePilas(numbers);
            for(int i=0; i<numbers.size()-1; i++){
                if(numbers.get(i).peek() == min){
                    OrderedList.add(numbers.get(i).pop());
                    i=0;
                }
            }
            for(int i=0; i<numbers.size()-1; i++){
                if (numbers.get(i).empty()){
                    last++;
                }
            }
            if(last==numbers.size()){
                end = true;
            } else {
                last = 0;
            }
        }
        
        return OrderedList;
    }

    public static Stack<Integer> listaAStack(List<Integer> lista) {
        Stack<Integer> pila = new Stack<>();
        // Agregar los elementos de la lista en orden inverso
        for (int i = lista.size() - 1; i >= 0; i--) {
            pila.push(lista.get(i));
        }
        return pila;
    }

     // Método para encontrar el valor mínimo entre las pilas
    public static int encontrarMinimoEntrePilas(List<Stack<Integer>> pilas) {
      if (pilas == null || pilas.isEmpty()) {
         throw new IllegalArgumentException("La lista de pilas no puede estar vacía.");
      }

     int minValor = Integer.MAX_VALUE; // Inicializar el mínimo con el máximo valor entero posible

      // Recorrer todas las pilas
      for (Stack<Integer> pila : pilas) {
         // Si la pila no está vacía, obtener el elemento en la cima
           if (!pila.isEmpty()) {
                int top = pila.peek();
               if (top < minValor) {
                   minValor = top;
               }
          } else { // Si la pila está vacía, verificar si el valor mínimo actual es mayor que Integer.MAX_VALUE
              if (minValor == Integer.MAX_VALUE) {
                 minValor = Integer.MIN_VALUE; // Asignar el valor mínimo entero posible si no se ha encontrado ningún elemento
             }
         }
        }

        return minValor;
    }


    
}
