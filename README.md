# TalkingApp-V2

## Autores

* Juan Sebastian Gonzalez -
* Juan Manuel Marin Angarita -
* Juan Felipe Jojoa Crespo - A00382042
* Oscar Gomez -

# Implementación de Algoritmo de Ordenamiento Distribuido con Llamadas Asíncronas

Este repositorio contiene la implementación de un algoritmo de ordenamiento distribuido utilizando llamadas asíncronas en el contexto de ICE (Internet Communications Engine). La tarea fue asignada como parte de un proyecto académico para explorar y desarrollar sistemas distribuidos eficientes.

## Descripción del Problema

El objetivo de esta tarea es diseñar e implementar un algoritmo de ordenamiento que pueda ser distribuido entre varios nodos de una red utilizando ICE. Se requiere una estrategia de distribución eficiente que permita dividir el conjunto de datos entre los nodos, coordinar las tareas de ordenamiento de manera asíncrona y evaluar el rendimiento del algoritmo distribuido en términos de tiempo de ejecución al aumentar el número de nodos en la red.

## Contenido del Repositorio

- **src/**: Contiene el código fuente de la implementación en Java.
- **docs/**: Incluye documentación adicional, como el informe final y cualquier otro material relevante.
- **README.md**: Proporciona información básica sobre el proyecto, instrucciones de instalación y uso, así como enlaces a recursos adicionales.

## Estructura del Código Fuente

El código fuente está organizado de la siguiente manera:

- **Main.java**: El punto de entrada del programa que inicializa los nodos y coordina la distribución de tareas.
- **SortAlgorithm.java**: Contiene la implementación del algoritmo de ordenamiento distribuido.
- **Node.java**: Representa un nodo en la red que participa en el proceso de ordenamiento distribuido.
- **ICECommunicator.java**: Maneja la comunicación entre los nodos utilizando ICE.

## Instrucciones de Uso

1. Clona este repositorio en tu máquina local.
2. Importa el proyecto en tu entorno de desarrollo Java preferido.
3. Ejecuta el programa principal (Main.java) y sigue las instrucciones para probar el algoritmo de ordenamiento distribuido.
4. Consulta el informe en la carpeta **docs/** para obtener más detalles sobre la estrategia de distribución, los resultados de las pruebas y el análisis de rendimiento.
