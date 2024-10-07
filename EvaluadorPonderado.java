// EvaluadorPersonalizado.java
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class EvaluadorPonderado extends EvaluadorPersonalizado {
    public static float PONDERACION_FICHAS_CONSECUTIVAS = 1.0f;
    public static float PONDERACION_POSIBLES_ALINEACIONES = 1.0f;
    public static float PONDERACION_FICHAS_CENTRALES = 1.0f;
    public static float PONDERACION_FICHAS_DIAGONALES_ASCENDENTES = 1.0f;

    public static final int NUMERO_ITERACIONES = 100; // Número de iteraciones para ajustar los pesos
    public static final double FACTOR_AJUSTE = 0.1; // Factor de ajuste para modificar los pesos

    private static void guardarPesosEnArchivo(float[] pesos) {
        try {
            FileWriter writer = new FileWriter("pesos_ajustados.txt");
            writer.write("Ponderación Fichas Consecutivas: " + pesos[0] + "\n");
            writer.write("Ponderación Posibles Alineaciones: " + pesos[1] + "\n");
            writer.write("Ponderación Fichas Centrales: " + pesos[2] + "\n");
            writer.write("Ponderación Fichas Diagonales Ascendentes: " + pesos[3] + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo.");
            e.printStackTrace();
        }
    }
    
    // Método para leer los pesos desde el archivo
    private static void leerPesosDesdeArchivo(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            int indice = 0;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                float valor = Float.parseFloat(partes[1].trim());
                valor = Math.min(1, Math.max(-1, valor));
                // Normalizar el valor entre -1 y 1
                switch (indice) {
                    case 0:
                        PONDERACION_FICHAS_CONSECUTIVAS = valor;
                        break;
                    case 1:
                        PONDERACION_POSIBLES_ALINEACIONES = valor;
                        break;
                    case 2:
                        PONDERACION_FICHAS_CENTRALES = valor;
                        break;
                    case 3:
                        PONDERACION_FICHAS_DIAGONALES_ASCENDENTES = valor;
                        break;
                }
                indice++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo.");
            e.printStackTrace();
        }
    }

    @Override
    public int valoracion(Tablero tablero, int jugador) {
        int valoracionTotal = 0;
        valoracionTotal += evaluarFichasConsecutivas(tablero, jugador) * PONDERACION_FICHAS_CONSECUTIVAS;
        valoracionTotal += evaluarPosiblesAlineaciones(tablero, jugador) * PONDERACION_POSIBLES_ALINEACIONES;
        valoracionTotal += evaluarFichasCentrales(tablero, jugador) * PONDERACION_FICHAS_CENTRALES;
        valoracionTotal += evaluarFichasDiagonalAscendente(tablero, jugador) * PONDERACION_FICHAS_DIAGONALES_ASCENDENTES;
        return valoracionTotal;
    }

    public static void ajustarPesos() {
        leerPesosDesdeArchivo("pesos_ajustados.txt");
        float mejorValoracion = Float.MIN_VALUE;
        float[] mejoresPesos = {PONDERACION_FICHAS_CONSECUTIVAS, PONDERACION_POSIBLES_ALINEACIONES, PONDERACION_FICHAS_CENTRALES, PONDERACION_FICHAS_DIAGONALES_ASCENDENTES};

        for (int i = 0; i < NUMERO_ITERACIONES; i++) {
            float[] nuevosPesos = generarNuevosPesos(mejoresPesos);
            setPonderaciones(nuevosPesos);

            // Evaluar el desempeño con los nuevos pesos
            float valoracion = evaluarDesempeno();

            // Comparar con el mejor desempeño hasta ahora
            if (valoracion > mejorValoracion) {
                mejorValoracion = valoracion;
                mejoresPesos = nuevosPesos;
            }
        }

        // Establecer los mejores pesos
        setPonderaciones(mejoresPesos);

        // Guardar los pesos ajustados en un archivo
        guardarPesosEnArchivo(mejoresPesos);
    }

    private static float[] generarNuevosPesos(float[] pesosActuales) {
        float[] nuevosPesos = new float[pesosActuales.length];
        for (int i = 0; i < pesosActuales.length; i++) {
            // Incrementar o decrementar el peso actual por un factor
            nuevosPesos[i] = (float) (pesosActuales[i] * (1 + (Math.random() * 2 - 1) * FACTOR_AJUSTE));
        }
        return nuevosPesos;
    }

    private static void setPonderaciones(float[] pesos) {
        PONDERACION_FICHAS_CONSECUTIVAS = pesos[0];
        PONDERACION_POSIBLES_ALINEACIONES = pesos[1];
        PONDERACION_FICHAS_CENTRALES = pesos[2];
        PONDERACION_FICHAS_DIAGONALES_ASCENDENTES = pesos[3];
    }

    private static int evaluarDesempeno() {
        int totalPartidas = 15; // Número total de partidas para evaluar el desempeño
        int victoriasActuales = 0;

        for (int i = 0; i < totalPartidas; i++) {
            // Crear una nueva instancia de Tablero para cada partida
            Tablero tablero = new Tablero();
            int resultado = simularPartida(tablero); // Simular la partida actual

            if (resultado == 1) {
                victoriasActuales++;
            }
        }

        // Calcular el porcentaje de victorias
        double porcentajeVictorias = (double) victoriasActuales / totalPartidas * 100;

        // Devolver un valor que represente el desempeño relativo de los pesos actuales
        // Por simplicidad, devolvemos el porcentaje de victorias
        return (int) porcentajeVictorias;
    }

    private static int simularPartida(Tablero tablero) {
        // Crear los jugadores con las heurísticas respectivas
        Jugador jugadorAleatorio = new Jugador(1);
        jugadorAleatorio.establecerEstrategia(new EstrategiaAlfaBeta(new EvaluadorPersonalizado(), 4));

        Jugador jugadorPersonalizado = new Jugador(2);
        jugadorPersonalizado.establecerEstrategia(new EstrategiaAlfaBeta(new EvaluadorPersonalizado(), 4));

        // Inicializar el turno aleatoriamente
        Random rand = new Random();
        int turno = rand.nextInt(2) + 1;

        while (!tablero.esFinal()) {
            int jugada;
            if (turno == 1) {
                jugada = jugadorAleatorio.obtenerJugada(tablero);
            } else {
                jugada = jugadorPersonalizado.obtenerJugada(tablero);
            }

            if (tablero.esCasillaValida(jugada, 0)) {
                tablero.anadirFicha(jugada, turno);
                tablero.obtenerGanador();
                turno = turno == 1 ? 2 : 1; // Cambiar de turno
            }
        }

        // Devolver el resultado de la partida
        if (tablero.ganaJ1()) {
            return 1; // Jugador 1 gana
        } else if (tablero.ganaJ2()) {
            return -1; // Jugador 2 gana
        } else {
            return 0; // Empate
        }
    }


}
