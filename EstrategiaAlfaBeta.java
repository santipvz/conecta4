public class EstrategiaAlfaBeta extends Estrategia {
     
    private Evaluador _evaluador;
    private int _capaMaxima;

    private int _jugadorMAX;
   
    /** Creates a new instance of EstrategiaAlfaBeta */
    public EstrategiaAlfaBeta() {
    }
    
    public EstrategiaAlfaBeta(Evaluador evaluador, int capaMaxima) {
       this.establecerEvaluador(evaluador);  
       this.establecerCapaMaxima(capaMaxima);
    }
    
    public int buscarMovimiento(Tablero tablero, int jugador) {
        // Implementa la estrategia de búsqueda de movimiento

        num_movimientos++;
        double tiempo_inicio = System.currentTimeMillis();
        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col,valorSucesor;
        int mejorPosicion=-1;  // Movimiento nulo
        int mejorValor=_evaluador.MINIMO; // Minimo  valor posible 

        _jugadorMAX = jugador; // - anota el identificador del jugador que
                               //   tiene el papel de MAX
                               // - necesario para evaluar posiciones finales
        for (col=0; col<Tablero.NCOLUMNAS; col++) {
            if (movimientosPosibles[col]) { //se puede añadir ficha en columna
                // crear nuevo tablero y comprobar ganador
                nuevoTablero = (Tablero) tablero.clone();
                nuevoTablero.anadirFicha(col,jugador);
                nuevoTablero.obtenerGanador();

                // evaluarlo (OJO: cambiar jugador, establecer capa a 1)
                valorSucesor = ALFABETA(nuevoTablero,Jugador.alternarJugador(jugador), _evaluador.MINIMO, _evaluador.MAXIMO, 1);                
                nuevoTablero = null; // Ya no se necesita 
                
                // tomar mejor valor            
                if (valorSucesor >= mejorValor) {
                    mejorValor = valorSucesor;
                    mejorPosicion = col;
                }
            }
        }
        tiempo_total += System.currentTimeMillis() - tiempo_inicio;
        return(mejorPosicion);        
    }
    
    
    public int ALFABETA(Tablero tablero, int jugador, int alfa, int beta, int capa) {
        // Implementa la propagación de valores ALFABETA propiamente dicha
	    // a partir del segundo nivel (capa 1)
        num_nodos++;
        // Casos base
        if (tablero.hayEmpate()) {
            return(0);
        }
	    // la evaluacion de posiciones finales (caso base recursididad)
	    // se hace SIEMPRE desde la prespectiva de MAX
	    // -> se usa el identificador del jugador MAX (1 o 2) guardado
	    //    en la llamada a buscarMovimiento()
        if (tablero.esGanador(_jugadorMAX)){ // gana MAX
            return(_evaluador.MAXIMO);
        }
        if (tablero.esGanador(Jugador.alternarJugador(_jugadorMAX))){ // gana el otro
            return(_evaluador.MINIMO);
        } 
        if (capa == (_capaMaxima)) { // alcanza nivel maximo
            return(_evaluador.valoracion(tablero, _jugadorMAX));
        }

        // Recursividad sobre los sucesores
        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col, alfa_actual, beta_actual;
        int valor = 0;

        if (esCapaMAX(capa)) {
            alfa_actual = alfa;
            valor = _evaluador.MINIMO;
            for (col=0; col<Tablero.NCOLUMNAS; col++) {
                nuevoTablero = (Tablero) tablero.clone();
                nuevoTablero.anadirFicha(col, jugador);
                nuevoTablero.obtenerGanador();

                if (alfa_actual >= beta) {
                    break; //poda BETA
                }
                else {
                    valor = maximo2(valor, ALFABETA(nuevoTablero, Jugador.alternarJugador(jugador), alfa_actual, beta, capa + 1));
                    alfa_actual = maximo2(alfa_actual, valor);
                }

            }
        }
        else if (esCapaMIN(capa)) {
            beta_actual = beta;
            valor = _evaluador.MAXIMO; // valor máximo 
            for (col=0; col<Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) { //se puede añadir ficha en columna
                    // crear nuevo tablero y comprobar ganador
                    nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col,jugador);
                    nuevoTablero.obtenerGanador();

                    if (beta_actual <= alfa) {
                        break; // poda ALFA
                    }
                    else {
                        valor = minimo2(valor, ALFABETA(nuevoTablero,Jugador.alternarJugador(jugador), alfa, beta_actual, capa + 1));
                        beta_actual = minimo2(beta_actual, valor);
                    }
                }
            }
        }
        
        return(valor);
    }
    
   public void establecerCapaMaxima(int capaMaxima) {
      _capaMaxima = capaMaxima;
   }
   
   public void establecerEvaluador(Evaluador evaluador) {
      _evaluador = evaluador;
   }
    private static final boolean esCapaMIN(int capa) {
        return((capa % 2)==1); // es impar
    }
    
    private static final boolean esCapaMAX(int capa) {
        return((capa % 2)==0); // es par
    }
    
    private static final int maximo2(int v1, int v2) {
        if (v1 > v2)
            return(v1);
        else
            return(v2);
    }
    
    private static final int minimo2(int v1, int v2) {
        if (v1 < v2)
            return(v1);
        else
            return(v2);    
    }

    
}  // Fin clase EstartegiaALFABETA
