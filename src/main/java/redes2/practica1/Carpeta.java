/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package redes2.practica1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author mauri
 */
public class Carpeta implements Serializable{
    String nombre;
    String ruta;
    String rutaRelativa;
    int nivel;
    ArrayList<Carpeta> carpetas;
    ArrayList<Archivo> archivos;

    public Carpeta(String nombre, String ruta, String rutaRelativa, int nivel) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.rutaRelativa = rutaRelativa;
        this.nivel = nivel;
        this.carpetas = new ArrayList<Carpeta>();
        this.archivos =  new ArrayList<Archivo>();
    }

    
    
    public void agregarCarpeta(Carpeta subcarpeta){
        this.carpetas.add(subcarpeta);
    }
    public void agregarArchivo(Archivo archivo){
        this.archivos.add(archivo);
    }
    public void eliminarCarpeta(){
        
    }
    public void mostrarCarpeta(){
        System.out.println(("   ").repeat(this.nivel)+this.nombre+("   ").repeat(25-nivel)+this.rutaRelativa);
        for (Archivo archivo:archivos){
            archivo.mostrarArchivo(this.nivel);
        }
        for (Carpeta carpeta:carpetas){
            carpeta.mostrarCarpeta();
        }
        
    }
    public void enviarCarpeta(){
        
    }
    
    
}
