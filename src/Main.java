// (c) 2026 Tecnicas y Metodologias de Programacion Avanzada, UCN, Antofagasta, Chile.
/*
 * Posicionar 4 espejos diagonales en los espacios disponibesl de una matriz 5x5
 * para que un laser llegue a su objetivo (target) y el show pueda comenzar.
 * 1.- Disposicion y simbologias:
 * Se opera sobre una matriz de 5x5 y el programa debe permitir al usuario definir las posiciones iniciales de los
 * siguientes elementos:
 * - "L": Fuente de Laser (El haz siempre sale disparado hacia el este)
 * - "T": Objetivo (target)
 * - "X": Muros de Bloqueo (Se pueden colocar hasta un maximo de 3 muros
 * - "-": Indica que la celda esta vacia
 * - En la configuracion inicial el usuario debe poder seleccionar cuantos espejos
 *      de tipo (/) o (\) desea colocar ( minimo y maximo 4) y luego posicionarlos en la matriz. (SIEMPRE 4 ESPEJOS)
 * --------------------------------------------------------------------------------
 * 2.- Logica de refraccion (ATENCION QUE ESTA ES LA PARTE MAS CONFUSA)
 *      1) ESPEJO "/": SI EL HAZ VIENE DESDE EL OESTE(←), SALE HACIA EL NORTE(↑)
 *                     SI EL HAZ VIENE DESDE EL ESTE(→), SALE HACIA EL SUR(↓)
 *                     SI EL HAZ VIENE DESDE EL NORTE(↑), SALE HACIA EL OESTE(←)
 *                     SI EL HAZ VIENE DESDE EL SUR(↓), SALE HACIA EL ESTE(→)
 *      2) ESPEJO "\": SI EL HAZ VIENE DESDE EL OESTE(←), SALE HACIA EL SUR(↓)
 *                     SI EL HAZ VIENE DESDE EL ESTE(→), SALE HACIA EL NORTE(↑)
 *                     SI EL HAZ VIENE DESDE EL NORTE(↑), SALE HACIA EL ESTE(→)
 *                     SI EL HAZ VIENE DESDE EL SUR(↓), SALE HACIA EL OESTE(←)
 * --------------------------------------------------------------------------------
 * 3.- Dónde implementar el backtracking?
 *     EL algoritmo debe buscar de forma exhaustiva una configuracion valida para los 4 espejos, la logica debe seguir
 *     el patron RECURSIVO de:
 *     a) Intentar colocar un espejo en una celda vacia
 *     b) Simular el trayecto del laser
 *     c) Si el laser no llega al objetivo, realizar BACKTRACKING (quitar el espejo y probar en otra posicion u
 *        orientacion)
 * 4.- Condicion de fallo por limites
 *     El trayecto del laser se considera FALLIDO si este intenta salir de los limites de la matriz o si colisiona
 *     con un muro de bloqueo (X) sin haber alcanzado el objetivo (T)
 */

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static Scanner in = new Scanner(System.in);
    static int cLaser;
    static int fLaser;

    public static void main(String[] args) {
        // Creando la matriz
        String[][] matriz = {
                {"-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-"},
                {"-", "-", "-", "-", "-"}
        };
        imprimirMatriz(matriz);
        // Pedimos al usuario la posicion del laser
        posicionLaser(matriz);
        imprimirMatriz(matriz);
        // Pedimos al usuario la posicion del target
        posicionTarget(matriz);
        imprimirMatriz(matriz);
        // Pedimos al usuario la cantidad de muros y sus respectivas posiciones
        posicionMuros(matriz);
        imprimirMatriz(matriz);
        // espejo seleccionados por el usuario
        String[] espejosSeleccionados = tipoEspejos();
        // Probamos configuración de espejos
        if (posicionEspejos(matriz, espejosSeleccionados,0)) {
            System.out.println("Configuración encontrada.");
            imprimirMatriz(matriz);
        } else {
            System.out.println("Fallido, no se encontró una configuración adecuada");
        }


    }

    /**
     * Metodo encargado de imprimir la matriz.
     *
     * @param matriz principal.
     */
    public static void imprimirMatriz(String[][] matriz) {
        System.out.println(" 1 2 3 4 5");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Metodo encargado de preguntar al usuario la posicion del laser
     * para luego asignarlo a la matriz principal.
     *
     * @param matriz principal utilizada en el sistema.
     */
    public static void posicionLaser(String[][] matriz) {
        //Preguntamos al usuario la posicion inicial del Laser
        System.out.println("A continuacion ingrese las coordenadas iniciales del Laser: ");
        while (true) {
            try {
                System.out.println("Ingrese la fila del laser (1 a 5): ");
                fLaser = in.nextInt() -1;
                System.out.println("Ingrese la columna del laser (1 a 5): ");
                cLaser = in.nextInt() -1;
                // validar que las coordenadas esten dentro del rango
                if (cLaser <= 4 && cLaser >= 0 && fLaser <= 4 && fLaser >= 0) {
                    // Ingresamos el laser a la matriz
                    matriz[fLaser][cLaser] = "L";
                    break;
                } else {
                    System.out.println("Ingrese un valor valido (1 a 5)");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Texto identificado, ingrese unos valores validos.");
                in.nextLine();
            }
        }

    }

    /**
     * Metodo encargado de preguntar al usuario la posicion del target
     * para luego asignarlo a la matriz principal.
     *
     * @param matriz principal utilizada en el sistema.
     */
    public static void posicionTarget(String[][] matriz) {
        int fTarget;
        int cTarget;
        // Preguntamos al usuario la posicion inical del target
        System.out.println("A continuacion ingrese las coordenadas iniciales del Target: ");
        while (true) {
            try {
                System.out.println("Ingrese la fila del target (1 a 5): ");
                fTarget = in.nextInt() -1;
                System.out.println("Ingrese la columna del target (1 a 5): ");
                cTarget = in.nextInt() -1;
                // Validamos que las coordenadas esten dentro del rango
                if (fTarget <= 4 && fTarget >= 0 && cTarget <= 4 && cTarget >= 0) {
                    // Nos aseguramos de que no repita coordenadas ya utilizadas
                    if (matriz[fTarget][cTarget].equalsIgnoreCase("-")) {
                        // Ingresamos el target a la matriz
                        matriz[fTarget][cTarget] = "T";
                        break;
                    } else {
                        System.out.println("Error: Coordenadas ya utilizadas.");
                    }
                } else {
                    System.out.println("Ingrese valores validos.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Texto identificado, ingrese unos valores válidos.");
                in.nextLine();
            }
        }
    }

    /**
     * Metodo encargado de preguntar al usuario la cantidad y posicion de los muros
     * para luego asignarlos a la matriz principal.
     *
     * @param matriz principal utilizada en el sistema
     */
    public static void posicionMuros(String[][] matriz) {
        int cantidadMuros;
        // Preguntamos al usuario cuantos muros quiere poner (max 3)
        while (true) {
            try {
                System.out.println("Ingrese la cantidad de muros a posicionar (0 a 3)");
                cantidadMuros = in.nextInt();
                // Validamos que haya ingresado una cantidad correcta
                if (cantidadMuros <= 3 && cantidadMuros >= 0) {
                    break;
                } else {
                    System.out.println("Ingrese una cantidad válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Texto identificado, ingrese unos valores válidos.");
                in.nextLine();
            }
        }
        try {
            // Preguntamos posicion de cada muro
            for (int i = 0; i < cantidadMuros; i++) {
                while (true) {
                    System.out.println("Ingrese la fila del muro " + (i + 1) + " (1 a 5): ");
                    int fMuro = in.nextInt()-1;
                    System.out.println("Ingrese la columna del muro " + (i + 1) + " (1 a 5): ");
                    int cMuro = in.nextInt()-1;
                    // Validamos que haya ingresado coordenadas dentro del rango
                    if (fMuro <= 4 && fMuro >= 0 && cMuro <= 4 && cMuro >= 0) {
                        // Ver si esa posicion esta vacia
                        if (matriz[fMuro][cMuro].equalsIgnoreCase("-")) {
                            matriz[fMuro][cMuro] = "X";
                            break;
                        } else {
                            System.out.println("Error: Coordenadas ya ocupadas, ingrese unas diferentes.");
                        }
                    } else {
                        System.out.println("Ingrese unas coordenadas validas.");
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Texto identificado, ingrese unos valores válidos.");
            in.nextLine();

        }
    }

    /**
     * Metodo encargado de configurar los tipos de espejos que el usuario escoja.
     *
     * @return array con los 4 tipos de espejo seleccionados.
     */
    public static String[] tipoEspejos() {
        int cantEspejoA;
        String[] tiposEspejo = new String[4];
        while (true) {
            try {
                // Solicitar al usuario la cantidad de cada tipo de espejo
                System.out.println("Cuántos espejos del tipo (/) o (\\) desea ingresar (total espejos máximo 4):  ");
                System.out.println("Cantidad de espejos (/):");
                cantEspejoA = in.nextInt();
                if (cantEspejoA <= 4 && cantEspejoA >= 0) {
                    break;
                } else {
                    System.out.println("Ingrese una cantidad válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Texto identificado, ingrese un valor válido.");
                in.nextLine();
            }
        }
        // Guardamos los espejos en un array
        for (int i = 0; i < tiposEspejo.length; i++) {
            if (i < cantEspejoA) {
                tiposEspejo[i] = "/";
            } else {
                tiposEspejo[i] = "\\";
            }
        }
        return tiposEspejo;
    }

    /**
     * Metodo encargado de simular la trayectoria del laser.
     *
     * @param matriz principal del sistema
     * @return true si se logró alcanazar el target,
     * false si fallo
     *
     */
    /**
     * Metodo encargado de simular la trayectoria del laser.
     *
     * @param matriz principal del sistema
     * @return true si se logró alcanzar el target, false si fallo
     */
    public static boolean trayectoriaLaser(String[][] matriz) {
        int fInicial = fLaser;
        int cInicial = cLaser;
        int direccion = 1; // 1: Este, 2: Oeste, 3: Norte, 4: Sur

        int posicion = 0;
        int posicionMax = 100; // Evita bucles infinitos en circuitos cerrados

        while (posicion < posicionMax) {
            // 1. Validar límites ANTES de leer cualquier posición en la matriz
            if (fInicial < 0 || fInicial >= 5 || cInicial < 0 || cInicial >= 5) {
                return false;
            }

            // 2. Obtener el elemento de la celda actual
            String casilla = matriz[fInicial][cInicial];

            // 3. Evaluar condiciones de Victoria o Fracaso
            if (casilla.equalsIgnoreCase("T")) {
                return true; // Éxito: Llegó al objetivo
            }
            // Permite estar en "L" solo en el paso 0. Si regresa a "L" después, es un bucle/fallo.
            if (casilla.equalsIgnoreCase("X") || (casilla.equalsIgnoreCase("L") && posicion > 0)) {
                return false;
            }

            // 4. Lógica de refracción (Modifica la dirección en la celda actual)
            if (casilla.equalsIgnoreCase("/")) {
                if (direccion == 1)      direccion = 3; // Oeste -> Norte
                else if (direccion == 2) direccion = 4; // Este -> Sur
                else if (direccion == 3) direccion = 1; // Sur -> Este
                else if (direccion == 4) direccion = 2; // Norte -> Oeste
            }
            else if (casilla.equalsIgnoreCase("\\")) {
                if (direccion == 1)      direccion = 4; // Oeste -> Sur
                else if (direccion == 2) direccion = 3; // Este -> Norte
                else if (direccion == 3) direccion = 1; // Sur -> Oeste
                else if (direccion == 4) direccion = 2; // Norte -> Este
            }

            // 5. MOVIMIENTO: Se calcula la siguiente celda al final de la iteración actual
            if (direccion == 1) {
                cInicial++;
            } else if (direccion == 2) {
                cInicial--;
            } else if (direccion == 3) {
                fInicial--;
            } else if (direccion == 4) {
                fInicial++;
            }

            posicion++;
        }
        return false;
    }

    /**
     * Metodo que mediante backtracking posiciona los espejos configurados
     * por el usuario anteriormente.
     * @param tiposEspejos Array con los espejos a posicionar
     * @param indexEspejo Indice del espejo que estamos intentando colocar
     * @param matriz principal utilizada en el sistema.
     */
    public static boolean posicionEspejos(String[][] matriz, String[] tiposEspejos, int indexEspejo) {
        // Caso base
        if (indexEspejo == 4) {
            return trayectoriaLaser(matriz);
        }
        for (int f = 0; f<5; f++){
            for (int c = 0; c<5; c++){
                // colocamos el espejo solo en las celdas vacias
                if(matriz[f][c].equals("-")){
                    matriz[f][c] = tiposEspejos[indexEspejo]; // ponemos el espejo
                    // Llamada recursiva para el siguiente espejo
                    if (posicionEspejos(matriz, tiposEspejos, indexEspejo + 1)) {
                        return true;
                    }
                    // Backtracking: quitamos el espejo
                    matriz[f][c] = "-";
                }
            }
        }
        return false;
    }

}