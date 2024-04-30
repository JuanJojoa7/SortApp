# Informe Final

## De donde partimos
  El proyecto comenzó con la identificación de la necesidad de un sistema distribuido para ordenar grandes conjuntos de datos, específicamente listas de cadenas de texto. Dada la naturaleza distribuida y la escala de los datos, se decidió utilizar un algoritmo de ordenamiento eficiente, el merge sort, que es conocido por su eficiencia en el manejo de grandes conjuntos de datos y su capacidad para realizar ordenamientos externos.

Para implementar la comunicación entre los nodos en el sistema distribuido, se decidió utilizar Ice (Internet Communications Engine), un middleware de comunicaciones robusto y eficiente. Ice permite la comunicación transparente entre los nodos y maneja los detalles de bajo nivel de la comunicación en red, lo que nos permitió centrarnos en la lógica de la aplicación.

Se definió una interfaz, `SortFile`, utilizando el lenguaje de definición de interfaz (IDL) de Ice. Esta interfaz define un método `sortFileList` que toma una secuencia de cadenas y devuelve una secuencia de cadenas ordenada. Esta interfaz se utiliza para la comunicación entre el cliente y el servidor en el sistema.

Para la construcción y gestión del proyecto, se utilizó Gradle, una herramienta de automatización de construcción potente y flexible. Gradle se encargó de las tareas como la compilación del código, la gestión de las dependencias (incluyendo la dependencia de Ice), y la ejecución de la aplicación.

## Importancia del ICE y Gradle en el desarrollo del proyecto: Sort.ice: 
  Definimos una interfaz `SortFile` en el módulo `Sorting` usando el lenguaje de definición de interfaz (IDL) de Ice. Esta interfaz define un método `sortFileList` que toma una secuencia de cadenas (`StringSeq`) y devuelve una secuencia de cadenas.

El papel de este código en la implementación es definir la interfaz de comunicación entre el cliente y el servidor. El cliente y el servidor utilizan esta interfaz para comunicarse entre sí. El cliente envía una secuencia de cadenas al servidor para que las ordene, y el servidor devuelve la secuencia ordenada.

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
  La clase `Client` en este código es un cliente que se conecta a varios servidores para ordenar cadenas de texto. Aquí está la explicación detallada de cada método:

- `main(String[] args)`: Este es el método principal que se ejecuta cuando se inicia el programa. Primero, solicita al usuario que agregue servidores proporcionando los números de puerto. Luego, pregunta al usuario si desea crear un archivo de números. Si la respuesta es sí, se llama al método `createFile()`. Después de eso, el archivo se divide en partes más pequeñas con el método `divideFile()`. Luego, se inician hilos para cada servidor y se les asigna una parte del archivo para ordenar. Una vez que todos los hilos han terminado, las cadenas ordenadas se combinan y se ordenan nuevamente. Finalmente, las cadenas ordenadas se guardan en un archivo.

- `createFile()`: Este método crea un archivo con cadenas aleatorias. Si el archivo ya existe, no se crea un nuevo archivo. Las cadenas se generan aleatoriamente utilizando los caracteres en el array `CHARACTERS`.

- `fileSizeInMB(String fileName)`: Este método devuelve el tamaño del archivo en megabytes.

- `divideFile()`: Este método divide el archivo en partes más pequeñas. Cada parte contiene como máximo 70000 cadenas. Las partes se almacenan en una lista de arrays de cadenas.

- `sortFileList(List<String[]> strings)`: Este método combina todas las cadenas en una lista y las ordena utilizando el algoritmo de ordenación por mezcla.

- `mergeSort(String[] arr)`: Este método implementa el algoritmo de ordenación por mezcla. Divide el array en dos mitades, las ordena de forma recursiva y luego las combina.

- `merge(String[] arr, String[] left, String[] right)`: Este método es parte del algoritmo de ordenación por mezcla. Combina dos arrays ordenados en un solo array ordenado.

- `saveFile(String[] strings)`: Este método guarda las cadenas ordenadas en un archivo. Cada cadena se separa por una coma.

Además, la clase `Client` utiliza una cola de bloqueo (`BlockingQueue`) para almacenar las partes del archivo. Cada hilo toma una parte de la cola, la ordena y luego la agrega a la lista `dividedStrings`.
  
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
Aquí están los pasos pulidos para ejecutar el programa:

3. **Inicializar los servidores**: Inicie cada servidor que actuará como un trabajador en el programa de ordenamiento. Asegúrese de asignar a cada servidor un puerto único que no esté en uso, en el rango de 10000 en adelante.

4. **Ejecutar el cliente**: Una vez que todos los servidores estén en funcionamiento, inicie el cliente. Durante la inicialización, el cliente le pedirá que introduzca los puertos que los servidores están utilizando.

5. **Crear un archivo de ordenamiento**: El cliente le preguntará si desea crear un nuevo archivo para ordenar. Si elige hacerlo, deberá especificar el tamaño del archivo en megabytes (MB). Si elige no crear un nuevo archivo, el sistema buscará un archivo existente para ordenar.

6. **Ordenar el archivo**: El sistema creará el archivo (si eligió hacerlo) y luego procederá a ordenarlo utilizando los servidores que ha inicializado.

7. **Confirmación de la ordenación**: Una vez que el archivo ha sido ordenado, el sistema le informará que la ordenación se ha completado con éxito. También le proporcionará la ubicación del archivo ordenado en el directorio y el tiempo que tardó en ordenarlo.
