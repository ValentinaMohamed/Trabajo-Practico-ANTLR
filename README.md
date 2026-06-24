# Trabajo Práctico: Intérprete con ANTLR4


## Integrantes

- Leandro Aranda
- Valentina Mohamed


## Variante asignada

**Do-While**


## Video de explicación

https://youtu.be/u-hcbduER9M?si=ID27n694m8zL_HTh


## Descripción del lenguaje

El proyecto implementa un lenguaje de programación imperativo simple denominado **MiniLang**, desarrollado en Java utilizando **ANTLR4** para el análisis léxico y sintáctico. Su sintaxis fue diseñada para ser clara e intuitiva, favoreciendo la legibilidad y facilitando el aprendizaje.

MiniLang soporta los tipos de datos primitivos `int`, `real`, `string` y `bool`, permitiendo la declaración de variables mediante la palabra reservada `var`. Estas pueden inicializarse en el momento de la declaración o recibir un valor posteriormente a través de asignaciones. Para gestionar las variables durante el análisis y la ejecución, se utiliza una **tabla de símbolos** que almacena el tipo y el valor asociado a cada identificador declarado.

El lenguaje admite comentarios de línea (`//`) y comentarios de bloque (`/* ... */`), los cuales son ignorados por el intérprete durante el procesamiento del programa.

Incorpora expresiones aritméticas (`+`, `-`, `*`, `/`), relacionales (`==`, `!=`, `<`, `<=`, `>`, `>=`) y lógicas (`&&`, `||`). Los operadores se aplican según el orden de prioridad definido en la gramática, con posibilidad de modificar dicho orden mediante paréntesis.

Para la salida de información, MiniLang incluye la instrucción `print`, que permite mostrar valores y resultados de expresiones por consola.

En cuanto a las estructuras de control, el lenguaje presenta condicionales `if` e `if-else`, junto con la variante asignada `do-while`, que ejecuta un bloque de instrucciones al menos una vez antes de evaluar la condición de repetición.

Además del análisis sintáctico, el proyecto integra un **análisis semántico** encargado de validar el correcto uso de las construcciones definidas en la gramática. Entre las verificaciones realizadas se encuentran la detección de variables no declaradas, la redeclaración de variables, las incompatibilidades de tipos, las condiciones no booleanas en estructuras de control y las divisiones por cero.

La ejecución de los programas se lleva a cabo mediante un **intérprete** construido utilizando el patrón Visitor generado por ANTLR4. Los programas fuente se almacenan en archivos con extensión `.mini`, los cuales son procesados, validados y ejecutados por el sistema.


## Decisiones de diseño

- **Tipos de datos soportados:** `int`, `real`, `string` y `bool`.  
- **Expresiones relacionales:**  
  - Comparaciones de igualdad (`==`, `!=`) permitidas entre cualquier tipo de dato.  
  - Comparaciones de orden (`<`, `<=`, `>`, `>=`) restringidas únicamente a tipos numéricos (`int`, `real`).  
- **Estructuras condicionales:** se implementan `if` simple y `if-else`.  
- **Salida de datos:** la instrucción `print` admite la impresión de un único valor o expresión por vez.  
- **Comentarios:** se aceptan comentarios de línea (`// ...`) y de bloque (`/* ... */`).  
- **Jerarquía de operadores:** definida en la gramática, respetando precedencia aritmética, relacional y lógica, con posibilidad de modificar el orden mediante paréntesis.  
- **Declaración de variables:** todas las variables deben declararse antes de su uso.  
- **Reasignación:** las variables pueden recibir nuevos valores posteriores a su declaración.  
- **Números negativos:** se admite la representación de valores enteros y reales con signo.  
- **Valores booleanos:** se definen explícitamente como `true` y `false`.  


## Instrucciones de compilación y ejecución

Este proyecto utiliza **Java**, **ANTLR4** y **Maven** para la construcción y ejecución del intérprete.


### Requisitos previos

- JDK 17 o superior instalado y configurado en el `PATH`.
- Maven instalado (versión 3.6+).
- Eclipse IDE con soporte para proyectos Maven.


### Pasos de compilación y ejecución

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/ValentinaMohamed/Trabajo-Practico-ANTLR
   ```
   
2. **Ir a la raíz del proyecto**
   ```bash
   cd Trabajo-Practico-ANTLR
   ````
   
3. **Compilar e instalar dependencias con Maven**
   ```bash
   mvn clean install
   ```

4. **Importar el proyecto en Eclipse**
    - Abrir Eclipse.
    - Ir a File → Import → Existing Maven Projects.
    - Seleccionar la carpeta raíz del proyecto (Trabajo-Practico-ANTLR).
    - Finalizar la importación.
  
5. **Ejecutar el intérprete**
    - En Eclipse, abrir la clase `Main.java`.
    - Hacer clic derecho → Run As → Java Application.
    - El sistema leerá los archivos .mini ubicados en la carpeta programs/, realizará el análisis léxico, sintáctico y semántico, y luego interpretará las instrucciones.

    
## Ejemplos de uso
Si en `programs/programa1.mini` se encuentra:

```bash
// Declaración de variables
var saludo : string = "Hola";
print(saludo);

var numero : int = 3;
print(numero);

// Condicional if-else
if (numero > 0) {
    print("El número es positivo");
} else {
    print("El número no es positivo");
}

// Bucle do-while
var i : int = 0;
do {
    print(i);
    i = i + 1;
} while (i < numero);
```

La salida será:

```bash
Hola
3
El número es positivo
0
1
2
```


## Ejecutar programas propios

Para probar tus propios programas en MiniLang, simplemente creá un archivo con extensión `.mini` y guardalo dentro de la carpeta `programs/`.  
Luego, en la clase `Main.java`, agregá el nombre de tu archivo al arreglo de programas que se van a ejecutar. Por ejemplo:

```bash
String[] archivos = {
    "programs/programa1.mini",
    "programs/miPrograma.mini"
};
```

De esta manera, al correr el Main, tu archivo será leído, analizado y ejecutado junto con los demás programas definidos en la lista.
