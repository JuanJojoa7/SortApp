# Informe Final



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

- **Node.java**: Representa un nodo en la red que participa en el proceso de ordenamiento distribuido.
- **ICECommunicator.java**: Maneja la comunicación entre los nodos utilizando ICE.

## Instrucciones de Uso

1. Clona este repositorio en tu máquina local.
2. Importa el proyecto en tu entorno de desarrollo Java preferido.
3. Ejecuta el programa principal (Main.java) y sigue las instrucciones para probar el algoritmo de ordenamiento distribuido.
4. Consulta el informe en la carpeta **docs/** para obtener más detalles sobre la estrategia de distribución, los resultados de las pruebas y el análisis de rendimiento.
