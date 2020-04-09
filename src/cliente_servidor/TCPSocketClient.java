/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente_servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author ivandro
 */
public class TCPSocketClient {

 public static void main(String[] args) {

       try {
	    /*                      Experimento 1                             */
    
	    //Define variavel para armazenar string, e le string do teclado.
            BufferedReader tecladoBR = new BufferedReader(new InputStreamReader(System.in));
            String tecladoStr= tecladoBR.readLine();

            // Cria um socket para conectar ao ip/porta do servidor   
            //Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            Socket socket = new Socket("127.0.0.1", 12345);
    
            // Cria um stream de saída 
            DataOutputStream paraServidor = new DataOutputStream(socket.getOutputStream());
	    // e um de entrada, do socket
            BufferedReader doServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
            // Envia mensagem digitada para o servidor.
            paraServidor.writeBytes(tecladoStr+ "\n");
    
            
            // Recebe resposta do servidor
            String socketStr = doServidor.readLine();
            System.out.println("Cliente@ Resposta: " + socketStr);

            // Fecha o Socket
            socket.close();   

       } catch (UnknownHostException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
   } 
    
}
