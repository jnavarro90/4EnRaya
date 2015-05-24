/**
 * CasillaVista.java
 * @date 22-mayo-2015
 * @author Javi Navarro
 * 
 * @version 2 
 */
package vista;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Una casilla del tablero es un JLabel
 */
public class CasillaVista extends JLabel {
  private int fil, col;  

  /**
   * Casilla
   */
  CasillaVista(int fil, int col, Icon icon) {
    this.fil = fil;
    this.col = col;
    setIcon(icon);
        
    setHorizontalAlignment(SwingConstants.CENTER);
  }
  
  /**
   * Casilla
   */
  CasillaVista(int fil, int col) {
    this(fil, col, null);
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

  /**
   * toString
   */  
  @Override
  public String toString() {
    return "Casilla{" + "fil=" + fil + ", col=" + col + '}';
  }
}
      
      