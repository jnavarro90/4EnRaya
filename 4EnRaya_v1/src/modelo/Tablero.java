package modelo;

import java.io.Serializable;

/**
 *
 * @author Javi Navarro
 * @version 1.2
 */
public class Tablero implements Serializable{
    private final int  MAX_COLUMNAS = 8;
    private final int MAX_FILAS = 7;
    private final int COL_MIN = 0;
    private final int COL_MAX = 6;
    private final int FIL_MAX = 6;
    private final int FIL_MIN = 1;
    private final int ERROR = -1;
    private Casilla casillas[][];
    private int movimientos;
    public Tablero() {
        this.casillas = new Casilla[MAX_FILAS][MAX_COLUMNAS];
        vaciar();
    }
    
    public void vaciar(){
        for (int i = MAX_FILAS-1;i>0;i--){
            for(int j = 0; j<MAX_COLUMNAS-1;j++){
                this.casillas[i][j] = new Casilla(i, j);
            }
        }
        this.movimientos = 0;
        
    }
    /**
     * El metodo jugada comprobara si se puede hacer la
     * jugada que el jugador ha introducido
     * @param col: columna en la que se quiere introducir la ficha
     * @param sim: El simbolo de la ficha
     * @return true o false dependiendo si se ha realizado bien o mal 
     * la insercion de la ficha
     */
    public int esJugadaValida(int col){
        if(col< MAX_COLUMNAS-1 && col >= 0){
            for(int i = 1; i < MAX_FILAS; i++){
               if(this.casillas [i][col].getSimbolo().equals(".")){
                    return i;
                }
            }
            return ERROR;
        }else{
            return ERROR;
        }
    }
    
    public void ponerFicha(int fil, int col, String sim){
        this.casillas[fil][col] = new Casilla(fil, col, sim);
        this.movimientoHecho();
    }
    
    /**
     * EL metodo finDePartida lleva toda la logica de comprobar 
     * si se ha hecho cuatro en raya en diagonal, 
     * horizontal o vertical.
     * @return true si en el final de la partida y false si no lo es
     */
    public boolean finDePartida(int fil, int col){
        return (comprVertical(fil, col) || comprHorizontal(fil, col) ||
                comprDiagonalDer(fil, col) || comprDiagonalIzq(fil, col));
    }

    /**
    * El metodo comprHorizontal comprueba si se ha producido
    * cuatro en raya
    * comprobando con dos iteradores en la posicon horizontal 
    * @return true si hay cuatro en raya y false si no lo hay
    */
    private boolean comprHorizontal(int fil, int col){
        int iteradorDer = col+1;
        int iteradorIzq = col-1;
        int numeroFichas = 1;
        boolean itIz = true;
        boolean itDe = true;
        while(true){
            //comprueba que este dento del tablero las comprobaciones
            if(iteradorDer > COL_MAX){
                itDe = false;
            }
            if(iteradorIzq < COL_MIN){
                itIz = false;
            }
            
            if(itDe){
                if(this.casillas[fil][col].getSimbolo().equals(
                        this.casillas[fil][iteradorDer].getSimbolo())){
                    numeroFichas++;
                    iteradorDer++;
                }else{
                    itDe = false;
                }
            }
            if(itIz){
                if(this.casillas[fil][col].getSimbolo().equals(
                        this.casillas[fil][iteradorIzq].getSimbolo())){
                    numeroFichas++;
                    iteradorIzq--;
                }else{
                    itIz = false;
                }
            }
            if(numeroFichas == 4){ 
      //Si ha encontrado 4 fichas iguales devuelve true
                return true;
            }else if(!itDe && !itIz){ 
      //Si no puede avanzar por la derecha ni por la izq sale del bucle
                break;
            }
        }
        return false;
    }
    
    /**
    * El metodo comprVertical comprueba si se ha producido cuatro en raya
    * comprobando con dos iteradores en la posicon vertical 
    * @return true si hay cuatro en raya y false si no lo hay
    */
    private boolean comprVertical(int fil, int col){
        int iteradorBot = fil-1;
        int numeroFichas = 1;
        boolean itbot = true;    
        while(true){
            
            //comprueba que este dento del tablero las comprobaciones
            if(iteradorBot < FIL_MIN){
                itbot = false;
            }
            if(itbot){
                if(this.casillas[fil][col].getSimbolo().equals(
                        this.casillas[iteradorBot][col].getSimbolo())){
                    numeroFichas++;
                    iteradorBot--;
                }else{
                    itbot = false;
                }
            }
            if(numeroFichas == 4){ 
//Si ha encontrado 4 fichas iguales devuelve true
                return true;
            }else if(!itbot){ 
//Si no puede avanzar hacia abajo sale del bucle
                break;
            }
        }
        return false;
    }
   
    /**
    * El metodo comprDiagonalDer comprueba si se ha producido
    * cuatro en raya comprobando con dos iteradores para 
    * derecha y otros dos para la derecha
    * en la posicon diagonal hacia la derecha 
    * @return true si hay cuatro en raya y false si no lo hay
    */
    private boolean comprDiagonalDer(int fil, int col){
        int iteradorDiaDerfil = fil+1;
        int iteradorDiaDercol = col+1;
        int iteradorDiaIzqfil = fil-1;
        int iteradorDiaIzqcol = col-1;
        int numeroFichas = 1;
        boolean ittop = true;
        boolean itbot = true;    
        while(true){
            
            //comprueba que este dento del tablero las comprobaciones
            if(iteradorDiaDercol > COL_MAX ||
                    iteradorDiaDerfil > FIL_MAX){
                ittop = false;
            }
            if(iteradorDiaIzqcol < COL_MIN  ||
                    iteradorDiaIzqfil < FIL_MIN){
                itbot = false;
            }
            
            if(ittop){
                if(this.casillas[fil][col].getSimbolo().equals(
                        this.casillas[iteradorDiaDerfil][iteradorDiaDercol]
                                .getSimbolo())){
                    numeroFichas++;
                    iteradorDiaDerfil++;
                    iteradorDiaDercol++;
                }else{
                    ittop = false;
                }
            }
            if(itbot){
                if(this.casillas[fil][col].getSimbolo().equals(
                        this.casillas[iteradorDiaIzqfil][iteradorDiaIzqcol]
                                .getSimbolo())){
                    numeroFichas++;
                    iteradorDiaIzqfil--;
                    iteradorDiaIzqcol--;
                }else{
                    itbot = false;
                }
            }
            if(numeroFichas == 4){ 
     //Si ha encontrado 4 fichas iguales devuelve true
                return true;
            }else if(!ittop && !itbot){ 
     //Si no puede avanzar por la derecha ni por la izq sale del bucle
                break;
            }
        }
        
        return false;
    }
    
    /**
    * El metodo comprDiagonalIzq comprueba si se ha producido 
    * cuatro en raya comprobando con dos iteradores
    * para derecha y otros dos para la derecha
    * en la posicon diagonal hacia la izquierda 
    * @return true si hay cuatro en raya y false si no lo hay
    */
    private boolean comprDiagonalIzq(int fil, int col){
        int iteradorDiaDerfil = fil-1;
        int iteradorDiaDercol = col+1;
        int iteradorDiaIzqfil = fil+1;
        int iteradorDiaIzqcol = col-1;
        int numeroFichas = 1;
        boolean ittop = true;
        boolean itbot = true;    
        while(true){
            //comprueba que este dento del tablero las comprobaciones
            if(iteradorDiaDercol > COL_MAX ||
                    iteradorDiaDerfil < FIL_MIN){
                ittop = false;
            }
            if(iteradorDiaIzqcol < COL_MIN  || 
                    iteradorDiaIzqfil > FIL_MAX){
                itbot = false;
            }
            
            if(ittop){
                if(this.casillas[fil][col].getSimbolo().equals(
                        this.casillas[iteradorDiaDerfil][iteradorDiaDercol]
                                .getSimbolo())){
                    numeroFichas++;
                    iteradorDiaDerfil--;
                    iteradorDiaDercol++;
                }else{
                    ittop = false;
                }
            }
            if(itbot){
                if(this.casillas[fil][col].getSimbolo().equals(
                        this.casillas[iteradorDiaIzqfil][iteradorDiaIzqcol]
                                .getSimbolo())){
                    numeroFichas++;
                    iteradorDiaIzqfil++;
                    iteradorDiaIzqcol--;
                }else{
                    itbot = false;
                }
            }
            if(numeroFichas == 4){ 
  //Si ha encontrado 4 fichas iguales devuelve true
                return true;
            }else if(!ittop && !itbot){ 
   //Si no puede avanzar por la derecha ni por la izq sale del bucle
                break; 
            }
        }
        return false;
    }
    
    
    /**
     * El metodo esEmpate comprueba si el juego acaba en empate
     * @return true si hay empate false si no hay empate
     */
    public boolean esEmpate(){
        int totalCasillas = (MAX_COLUMNAS-1)*(MAX_FILAS-1);
        return movimientos == (totalCasillas);
    }
    
    public Casilla[][] getCasillas() {
        return this.casillas;
    }
    
    public void movimientoHecho() {
        this.movimientos += 1;
    }
    
    /**
     * Dibuja el tablero en la pantalla
     * Este metodo lo haria la vista
     * @return  
     */
    
    public boolean dibujar(Jugador j1, Jugador j2){
        int ultimaFila = MAX_FILAS;
        System.out.println("|                    |Marcador");
        System.out.print("|     4 en Raya        | "+j1.getGanadas()+"\t"+
                j1.getNombre());
        if(j1.miTurno()){
            System.out.println("  <<");
        }else{
            System.out.println(" ");
        }
        System.out.print("|______________________| "+j2.getGanadas()+"\t"+
                j2.getNombre());
        if(!j1.miTurno()){
            System.out.println("  <<");
        }else{
            System.out.println(" ");
        }
        System.out.println("|  1  2  3  4  5  6  7 |");
        for (int i = MAX_FILAS-1;i>0;i--){
            if(ultimaFila!=i) System.out.print("| ");
            for(int j = 0; j<MAX_COLUMNAS-1;j++){
                System.out.print(" "+this.casillas[i][j].getSimbolo()+" ");             
            }
            if(ultimaFila!=i) System.out.print("|\n");
        }
        System.out.print("|----------------------|   Movimientos: "+
                this.movimientos+"\n");
        return true;
    }
}
