/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package redes2.practica1;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author mauri
 */
public class Server {
    static GestorArchivos gaSever;
    public static void main(String[] args) throws IOException {
        
        ServerSocket socketServer = new ServerSocket(3000);
        socketServer.setReuseAddress(true);
        
        ServerSocket socketGestor = new ServerSocket(3001);
        socketGestor.setReuseAddress(true);
        
        ServerSocket socketArchivos = new ServerSocket(3002);
        socketGestor.setReuseAddress(true);
        
	System.out.println("Servidor iniciado");
        String path = new File("").getAbsolutePath()+"\\arcServer";
        
        gaSever = new GestorArchivos(path);
        for(;;){
            try (Socket socketCliente = socketServer.accept(); DataInputStream dataInput = new DataInputStream(socketCliente.getInputStream())) {
                enviarGestor(socketGestor);
                boolean salir = false;
                while(!salir){
                    String opcion= dataInput.readUTF();
                    System.out.println(opcion);
                    switch(opcion){
                        case "1":
                                enviarGestor(socketGestor);
                            break;

                        case "2"://recibir archivo/carpeta
                            gaSever.subirServer(socketArchivos);
                        break;
                            
                        case "3"://enviar archivo/carpeta
                            gaSever.descargarServer(socketArchivos);
                        break;
                        case "4":
                            String pathBorrar = dataInput.readUTF();
                            System.out.println(pathBorrar);
                            gaSever.borrarArchivo(pathBorrar);
                            enviarGestor( socketGestor);
                        break;
                        case "7":
                            salir = true;
                        break;
                    }
                }
                
                
                
            }
        }
        
    }
    
    public static void enviarGestor(ServerSocket socketGestor) throws IOException{
        
        Socket socketCliente = socketGestor.accept();
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(socketCliente.getOutputStream())) {
            gaSever.updateRoot();
            objectOutput.writeObject(gaSever);
            objectOutput.flush();
        }
    }
}
