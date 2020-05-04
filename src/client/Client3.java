package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client3 {
    private static Socket client;
    public static void main(String[] args) {
        try {
            client = new Socket("127.0.0.1", 12345);

            // Lendo menssagem do servidor
            new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader leitor = new BufferedReader(new InputStreamReader(client.getInputStream())); // Receber Menssagem
                        while (true) {

                            String msg = leitor.readLine();
                            System.out.println(msg);
                        }

                    } catch (IOException e) {
                        System.err.println("Fechou a conexao");
                        System.exit(0);
                    }
                }
            }.start();

            // escrevendo para o servidor
            PrintWriter escritor = new PrintWriter(client.getOutputStream(), true); // Enviar menssagem
            BufferedReader leia = new BufferedReader(new InputStreamReader(System.in)); // Ira ler

            while (true) {
                String msg = leia.readLine();
                if (msg.equals("Exit")){
                    escritor.close();
                    leia.close();
                    client.close();
                    break;
                }else {
                    escritor.println(msg);
                }
            }


        } catch (UnknownHostException e) {
            System.err.println("O endereço invalido");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Servidor fora do ar");
            e.printStackTrace();
        }
    }
}