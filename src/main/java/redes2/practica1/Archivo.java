/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package redes2.practica1;

import java.io.Serializable;

/**
 *
 * @author mauri
 */
public class Archivo  implements Serializable{
    String nombre;
    String ruta;
    String rutaRelativa;
    long tamano;

    public Archivo(String nombre, String ruta, String rutaRelativa, long tamano) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.rutaRelativa = rutaRelativa;
        this.tamano = tamano;
    }

    public void mostrarArchivo(int nivel){
        System.out.println(("   ").repeat(nivel)+"-"+this.nombre+"  "+this.tamano+" bytes"+("   ").repeat(20-nivel)+this.rutaRelativa);
    }
    
    public void enviarArchivo(){
        
    }
    
    
}
