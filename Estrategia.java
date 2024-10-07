/*
 * Estrategia.java
 *
 * Created on 10 de enero de 2004, 16:57
 */

/**
 *
 * @author  ribadas
 */
public abstract class Estrategia {
    /* 
     * Superclase del patron estrategia, el ofrece interfaz comun de todas las
     * estrategias (funcion buscarMovimiento).
     */
   
    /** Creates a new instance of Estrategia */
    public int num_nodos = 0;
    public int num_movimientos = 0;
    public double tiempo_total = 0;

    public Estrategia() {
    }
    
    public abstract int buscarMovimiento(Tablero tablero, int jugador);

}
