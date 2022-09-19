/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package redes2.practica1;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mauri
 */
public class Practica1 {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner (System.in);
        File ff= new File("");
        String path = ff.getAbsolutePath()+"\\arcCliente";
        File ff2= new File(path);
        System.out.println(Arrays.toString(ff2.list()));
        //mostrarCarpetas( path, 0);
        /*File delPath = new File(path+"\\alv");
        FileUtils.deleteDirectory(delPath);
        //delPath.delete();
        mostrarCarpetas( path, 0);*/
        //System.out.println("Ruta:"+z);
        
        /*GestorArchivos ga = new GestorArchivos(z);
        ga.root.mostrarCarpeta();
        String filePath = sc.nextLine();
        ga.borrarArchivoLocal(filePath);
        ga.root.mostrarCarpeta();*/
        /*File ff2 = new File(z);
        File[] archivos = ff2.listFiles();
        for (File archivo : archivos) {
            String xx = archivo.getAbsolutePath();
            xx = (archivo.isDirectory()) ? xx+ "/" : xx;
            System.out.println(xx);
        } //*/
    }
    public static void mostrarCarpetas(String path, int nivel){
        File dirPath = new File(path);
        //System.out.println("path: "+path+"\npathFile: "+ dirPath.getAbsolutePath());
        File[] archivos = dirPath.listFiles();
        for (File archivo : archivos) {
            if(archivo.isDirectory()){
                System.out.println(("   ").repeat(nivel)+archivo.getName());
                mostrarCarpetas(archivo.getAbsolutePath(), nivel+1);
            }else{
                System.out.println(("   ").repeat(nivel)+"-"+archivo.getName());
            }
            
        } 
    }
    
    /*public static Carpeta mapear(String path, String realtivePath, int  nivel){
        
        File dirPath = new File(path);
        Carpeta carpeta = new Carpeta(dirPath.getName(),dirPath.getAbsolutePath(), realtivePath, nivel);
        File[] archivos = dirPath.listFiles();
        for (File archivo : archivos) {
            realtivePath+="\\"+archivo.getName();
            if(archivo.isDirectory()){
                carpeta.agregarCarpeta(mapear(archivo.getAbsolutePath(), realtivePath, nivel+1));
            }else{
                carpeta.agregarArchivo(new Archivo(archivo.getName(),archivo.getAbsolutePath(),realtivePath,archivo.length()));
            }
            
        } 
        return carpeta;
    }*/
}
