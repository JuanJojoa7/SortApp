# Informe Final

## Metodo de ordenamiento
  El algoritmo de ordenamiento por mezcla (merge sort) es una elección eficiente para este sistema debido a sus características particulares:

1. **Eficiencia**: Merge sort es un algoritmo de ordenamiento de tiempo O(n log n) en todos los casos, lo que significa que puede manejar grandes conjuntos de datos de manera eficiente.

2. **Estabilidad**: Merge sort es un algoritmo de ordenamiento estable, lo que significa que mantiene el orden relativo de los registros con claves iguales. Esto puede ser importante si las cadenas de texto tienen algún tipo de ordenamiento secundario que se desea mantener.

3. **Ordenamiento externo**: Merge sort es particularmente bueno para el ordenamiento externo, donde los datos que se están ordenando no caben en la memoria al mismo tiempo. Dado que este sistema parece estar trabajando con archivos de texto, esto podría ser una consideración importante.

Por otro lado, otros algoritmos de ordenamiento pueden no ser adecuados por varias razones:

- **Bubble sort, Insertion sort, Selection sort**: Estos algoritmos son O(n^2) en el peor de los casos, lo que los hace ineficientes para grandes conjuntos de datos.

- **Quick sort**: Aunque Quick sort es O(n log n) en el mejor y en el caso promedio, es O(n^2) en el peor de los casos. Además, no es estable y puede ser ineficiente para el ordenamiento externo.

- **Heap sort**: Aunque Heap sort es O(n log n) en todos los casos, no es estable y puede ser ineficiente para el ordenamiento externo.

Por lo tanto, dado el contexto de este sistema, merge sort parece ser la elección más adecuada.



## Estructura del Código Fuente

El código fuente está organizado de la siguiente manera:

## **Client.java**:
  La clase `Client` en este código es una implementación de un cliente que se conecta a varios servidores para ordenar cadenas de texto. Aquí está lo que hace cada parte del código:

1. **Variables y constantes**: Define varias constantes y variables, incluyendo un `Scanner` para la entrada del usuario, una longitud de cadena, un tamaño de archivo objetivo, un nombre de archivo, y un array de caracteres para generar cadenas aleatorias.

2. **Método main**: En el método `main`, se crea una lista de puertos a los que se conectará el cliente. Luego, se le pregunta al usuario si desea crear un archivo de números. Si el usuario responde afirmativamente, se llama al método `createFile` para crear un archivo con cadenas aleatorias. Luego, el archivo se divide en partes iguales al número de servidores y cada parte se envía a un servidor diferente para ser ordenada. Una vez que todas las partes han sido ordenadas por los servidores, se combinan y se ordenan en el cliente. Finalmente, se imprime el tiempo que tomó todo el proceso.

3. **Método createFile**: Este método crea un archivo con cadenas aleatorias. Si el archivo ya existe, no se crea uno nuevo.

4. **Método fileSizeInMB**: Este método devuelve el tamaño del archivo en megabytes.

5. **Método divideFile**: Este método divide el archivo en partes iguales al número de servidores.

6. **Método sortFileList**: Este método combina todas las partes ordenadas por los servidores y las ordena.

7. **Métodos mergeSort y merge**: Estos métodos implementan el algoritmo de ordenamiento por mezcla para ordenar las cadenas.

## **Server.java**:
  La clase `Server` en este código es una implementación de un servidor que recibe cadenas de texto de un cliente para ordenarlas. Aquí está lo que hace cada parte del código:

1. **Variables**: Define un `Scanner` para la entrada del usuario y una variable `port` para almacenar el número de puerto en el que se ejecutará el servidor.

2. **Método main**: En el método `main`, se le pide al usuario que introduzca el número de puerto en el que se ejecutará el servidor. Luego, se inicializa un comunicador Ice, que es un objeto que proporciona servicios para la comunicación en red, la gestión de hilos, la gestión de la memoria, etc. Se crea un adaptador de objetos con el comunicador, que se utiliza para recibir peticiones de los clientes. Se crea un objeto `SortFileI` y se añade al adaptador con una identidad única. Luego, se activa el adaptador y se espera a que el comunicador se cierre.

Tambien se relaciona con la clase `Client.java`, el cliente se conecta a este servidor a través del puerto especificado y le envía cadenas de texto para que las ordene. El servidor recibe las cadenas, las ordena y las devuelve al cliente. En resumen, esta clase implementa un servidor que recibe cadenas de texto de un cliente, las ordena y las devuelve al cliente.

## **SortFileI.java**:
  La clase `SortFileI` implementa la interfaz `SortFile` y define cómo se deben ordenar las cadenas de texto que recibe del cliente. Aquí está lo que hace cada parte del código:

1. **sortFileList**: Este método toma un array de cadenas de texto y las ordena utilizando el algoritmo de ordenamiento por mezcla (merge sort). Luego, devuelve el array ordenado.

2. **mergeSort**: Este es un método recursivo que implementa el algoritmo de ordenamiento por mezcla. Divide el array en dos mitades, las ordena por separado y luego las combina.

3. **merge**: Este método toma dos arrays ordenados y los combina en un solo array ordenado.

Cómo se relaciona con las clases `Client.java` y `Server.java`:

- `Client.java`: El cliente envía un array de cadenas de texto al servidor para que lo ordene. El servidor utiliza la implementación de `SortFileI` para ordenar las cadenas.

- `Server.java`: El servidor recibe el array de cadenas de texto del cliente y utiliza la implementación de `SortFileI` para ordenar las cadenas. Luego, devuelve el array ordenado al cliente.
  
## **Settings.java**:
  La clase `Settings` en este código es una clase de configuración que almacena la configuración del sistema, específicamente el número de nodos (`numNodes`) y el puerto (`port`). Aquí está lo que hace cada parte del código:

1. **Variables**: Define dos variables privadas, `numNodes` y `port`.

2. **Constructor**: El constructor `SystemConfig` toma dos argumentos, `numNodes` y `port`, y los asigna a las variables de la clase.

3. **Getters y Setters**: Los métodos `getNumNodes`, `setNumNodes`, `getPort`, y `setPort` son getters y setters para las variables `numNodes` y `port`, respectivamente. Permiten obtener y establecer los valores de estas variables.

Cómo se relaciona con las clases `Client.java`, `Server.java` y `SortFileI.java`:

- `Client.java`: El cliente puede utilizar la clase `Settings` para obtener la configuración del sistema, como el número de nodos y el puerto al que se debe conectar.

- `Server.java`: El servidor puede utilizar la clase `Settings` para obtener la configuración del sistema, como el número de nodos y el puerto en el que debe escuchar las conexiones.

- `SortFileI.java`: Aunque no hay una relación directa entre `SortFileI` y `Settings` en el código proporcionado, es posible que `SortFileI` pueda utilizar la configuración del sistema para determinar cómo manejar la ordenación de las cadenas de texto.

## Instrucciones de Uso

1. Clona este repositorio en tu máquina local.
2. Importa el proyecto en tu entorno de desarrollo Java preferido.
3. Ejecuta el programa principal (Main.java) y sigue las instrucciones para probar el algoritmo de ordenamiento distribuido.
4. Consulta el informe en la carpeta **docs/** para obtener más detalles sobre la estrategia de distribución, los resultados de las pruebas y el análisis de rendimiento.
