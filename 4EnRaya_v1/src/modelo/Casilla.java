package modelo;

/**
 *
 * @author javi
 */
public class Casilla {
    private int fil;
    private int col;
    private String simbolo;
    
      /**
   * Casilla
   */
  Casilla(int fil, int col, String simbolo) {
    this.fil = fil;
    this.col = col;
    this.simbolo = simbolo;
  }
  
  /**
   * Casilla
   */
  Casilla(int fil, int col) {
    this(fil, col, ".");
  }

  /**
   * getCol
   */
  public int getCol() {
    return col;
  }
  
  /**
   * getFil
   */
  public int getFil() {
    return fil;
  }
  public String getSimbolo(){
      return simbolo;
  }
}
