/**
 * TableroSwingRecursos.java
 * 19-abr-2012
 * @author ccatalan
 */
package utilidades;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import org.w3c.dom.Document;

/**
 * Agrupa valores para un Menú Swing
 */
class Menu {
  private String texto;
  private String atajo;
  
 /**
  * Menu
  */
  Menu(String texto, String atajo) {
    this.texto = texto;
    this.atajo = atajo;
  }

 /**
  * getTexto
  */  
  public String getTexto() {
    return texto;
  }    
  
 /**
  * getAtajo
  */  
  public String getAtajo() {
    return atajo;
  }
}

/**
 * Lee recursos de una aplicación Swing
 */
public class RecursosAppSwing {
  public static final String RUTA_RECURSOS = "/recursos/";
  
  public static final String TITULO = "titulo";
  public static final String VERSION = "version";
  public static final String AUTOR = "autor";
  public static final String FICH_ICONO_HUECO = "iconoHueco";
  public static final String FICH_ICONO_COLOR1 = "iconoColor1";
  public static final String FICH_ICONO_COLOR2 = "iconoColor2";
   
  private static final String MENU = "menu";
  private static final String ID = "id";
  private static final String MENU_ITEM = "menuItem";
  private static final String TEXTO = "texto";
  private static final String ATAJO = "atajo";
  private static final String ETIQUETA_APP = "etiqueta";
  private static final String ETIQUETA_SISTEMA = "etiquetaSistema";
  
  private Map<String, String> generales = new HashMap<>();
  private Map<String, Menu> menus = new HashMap<>();
  private Map<String, Menu> menusItem = new HashMap<>();
  private Map<String, String> etiquetas = new HashMap<>();
  
  private static RecursosAppSwing instancia = null;  // es singleton
  private LectorXML recursos;
  private ImageIcon iconoHueco;
  private ImageIcon iconoColor1;
  private ImageIcon iconoColor2;
     
 /**
  * RecursosAppSwing
  */  
  private RecursosAppSwing(String fichRecursos) {
    try {      
      recursos = new LectorXML(this.getClass().getResourceAsStream(
        RecursosAppSwing.RUTA_RECURSOS + fichRecursos));
      
      cargaGenerales();
      cargaMenus();
      cargaMenusItem();
      cargaEtiquetasApp();  
      cargaEtiquetasSistema();
      
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RecursosAppSwing.class.getName()).log(Level.SEVERE, 
                                                             null, ex);
      System.exit(1);
    } catch (Exception ex) {
      Logger.getLogger(RecursosAppSwing.class.getName()).log(Level.SEVERE, 
                                                             null, ex);
      System.exit(1);
    }   
  }

 /**
  * instancia
  */        
  public static synchronized RecursosAppSwing instancia(String fichRecursos) {
    if (instancia == null)
      instancia = new RecursosAppSwing(fichRecursos);
    return instancia;
  }
 
 /**
  * cargaApp
  */    
  private void cargaGenerales() {
    generales.put(TITULO, recursos.getValorEtiqueta(TITULO));  
    generales.put(VERSION, recursos.getValorEtiqueta(VERSION));
    generales.put(AUTOR, recursos.getValorEtiqueta(AUTOR));
    
    iconoHueco = new ImageIcon(this.getClass().getResource(
      RecursosAppSwing.RUTA_RECURSOS + 
      recursos.getValorEtiqueta(FICH_ICONO_HUECO)));
    iconoColor1 = new ImageIcon(this.getClass().getResource(
      RecursosAppSwing.RUTA_RECURSOS + 
      recursos.getValorEtiqueta(FICH_ICONO_COLOR1))); 
    iconoColor2 = new ImageIcon(this.getClass().getResource(
      RecursosAppSwing.RUTA_RECURSOS + 
      recursos.getValorEtiqueta(FICH_ICONO_COLOR2))); 
  }
  
 /**
  * cargaMenus
  */    
  private void cargaMenus() {
    List<String> menusID = recursos.getValoresElementoEtiqueta(MENU, ID);
    List<String> menusTexto = recursos.getValoresElementoEtiqueta(MENU, TEXTO);
    
    for(int i = 0; i < menusID.size(); i++)
      menus.put(menusID.get(i), new Menu(menusTexto.get(i), null));      
  }
  
 /**
  * cargaMenusItem
  */  
  private void cargaMenusItem() {
    List<String> menusItemId = 
      recursos.getValoresElementoEtiqueta(MENU_ITEM, ID);
    List<String> menusItemTexto = 
      recursos.getValoresElementoEtiqueta(MENU_ITEM, TEXTO);
    List<String> menusItemAtajo = 
      recursos.getValoresElementoEtiqueta(MENU_ITEM, ATAJO);
    
    for(int i = 0; i < menusItemId.size(); i++)
      menusItem.put(menusItemId.get(i), 
                    new Menu(menusItemTexto.get(i), menusItemAtajo.get(i)));      
  }
  
 /**
  * cargaEtiquetasApp
  */  
  private void cargaEtiquetasApp() {
    List<Document> etiquetasDocumento = 
      recursos.getSubDocumentosEtiqueta(ETIQUETA_APP);
    for(Document etiqueta: etiquetasDocumento) {
      etiquetas.put(LectorXML.getValorEtiqueta(etiqueta, ID), 
                    LectorXML.getValorEtiqueta(etiqueta, TEXTO));
    }
  }
  
 /**
  * cargaEtiquetasSistema
  */   
  private void cargaEtiquetasSistema() {
    List<Document> etiquetasDocumento = 
      recursos.getSubDocumentosEtiqueta(ETIQUETA_SISTEMA);
    for(Document etiqueta: etiquetasDocumento) {
      UIManager.put(LectorXML.getValorEtiqueta(etiqueta, ID), 
                    LectorXML.getValorEtiqueta(etiqueta, TEXTO));
    }
  }
 
 /**
  * getApp
  */  
  public String getGeneral(String id) {
    return generales.get(id);   
  }
  
 /**
  * getIconoImageApp
  */  
  public Image getIconoImagenApp() {
    return iconoHueco.getImage();
  }

 /**
  * getIconoApp
  */  
  public Icon getIconoApp() {
    return iconoHueco;
  }  
   /**
  * getIconoImageApp
  */  
  public Image getIconoImagenColor1() {
    return iconoColor1.getImage();
  }

 /**
  * getIconoApp
  */  
  public Icon getIconoColor1() {
    return iconoColor1;
  }  
     /**
  * getIconoImageApp
  */  
  public Image getIconoImagenColor2() {
    return iconoColor2.getImage();
  }

 /**
  * getIconoApp
  */  
  public Icon getIconoColor2() {
    return iconoColor2;
  }
 
 /**
  * getTextoMenu
  */  
  public String getTextoMenu(String id) {
    return menus.get(id).getTexto();
  }
   
 /**
  * getTextoMenuItem
  */  
  public String getTextoMenuItem(String id) {
    return menusItem.get(id).getTexto();
  }
  
 /**
  * getAtajoMenuItem
  */  
  public char getAtajoMenuItem(String id) {
    return menusItem.get(id).getAtajo().charAt(0);
  }
  
 /**
  * getEtiqueta
  */  
  public String getEtiqueta(String id) {
    return etiquetas.get(id);
  }
}