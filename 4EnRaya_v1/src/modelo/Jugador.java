package modelo;

import java.io.Serializable;

/**
 *
 * @author Javi Navarro
 * @version 1.2
 */
public class Jugador implements Serializable{
    private String nombre;
    private String simbolo;
    private int ganadas;
    private boolean turno;
    
    public Jugador(String nombre, String simbolo, boolean turno) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.turno = turno;
    }
    
    public void victoria(){
        this.ganadas += 1; 
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getNombre() {
        return nombre;
    }
    
   public boolean miTurno(){
       return turno;
   }
   
   public void setTurno(boolean turno){
       this.turno = turno;
   }
    public int getGanadas() {
        return ganadas;
    }

    public String toString() {
        return "Jugador "+this.nombre;
    }

    public boolean equals(Object obj) {
        if(!super.equals(obj)) return false;
        if(!(obj instanceof Jugador)) return false;
        Jugador j = (Jugador)obj;
        String nLower = this.nombre.toLowerCase();
        if(nLower.equals(j.getNombre().toLowerCase())) return true;
        return false;
    }

    public int hashCode() {
        int resultado = 17;
//Para que no tenga en cuenta mayusculas y minusculas al generar el codigo
        String nLower = this.nombre.toLowerCase();
        String nUpper = this.nombre.toUpperCase();
                
        resultado = 37 * resultado + nLower.hashCode();
        return 23 * resultado + nUpper.hashCode();
    }
}