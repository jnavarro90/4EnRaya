/**
 * LectorXML.java
 * 19-abr-2012
 * @author ccatalan
 */
package utilidades;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Extrae valores de un fichero XML 
 */
public class LectorXML {
  private Document documento = null;
 
  /**
   * LectorXML
   */    
  //public LectorXML(String _nombreFicheroXML) throws Exception {
  public LectorXML(InputStream _nombreFicheroXML) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    documento = builder.parse(_nombreFicheroXML);
  }

  /**
   * LectorXML
   */       
  public LectorXML(Document documento) throws Exception {
    this.documento = documento;
  }

  /**
   * documento
   */       
  public Document documento() {
    return documento;
  }
   
   /**
   * getValorEtiqueta
   */  
  static public String getValorEtiqueta(Document documento, String etiqueta){
    String valor = "";
               
    NodeList nodos = documento.getElementsByTagName(etiqueta);
    Node nodo = nodos.item(0);    
    while ((nodo !=null) && (nodo.getNodeType() != Node.ELEMENT_NODE)){
      nodo = nodo.getNextSibling();
    }        
    if (nodo != null){
      nodo = nodo.getFirstChild(); 
      if (nodo != null){
        valor =(String)nodo.getNodeValue();
      }
    }      
    return valor;
  }

  /**
   * getValorEtiqueta
   */  
  public String getValorEtiqueta(String etiqueta){
    return getValorEtiqueta(documento, etiqueta);
  }
    
  /**
   * subDocumento
   */  
  static public Document subDocumento(Node nodo){
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    Document subDocumento = null;
    try {
      builder  = factory.newDocumentBuilder();
      subDocumento = builder.newDocument();       
      subDocumento.appendChild(subDocumento.importNode(nodo, true));             
    } catch (ParserConfigurationException ex) {
     Logger.getLogger(LectorXML.class.getName()).log(Level.SEVERE, null, ex);            
    }     
    return subDocumento;
  }   

  /**
   * getValoresElementoEtiqueta
   */   
  public List getValoresElementoEtiqueta(String etiqueta, String idPropiedad)  { 
    List<String> valores = new ArrayList<>();
    try {
      NodeList nodos = documento.getElementsByTagName(etiqueta);  
      for (int i = 0; i < nodos.getLength(); i++) {
        Document subDocumento = subDocumento(nodos.item(i));
        valores.add(this.getValorEtiqueta(subDocumento, idPropiedad));
      }
    } catch (Exception ex) {
       Logger.getLogger(LectorXML.class.getName()).log(Level.SEVERE, null, ex);           
    }
    return valores;
  }

  /**
   * getSubDocumentosEtiqueta
   */    
  static public List getSubDocumentosEtiqueta(Document documento, 
                                              String etiqueta)  { 
    List<Document> subDocumentos = new ArrayList<>();
    try {
      NodeList nodos = documento.getElementsByTagName(etiqueta);  
      for (int i = 0; i < nodos.getLength(); i++) {
        Document subDocumento = subDocumento(nodos.item(i));
        subDocumentos.add(subDocumento);
      }
     } catch (Exception ex) {
       Logger.getLogger(LectorXML.class.getName()).log(Level.SEVERE, null, ex);
     }
     return subDocumentos;
   }

  /**
   * getSubDocumentosEtiqueta
   */      
  public List getSubDocumentosEtiqueta(String etiqueta)  { 
    List<Document> subDocumentos = new ArrayList<>();
    try {
      NodeList nodos = documento.getElementsByTagName(etiqueta);
      for (int i = 0; i < nodos.getLength(); i++) {
        Document subDocumento = subDocumento(nodos.item(i));
        subDocumentos.add(subDocumento);
      }
    } catch (Exception ex) {
     Logger.getLogger(LectorXML.class.getName()).log(Level.SEVERE, null, ex);
    }
    return subDocumentos;
  }
}  
