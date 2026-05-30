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

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;

public class Main {
    static Scanner in = new Scanner(System.in);

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


    }

    /**
     * Metodo encargado de imprimir la matriz.
     *
     * @param matriz principal.
     */
    public static void imprimirMatriz(String[][] matriz) {
        for (String[] row : matriz) {
            for (String cell : row) {
                System.out.print(cell + " ");
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
        int cLaser;
        int fLaser;
        //Preguntamos al usuario la posicion inicial del Laser
        System.out.println("A continuacion ingrese las coordenadas iniciales del Laser: ");
        while (true) {
            try {
                System.out.println("Ingrese la fila del laser (1 a 5): ");
                fLaser = in.nextInt();
                System.out.println("Ingrese la columna del laser (1 a 4): ");
                cLaser = in.nextInt();
                // Validamos que las coordenadas esten dentro del rango
                if (cLaser <= 4 && cLaser >= 1 && fLaser <= 5 && fLaser >= 1) {
                    // Ingresamos el laser a la matriz
                    matriz[fLaser - 1][cLaser - 1] = "L";
                    break;
                } else {
                    System.out.println("Ingrese un valor válido.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Texto identificado, ingrese unos valores válidos.");
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
                fTarget = in.nextInt();
                System.out.println("Ingrese la columna del target (1 a 5): ");
                cTarget = in.nextInt();
                // Validamos que las coordenadas esten dentro del rango
                if (fTarget <= 5 && fTarget >= 1 && cTarget <= 5 && cTarget >= 1) {
                    // Nos aseguramos de que no repita coordenadas ya utilizadas
                    if (matriz[fTarget - 1][cTarget - 1].equalsIgnoreCase("-")) {
                        // Ingresamos el target a la matriz
                        matriz[fTarget - 1][cTarget - 1] = "T";
                        break;
                    } else {
                        System.out.println("Error: Coordenadas ya utilizadas.");
                    }
                } else {
                    System.out.println("Ingrese unos valores validos.");
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
                    int fMuro = in.nextInt();
                    System.out.println("Ingrese la columna del muro " + (i + 1) + " (1 a 5): ");
                    int cMuro = in.nextInt();
                    // Validamos que haya ingresado coordenadas dentro del rango
                    if (fMuro <= 5 && fMuro >= 1 && cMuro <= 5 && cMuro >= 1) {
                        // Ver si esa posicion esta vacia
                        if (matriz[fMuro - 1][cMuro - 1].equalsIgnoreCase("-")) {
                            matriz[fMuro - 1][cMuro - 1] = "X";
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

}