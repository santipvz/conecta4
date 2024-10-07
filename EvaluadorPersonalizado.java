// EvaluadorPersonalizado.java

public class EvaluadorPersonalizado extends Evaluador {

    // Se pueden ajustar las ponderaciones de las funciones de evaluación según sea necesario
    public static float PONDERACION_FICHAS_CONSECUTIVAS = 1.0f;
    public static float PONDERACION_POSIBLES_ALINEACIONES = 1.0f;
    public static float PONDERACION_FICHAS_CENTRALES = 1.0f;
    public static float PONDERACION_FICHAS_DIAGONALES_ASCENDENTES = 1.0f;



    @Override
    public int valoracion(Tablero tablero, int jugador) {
        int valoracionTotal = 0;
        valoracionTotal += evaluarFichasConsecutivas(tablero, jugador) * PONDERACION_FICHAS_CONSECUTIVAS;
        valoracionTotal += evaluarPosiblesAlineaciones(tablero, jugador) * PONDERACION_POSIBLES_ALINEACIONES;
        valoracionTotal += evaluarFichasCentrales(tablero, jugador) * PONDERACION_FICHAS_CENTRALES;
        valoracionTotal += evaluarFichasDiagonalAscendente(tablero, jugador) * PONDERACION_FICHAS_DIAGONALES_ASCENDENTES;
        return valoracionTotal;
    }

    // Función de evaluación 1: Evalúa el número de fichas consecutivas en una fila
    public static int evaluarFichasConsecutivas(Tablero tablero, int jugador) {
        int valoracion = 0;
        for (int fila = 0; fila < Tablero.NFILAS; fila++) {
            for (int columna = 0; columna <= Tablero.NCOLUMNAS - Tablero.NOBJETIVO; columna++) {
                int contador = 0;
                for (int k = 0; k < Tablero.NOBJETIVO; k++) {
                    if (tablero.getCasilla(columna + k, fila) == jugador) {
                        contador++;
                    }
                }
                if (contador == Tablero.NOBJETIVO) {
                    valoracion += 1; // Ponderación arbitraria
                }
            }
        }
        return valoracion;
    }

    // Función de evaluación 2: Evalúa el número de posibles alineaciones para ganar
    public static int evaluarPosiblesAlineaciones(Tablero tablero, int jugador) {
        int valoracion = 0;
        for (int columna = 0; columna < Tablero.NCOLUMNAS; columna++) {
            for (int fila = 0; fila < Tablero.NFILAS; fila++) {
                if (tablero.getCasilla(columna, fila) == 0) {
                    int contador = 0;
                    for (int k = 0; k < Tablero.NOBJETIVO; k++) {
                        if (tablero.esCasillaValida(columna + k, fila)) {
                            if (tablero.getCasilla(columna + k, fila) == jugador) {
                                contador++;
                            }
                        }
                    }
                    if (contador == Tablero.NOBJETIVO - 1) {
                        valoracion -= 1; // Ponderación arbitraria
                    }
                }
            }
        }
        return valoracion;
    }

    // Función de evaluación 3: Evalúa el número de fichas en el centro del tablero
    public static int evaluarFichasCentrales(Tablero tablero, int jugador) {
        int valoracion = 0;
        for (int columna = 0; columna < Tablero.NCOLUMNAS; columna++) {
            for (int fila = 0; fila < Tablero.NFILAS; fila++) {
                if (fila >= 2 && fila <= 3) {
                    if (tablero.getCasilla(columna, fila) == jugador) {
                        valoracion += 2; // Ponderación arbitraria
                    }
                }
            }
        }
        return valoracion;
    }

    // Función de evaluación 4: Evalúa el número de fichas en diagonal ascendente
    public static int evaluarFichasDiagonalAscendente(Tablero tablero, int jugador) {
        int valoracion = 0;
        for (int columna = 0; columna <= Tablero.NCOLUMNAS - Tablero.NOBJETIVO; columna++) {
            for (int fila = 0; fila <= Tablero.NFILAS - Tablero.NOBJETIVO; fila++) {
                int contador = 0;
                for (int k = 0; k < Tablero.NOBJETIVO; k++) {
                    if (tablero.getCasilla(columna + k, fila + k) == jugador) {
                        contador++;
                    }
                }
                if (contador == Tablero.NOBJETIVO) {
                    valoracion -= 2; // Ponderación arbitraria
                }
            }
        }
        return valoracion;
    }

}
