/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package redes2.practica1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mauri
 */
public final class GestorArchivos implements Serializable{
    public Carpeta root;
    String path;

    public GestorArchivos(String path) {
        this.path=path;
        updateRoot();
    }
    
    /*public void updateRoot(String path){
        this.root = mapear(path, ".", 0);
    }*/
    public void updateRoot(){
        this.root = mapear(this.path, ".", 0);
    }
    
    public Carpeta mapear(String path, String realtivePath, int  nivel){
        String relativePathAux;
        File dirPath = new File(path);
        Carpeta carpeta = new Carpeta(dirPath.getName(),dirPath.getAbsolutePath(), realtivePath, nivel);
        File[] archivos = dirPath.listFiles();
        for (File archivo : archivos) {
            relativePathAux = realtivePath+"\\"+archivo.getName();
            if(archivo.isDirectory()){
                carpeta.agregarCarpeta(mapear(archivo.getAbsolutePath(), relativePathAux, nivel+1));
            }else{
                carpeta.agregarArchivo(new Archivo(archivo.getName(),archivo.getAbsolutePath(),relativePathAux,archivo.length()));
            }
            
        } 
        return carpeta;
    }
    
    public void borrarArchivo(String relativePath) throws IOException{
        
        File archivo = new File(path+relativePath);
        System.out.println("borrar archivo: "+ path+relativePath);
        if(archivo.isDirectory()){
            FileUtils.deleteDirectory(archivo);
        }else{
            System.out.println("pruebita borrar: "+ FileUtils.delete(archivo));
            //archivo.delete();
            //FileUtils.delete(archivo);
        }
        updateRoot();
        //System.out.println(archivo.getAbsolutePath());
        
    }
    
    public void subirCliente(DataOutputStream dataOutput, String relativePath) throws IOException{
        File archivo = new File(path+relativePath);
        if(archivo.isDirectory()){
            File[] archivos = archivo.listFiles();
            
            for(File arc : archivos){
                System.out.println("relative: "+relativePath+arc.getName());
                subirCliente( dataOutput,  relativePath+"\\"+arc.getName());
            }
        }else{
            dataOutput.writeUTF("2");
            dataOutput.flush();
            try (Socket cl = new Socket("localhost",3002)) {
                String pathFile = archivo.getAbsolutePath();
                long tam = archivo.length();
                System.out.println("Preparandose pare enviar archivo "+pathFile+" de "+tam+" bytes\n\n");
                try (DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); DataInputStream dis = new DataInputStream(new FileInputStream(pathFile))) {
                    dos.writeUTF(relativePath);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();
                    long enviados = 0;
                    int l=0,porcentaje=0;
                    while(enviados<tam){
                        byte[] b = new byte[1500];
                        l=dis.read(b);
                        System.out.println("enviados: "+l);
                        dos.write(b,0,l);// dos.write(b);
                        dos.flush();
                        enviados = enviados + l;
                        porcentaje = (int)((enviados*100)/tam);
                        System.out.print("\rEnviado el "+porcentaje+" % del archivo");
                    }//while
                    System.out.println("\nArchivo enviado..");
                }
            }
            
        }
    }
    public void descargarServer(ServerSocket s) throws IOException{
        //File archivo = new File(path);
        try (Socket cl = s.accept()) {
            System.out.println("Cliente conectado desde "+cl.getInetAddress()+":"+cl.getPort());
            boolean bandera= false;
            try (DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); DataInputStream dis = new DataInputStream(cl.getInputStream())) {
                String relativePath = dis.readUTF();
                String absPath = path+relativePath.substring(1);
                File archivo = new File(absPath);
                if(archivo.isDirectory()){
                    bandera= true;
                    ZipFiles zipFiles = new ZipFiles();
                    zipFiles.zipDirectory(archivo, absPath+".zip");
                    archivo = new File(absPath+".zip");
                    dos.writeLong(1);
                } else{
                    dos.writeLong(0);
                }
                
                String pathFile = archivo.getAbsolutePath();
                long tam = archivo.length();
                System.out.println("Preparandose pare enviar archivo "+pathFile+" de "+tam+" bytes\n\n");
                
                dos.writeLong(tam);
                dos.flush();
                try (DataInputStream disF = new DataInputStream(new FileInputStream(pathFile))) {
                    long enviados = 0;
                    int l=0,porcentaje=0;
                    while(enviados<tam){
                        byte[] b = new byte[1500];
                        l=disF.read(b);
                        System.out.println("enviados: "+l);
                        dos.write(b,0,l);// dos.write(b);
                        dos.flush();
                        enviados = enviados + l;
                        porcentaje = (int)((enviados*100)/tam);
                        System.out.print("\rEnviado el "+porcentaje+" % del archivo");
                    }//while
                    
                    
                    System.out.println("\nArchivo enviado..");
                }
                if(bandera ){
                        borrarArchivo(relativePath.substring(1)+".zip");
                    }
            }
        }
        
    }
    
    public void descargarCliente(String relativePath) throws IOException{
        try (Socket cl = new Socket("localhost",3002)) {
            //System.out.println("Cliente conectado desde "+cl.getInetAddress()+":"+cl.getPort());
            try (DataInputStream dis = new DataInputStream(cl.getInputStream()); DataOutputStream dosS = new DataOutputStream(cl.getOutputStream())) {
                dosS.writeUTF(relativePath);
                dosS.flush();
                //String relativePath = dis.readUTF();
                String absPath = path+relativePath.substring(1);
                String absPathDest = absPath;
                System.out.println(absPath);
                File f = new File(absPath);
                f.getParentFile().mkdirs();
                f.setWritable(true);
                long esCarpeta = dis.readLong();
                if(esCarpeta == 1){
                    absPath+=".zip";
                }
                long tam = dis.readLong();
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(absPath))) {
                    long recibidos=0;
                    int l=0, porcentaje=0;
                    while(recibidos<tam){
                        byte[] b = new byte[1500];
                        l = dis.read(b);
                        System.out.println("leidos: "+l);
                        dos.write(b,0,l);
                        dos.flush();
                        recibidos = recibidos + l;
                        porcentaje = (int)((recibidos*100)/tam);
                        System.out.print("\rRecibido el "+ porcentaje +" % del archivo");
                    }//while
                    
                }
                if(esCarpeta == 1){
                    UnzipFiles unzipfile = new UnzipFiles();
                    unzipfile.unzip(absPath, absPathDest);
                    System.out.println("descarga cliente: " +absPath);
                    borrarArchivo(relativePath.substring(1)+".zip");
                    System.out.println("Archivo recibido..");
                    }
                /*if(dis.readUTF().equals("1")){
                    descargarCliente(relativePath);
                } */
            }
        }
    }
    public void subirServer(ServerSocket s) throws IOException{
        try (Socket cl = s.accept()) {
            System.out.println("Cliente conectado desde "+cl.getInetAddress()+":"+cl.getPort());
            try (DataInputStream dis = new DataInputStream(cl.getInputStream())) {
                String relativePath = dis.readUTF();
                String absPath = path+relativePath.substring(1);
                File f = new File(absPath);
                f.getParentFile().mkdirs();
                f.setWritable(true);
                long tam = dis.readLong();
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(absPath))) {
                    long recibidos=0;
                    int l=0, porcentaje=0;
                    while(recibidos<tam){
                        byte[] b = new byte[1500];
                        l = dis.read(b);
                        System.out.println("leidos: "+l);
                        dos.write(b,0,l);
                        dos.flush();
                        recibidos = recibidos + l;
                        porcentaje = (int)((recibidos*100)/tam);
                        System.out.print("\rRecibido el "+ porcentaje +" % del archivo");
                    }//while
                    System.out.println("Archivo recibido..");
                }
            }
        }
    }
    
    public void mostrarRoot(){
        System.out.println(("-").repeat(50));
        root.mostrarCarpeta();
        System.out.println(("-").repeat(50));
    }
    
    
    
}
