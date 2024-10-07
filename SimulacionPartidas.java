// SimulacionPartidas.java
import java.util.Random;

public class SimulacionPartidas {

    public static final long NUMERO_PARTIDAS = 1000;
    
    public static void main(String[] args) {
        long t_i = System.currentTimeMillis();

        System.out.println("Simulacion Evaluador personalizado SIN ajuste automático vs Evaluador aleatorio\n");
        // Simulación de Evaluador personalizado SIN ajuste automático vs Evaluador Aleatorio
        simularPartidas(new EvaluadorPersonalizado(), false, new EvaluadorAleatorio(), false);
        // Ajustar los pesos automáticamente antes de cada simulación
        EvaluadorPonderado.ajustarPesos();
        System.out.println("Simulacion Evaluador personalizado SIN ajuste automático vs Evaluador personalizado CON ajuste automático\n");
        // Simulación de Evaluador personalizado SIN ajuste automático vs Evaluador personalizado CON ajuste automático
        simularPartidas(new EvaluadorPersonalizado(), false, new EvaluadorPonderado(), true);
                
        // Ajustar los pesos automáticamente antes de cada simulación
        EvaluadorPonderado.ajustarPesos();
        System.out.println("Simulacion Evaluador personalizado CON ajuste automático vs Evaluador aleatorio\n");
        // Simulación de Evaluador personalizado CON ajuste automático vs Evaluador Aleatorio
        simularPartidas(new EvaluadorPonderado(), true, new EvaluadorAleatorio(), false);

        System.out.println("Tiempo TOTAL: "+ (System.currentTimeMillis() - t_i) + " ms");
    }

    public static void simularPartidas(Evaluador evaluador1, boolean ajusteAutomatico1, Evaluador evaluador2, boolean ajusteAutomatico2) {
        // Contadores para almacenar los resultados
        int victoriasEvaluador1 = 0;
        int empatesEvaluador1 = 0;
        int victoriasEvaluador2 = 0;


        long tiempoEvaluador1 = 0;
        int numNodosEvaluador1 = 0;
        int numMovimientos1 = 0;
        long tiempoEvaluador2 = 0;
        int numNodosEvaluador2 = 0;
        int numMovimientos2 = 0;

        int profundidad = 4;
        // Simulación de las partidas
        for (int i = 0; i < NUMERO_PARTIDAS; i++) {
            // Crear un nuevo tablero
            Tablero tablero = new Tablero();

            // Crear los jugadores con las estrategias respectivas
            Jugador jugador1 = new Jugador(1);
            Estrategia estrategia1 = ajusteAutomatico1 ? new EstrategiaAlfaBeta(new EvaluadorPersonalizado(), profundidad) : new EstrategiaAlfaBeta(evaluador1, profundidad);
            jugador1.establecerEstrategia(estrategia1);

            Jugador jugador2 = new Jugador(2);
            Estrategia estrategia2 = ajusteAutomatico2 ? new EstrategiaAlfaBeta(new EvaluadorPersonalizado(), profundidad) : new EstrategiaAlfaBeta(evaluador2, profundidad);
            jugador2.establecerEstrategia(estrategia2);

            // Realizar la partida
            int resultado = jugarPartida(tablero, jugador1, jugador2);

            // Actualizar los contadores de resultados
            if (resultado == 1) {
                victoriasEvaluador1++;
                tiempoEvaluador1 += jugador1.getDatos()[2];
                numNodosEvaluador1 += jugador1.getDatos()[0];
                numMovimientos1 += jugador1.getDatos()[1];
                numMovimientos2 += jugador2.getDatos()[1];
            } else if (resultado == -1) {
                victoriasEvaluador2++;
                tiempoEvaluador2 += jugador2.getDatos()[2];
                numNodosEvaluador2 += jugador2.getDatos()[0];
                numMovimientos1 += jugador1.getDatos()[1];
                numMovimientos2 += jugador2.getDatos()[1];
            } else {
                empatesEvaluador1++;
                tiempoEvaluador1 += jugador1.getDatos()[2];
                numNodosEvaluador1 += jugador1.getDatos()[0];
                tiempoEvaluador2 += jugador2.getDatos()[2];
                numNodosEvaluador2 += jugador2.getDatos()[0];
                numMovimientos1 += jugador1.getDatos()[1];
                numMovimientos2 += jugador2.getDatos()[1];
            }
        }

        // Mostrar los resultados
        System.out.println("Resultados de la simulación:");
        System.out.println("");
        System.out.println("Victorias Evaluador 1: " + victoriasEvaluador1);
        System.out.println("Victorias Evaluador 2: " + victoriasEvaluador2);
        System.out.println("Empates: " + empatesEvaluador1);
        System.out.println("");

        System.out.println("Tiempo medio Evaluador 1: " + tiempoEvaluador1 + " ms");
        System.out.println("Número medio de nodos expandidos Evaluador 1: " + numNodosEvaluador1 / NUMERO_PARTIDAS);
        System.out.println("Número de movimientos medio Evaluador 1: " + numMovimientos1 / NUMERO_PARTIDAS);
        System.out.println("");
        System.out.println("Tiempo medio Evaluador 2: " + tiempoEvaluador2 + " ms");
        System.out.println("Número medio de nodos expandidos Evaluador 2: " + numNodosEvaluador2 / NUMERO_PARTIDAS);
        System.out.println("Número de movimientos medio Evaluador 2: " + numMovimientos2 / NUMERO_PARTIDAS);
        System.out.println("");
    }

    public static int jugarPartida(Tablero tablero, Jugador jugador1, Jugador jugador2) {
        Random rand = new Random();
        int turno = rand.nextInt(2) + 1; // Se elige aleatoriamente quién empieza

        while (!tablero.esFinal()) {
            int jugada;
            if (turno == 1) {
                jugada = jugador1.obtenerJugada(tablero);
            } else {
                jugada = jugador2.obtenerJugada(tablero);
            }

            if (tablero.esCasillaValida(jugada, 0)) {
                tablero.anadirFicha(jugada, turno);
                tablero.obtenerGanador();
                turno = turno == 1 ? 2 : 1; // Cambiar de turno
            }
        }

        if (tablero.ganaJ1()) {
            return 1; // Jugador 1 gana
        } else if (tablero.ganaJ2()) {
            return -1; // Jugador 2 gana
        } else {
            return 0; // Empate
        }
    }
}



