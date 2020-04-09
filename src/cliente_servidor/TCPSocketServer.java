package cliente_servidor;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author ivandro
 */
public class TCPSocketServer {
    
    public static void main(String[] args) {

       try {
 
	   //Crie uma instancia da class SocketServer, especializada para servidores (listen embutido)
          // ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]));
           ServerSocket socket = new ServerSocket(12345);
   
           while(true) {    
   		//Espera uma conexão
		Socket socketConectado = socket.accept();
    
               	BufferedReader doClienteBF = new BufferedReader(new InputStreamReader(socketConectado.getInputStream()));
               	DataOutputStream paraCliente = new DataOutputStream(socketConectado.getOutputStream());
    
                String lidoDoCliente = doClienteBF.readLine();
                System.out.println("Servidor@ Mensagem recebida: " + lidoDoCliente);

                paraCliente.writeBytes(lidoDoCliente = "Confirmacao de Recepcao(" + lidoDoCliente + ")\n");
           } 
      
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }  
   }    
}
