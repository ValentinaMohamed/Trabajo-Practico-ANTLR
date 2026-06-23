# Trabajo Práctico: Intérprete con ANTLR4

## Integrantes

- Leandro Aranda
- Valentina Mohamed

## Variante asignada

**Do-While**

## Descripción del lenguaje

El proyecto implementa un lenguaje de programación imperativo simple denominado MiniLang, desarrollado en Java utilizando ANTLR4 para el análisis léxico y sintáctico. Su sintaxis fue diseñada para ser clara e intuitiva, favoreciendo la legibilidad y facilitando el aprendizaje.

MiniLang soporta los tipos de datos primitivos `int`, `real`, `string` y `bool`, permitiendo la declaración de variables mediante la palabra reservada `var`. Estas pueden inicializarse en el momento de la declaración o recibir un valor posteriormente a través de asignaciones. Para gestionar las variables durante el análisis y la ejecución, se utiliza una tabla de símbolos que almacena el tipo y el valor asociado a cada identificador declarado.

El lenguaje admite comentarios de línea (`//`) y comentarios de bloque (`/* ... */`), los cuales son ignorados por el intérprete durante el procesamiento del programa.

Asimismo, incorpora expresiones aritméticas (`+`, `-`, `*`, `/`), relacionales (`==`, `!=`, `<`, `<=`, `>`, `>=`) y lógicas (`&&`, `||`). Los operadores se aplican según el orden de prioridad definido en la gramática, donde la multiplicación y la división tienen prioridad sobre la suma y la resta, seguidas por las operaciones relacionales y lógicas. El uso de paréntesis puede modificar dicho orden.

Los operadores de igualdad (`==`) y desigualdad (`!=`) aceptan la comparación de valores numéricos, cadenas de texto y valores booleanos.

Para la salida de información, MiniLang incluye la instrucción `print`, que permite mostrar valores y resultados de expresiones por consola.

En cuanto a las estructuras de control, el lenguaje presenta condicionales `if` e `if-else`, junto con la variante asignada `do-while`, que ejecuta un bloque de instrucciones al menos una vez antes de evaluar la condición de repetición.

Además del análisis sintáctico, el proyecto integra un análisis semántico encargado de validar el correcto uso de las construcciones definidas en la gramática. Entre las verificaciones realizadas se encuentran la detección de variables no declaradas, la redeclaración de variables, las incompatibilidades de tipos, las operaciones inválidas entre tipos de datos, las condiciones no booleanas en estructuras de control y las divisiones por cero.

La ejecución de los programas se lleva a cabo mediante un intérprete construido utilizando el patrón Visitor generado por ANTLR4. Los programas fuente se almacenan en archivos con extensión `.mini`, los cuales son procesados, validados y ejecutados por el sistema.

## Decisiones de diseño

## Instrucciones de compilación y ejecución

## Ejemplos de uso
