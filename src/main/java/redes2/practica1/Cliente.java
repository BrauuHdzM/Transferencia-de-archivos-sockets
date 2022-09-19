/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package redes2.practica1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author mauri
 */
public class Cliente {
    static GestorArchivos gaCliente;
    static GestorArchivos gaServer;
    static String host = "localhost";
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        int opcion;
        boolean salir = false;
        Scanner sc = new Scanner (System.in);
        
        Socket socketCliente = new Socket(host, 3000);
        System.out.println("Conexion con servidor exitosa.. ");
        
        String path = new File("").getAbsolutePath()+"\\arcCliente";
        gaCliente = new GestorArchivos(path);
        
       
        updateGestor();
        
        
        
        try (DataOutputStream dataOutput = new DataOutputStream(socketCliente.getOutputStream())) {
            while(!salir){
                
                System.out.println("""
                                               -----------Remoto-----------------
                                               1.- Mostrar archivos remotos
                                               2.- Subir archivo/carpeta
                                               3.- Descargar archivo/carpeta
                                               4.- Borrar archivo/carpeta remota
                                               ------------Local-----------------
                                               5.- Mostrar archivos locales
                                               6.- Borrar archivo/carpeta local
                                               ----------------------------------
                                               7.-Salir
                                               """);
                System.out.println("Ingrese opcion:");
                opcion = sc.nextInt();
                if(opcion!=2){
                    dataOutput.writeUTF(Integer.toString(opcion));
                    dataOutput.flush();
                }
                
                
                switch(opcion){
                    case 1://Mostrar remoto
                        updateGestor();
                        gaServer.mostrarRoot();
                    break;
                    case 2://Subir archivo/carpeta
                        gaCliente.mostrarRoot();
                        
                        System.out.println("Ingresa ruta del archivo/carpeta a subir");
                        gaCliente.subirCliente(dataOutput, (new Scanner(System.in)).nextLine());
                        
                    break;
                    case 3://Descargar archivo/carpeta
                        gaServer.mostrarRoot();
                        
                        System.out.println("Ingresa ruta del archivo/carpeta a descargar");
                        gaCliente.descargarCliente((new Scanner(System.in)).nextLine());
                    break;
                    case 4://Borrar remoto
                        
                        gaServer.mostrarRoot();
                        
                        System.out.println("Ingresa ruta del archivo/carpeta a borrar");
                        dataOutput.writeUTF((new Scanner(System.in)).nextLine());
                        dataOutput.flush();
                        updateGestor();
                    break;
                    
                    case 5://Mostrar local
                        gaCliente.updateRoot();
                        gaCliente.mostrarRoot();
                    break;
                    case 6://Borrar local
                        gaCliente.mostrarRoot();
                        System.out.println("Ingresa ruta del archivo/carpeta a borrar");
                        gaCliente.borrarArchivo((new Scanner(System.in)).nextLine());
                    break;
                    case 7:
                        salir = true;
                    break;
                }
                
            }
        }
            
    }
    
    
    
    public static void updateGestor() throws IOException, ClassNotFoundException{
        Socket socketCliente = new Socket(host, 3001);
        try (ObjectInputStream objectInput = new ObjectInputStream(socketCliente.getInputStream())) {
            gaServer= (GestorArchivos)objectInput.readObject();
        }
    }
    
}
