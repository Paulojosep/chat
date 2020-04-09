package cliente_servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        try {
            Socket cliente = new Socket("127.0.0.1", 12345);
            System.out.println(" Sou o cliente ");

            System.out.println("O cliente se conectou ao servidor!");

            try (Scanner teclado = new Scanner(System.in);
                 PrintStream saida = new PrintStream(cliente.getOutputStream())) {


                while (teclado.hasNextLine()) {
                    String line = teclado.nextLine();
                    if (line.equals("Exit")) {
                        cliente.close();
                        break;
                    }else {
                        saida.println(line);
                    }
                }
                // teclado.hasNextLine

            }

        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }
    }
}