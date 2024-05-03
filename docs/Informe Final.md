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
  La clase `Client` en este código es la clase principal que coordina la creación de un archivo de cadenas aleatorias, la división de este archivo en partes, el envío de estas partes a varios servidores para su ordenación, y finalmente la combinación de las cadenas ordenadas en un solo archivo. Aquí está la explicación detallada de cada método:

- `main(String[] args)`: Este es el método principal que se ejecuta al iniciar la aplicación. Primero, solicita al usuario que introduzca los puertos de los servidores a los que se conectará. Luego, pregunta al usuario si desea crear un archivo de cadenas aleatorias. Si el usuario responde afirmativamente, se llama al método `createFile()`. A continuación, se divide el archivo en partes con el método `divideFile()`. Luego, se crea un hilo para cada servidor y se envían las partes del archivo a los servidores para su ordenación. Finalmente, se combinan las cadenas ordenadas en un solo archivo y se guarda el archivo.

- `createFile()`: Este método crea un archivo de cadenas aleatorias. Si el archivo ya existe, no se crea un nuevo archivo. Las cadenas aleatorias se generan a partir de un conjunto de caracteres predefinidos.

- `fileSizeInMB(String fileName)`: Este método devuelve el tamaño del archivo especificado en megabytes.

- `divideFile()`: Este método divide el archivo de cadenas en partes. Cada parte contiene un máximo de 70000 cadenas.

- `sortFileList(List<String[]> strings)`: Este método combina todas las cadenas en una sola lista y luego ordena las cadenas utilizando el algoritmo de ordenación por fusión.

- `mergeSort(String[] arr)`, `merge(String[] arr, String[] left, String[] right)`: Estos métodos implementan el algoritmo de ordenación por fusión.

- `saveFile(String[] strings)`: Este método guarda las cadenas ordenadas en un archivo.

La relación entre la clase `Client` y las clases `Settings`, `Server` y `SortFileI` podría ser que `Client` utiliza la configuración proporcionada por `Settings` para establecer la conexión con los servidores. `Client` envía las partes del archivo a `Server` para su ordenación. `Server` implementa la interfaz `SortFileI` para proporcionar el servicio de ordenación. 
  
## **Server.java**:
  La clase `Server` en este código es un servidor que se ejecuta en un puerto específico y espera las solicitudes de los clientes para ordenar las cadenas de texto. Aquí está la explicación detallada:

- `main(String[] args)`: Este es el método principal que se ejecuta cuando se inicia el servidor. Primero, solicita al usuario que ingrese el número de puerto en el que se ejecutará el servidor. Luego, inicializa el comunicador Ice, que es responsable de la creación de adaptadores de objetos y proxies, y la gestión de la comunicación de red. Después de eso, crea un adaptador de objeto con el nombre "SortingFileAdapter" y lo configura para escuchar en el puerto especificado. Luego, crea un objeto de la clase `SortFileI` y lo agrega al adaptador con la identidad "SortingFile". Después de activar el adaptador, el servidor espera a que se cierre.

La relación entre la clase `Server` y la clase `Client` es que el cliente se conecta a este servidor para ordenar las cadenas de texto. El cliente divide el archivo en partes más pequeñas y envía cada parte a un servidor diferente para que la ordene. Una vez que todas las partes están ordenadas, el cliente las combina y las ordena de nuevo. Por lo tanto, el servidor es responsable de ordenar una parte del archivo, mientras que el cliente es responsable de dividir el archivo, enviar las partes a los servidores, combinar las partes ordenadas y ordenarlas de nuevo.

## **SortFileI.java**:
  La clase `SortFileI` en este código implementa la interfaz `SortFile` y proporciona la funcionalidad para ordenar una lista de cadenas de texto. Aquí está la explicación detallada de cada método:

- `sortFileList(String[] strings, Current current)`: Este método es la implementación de la interfaz `SortFile`. Toma una lista de cadenas y la ordena utilizando el algoritmo de ordenación por mezcla. El objeto `Current` proporciona información sobre la solicitud actual, como el adaptador de objeto y la identidad del objeto, pero no se utiliza en este método.

- `mergeSort(String[] arr)`: Este método implementa el algoritmo de ordenación por mezcla. Divide el array en dos mitades, las ordena de forma recursiva y luego las combina. Si el array tiene un solo elemento o está vacío, se devuelve tal cual, ya que un array con un solo elemento ya está ordenado.

- `merge(String[] arr, String[] left, String[] right)`: Este método es parte del algoritmo de ordenación por mezcla. Combina dos arrays ordenados en un solo array ordenado. Primero, compara los elementos de los arrays `left` y `right` y agrega el menor al array `arr`. Luego, agrega los elementos restantes del array `left` o `right` al array `arr`.

La relación entre la clase `SortFileI` y las clases `Client` y `Server` es que `SortFileI` proporciona la funcionalidad que el servidor `Server` ofrece a los clientes `Client`. El cliente se conecta al servidor y le pide que ordene una lista de cadenas utilizando el método `sortFileList()`. El servidor utiliza la implementación de `SortFileI` para ordenar las cadenas. Por lo tanto, `SortFileI` es esencialmente la parte del servidor que realiza el trabajo de ordenación.
  
## **Settings.java**:
  La clase `Settings` en este código es una clase de configuración que almacena el número de nodos y el puerto que se utilizarán en la aplicación. Aquí está la explicación detallada de cada método:

- `Settings(int numNodes, int port)`: Este es el constructor de la clase `Settings`. Toma dos parámetros: el número de nodos y el puerto. Estos valores se asignan a las variables de instancia `numNodes` y `port`.

- `getNumNodes()`: Este método es un getter para la variable de instancia `numNodes`. Devuelve el número de nodos.

- `setNumNodes(int numNodes)`: Este método es un setter para la variable de instancia `numNodes`. Establece el número de nodos.

- `getPort()`: Este método es un getter para la variable de instancia `port`. Devuelve el puerto.

- `setPort(int port)`: Este método es un setter para la variable de instancia `port`. Establece el puerto.

La relación entre la clase `Settings` y las clases `Client`, `Server` y `SortFileI` podría ser que `Settings` proporciona la configuración que `Client` y `Server` utilizan para establecer la conexión. Por ejemplo, `Client` podría utilizar el número de nodos para determinar cuántos servidores se deben conectar y el puerto para establecer la conexión. `Server` podría utilizar el puerto para escuchar las solicitudes entrantes. 
  
## Instrucciones de Uso y explicacion de las salidas: Ejecucion Local

![image](https://github.com/JuanJojoa7/SortApp/assets/110687384/81938b6a-52eb-4257-9aa3-4ef81efaed7d)

1. Clona este repositorio en tu máquina local.
2. Importa el proyecto en tu entorno de desarrollo Java preferido.
   
Aquí están los pasos pulidos para ejecutar el programa:

4. **Inicializar la clase Cliente**: Al inicializar la clase cliente pedira al usuario crear servidores locales y se podra elegir cuantos servidores desea crear. Es importante destacar que cuanto más servidores locales se creen, menor será el tiempo que le tomará al programa organizar el archivo de datos. Esto se debe a que cada servidor puede procesar una parte del trabajo de manera independiente y simultánea, lo que aprovecha el paralelismo y acelera el proceso de organización del archivo.

6. **Creacion del archivo de datos**: Acto seguido se le pregunta al usuario si desea crear el archivo de numeros para posteriormente organizarlo, en caso de no querer crear uno nuevo usara el que este en el directorio.

7. **Ordenamiento Sin Hilos**: Despues de ingresar las entradas descritas anteriormente, el programa empezara a ordenar el archivo creado sin hilos, y dejando el tiempo de ejecucion que demora en consola para despues hacer la comparacion de tiempos usando hilos.

**Ordenamiento sin Hilos (Secuencial)**
**Procesamiento Secuencial:** Cuando el archivo se organiza sin utilizar hilos, el proceso es secuencial. Esto significa que cada paso del ordenamiento se realiza uno después del otro en una sola secuencia de ejecución. Si el archivo es grande o la tarea es intensiva en términos de procesamiento, este enfoque puede llevar más tiempo porque cada operación debe completarse antes de pasar a la siguiente.
**Utilización de un Solo Núcleo de Procesamiento:** En un entorno de ejecución secuencial, la tarea se ejecuta en un solo hilo de ejecución (generalmente en un solo núcleo de procesador). Esto puede limitar la cantidad de trabajo que se puede realizar simultáneamente, especialmente si el ordenamiento implica operaciones intensivas en términos de CPU.

9. **Ordenamiento con hilos**: El sistema creará el archivo (si eligió hacerlo) y luego procederá a ordenarlo utilizando los servidores que ha inicializado.

10. **Confirmación de la ordenación**: Una vez que el archivo ha sido ordenado, el sistema le informará que la ordenación se ha completado con éxito. También le proporcionará la ubicación del archivo ordenado en el directorio y el tiempo que tardó en ordenarlo.
