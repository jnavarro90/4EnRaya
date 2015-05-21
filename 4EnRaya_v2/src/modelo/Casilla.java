package modelo;

import java.io.Serializable;

/**
 *
 * @author javi
 */
public class Casilla implements Serializable{
    private int fil;
    private int col;
    private String simbolo;
    
      /**
   * Casilla
   */
  public Casilla(int fil, int col, String simbolo) {
    this.fil = fil;
    this.col = col;
    this.simbolo = simbolo;
  }
  
  /**
   * Casilla
   */
  public Casilla(int fil, int col) {
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

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
  
  public String getSimbolo(){
      return simbolo;
  }
}
