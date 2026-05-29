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
import java.util.Scanner;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        // Creando la matriz
        String[][] Matriz = {
                {"-","-","-","-","-"},
                {"-","-","-","-","-"},
                {"-","-","-","-","-"},
                {"-","-","-","-","-"},
                {"-","-","-","-","-"}
        };
        for (String[] row : Matriz) {
            for (String cell : row) { {
                System.out.print(cell + " ");
            }
            System.out.println();
            }
        }
    }
}