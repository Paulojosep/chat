package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

// Utilização do Thread
public class Server extends Thread {
    private Socket cliente;
    private String nickName;
    private BufferedReader leitor;
    private PrintWriter escritor;
    private static final Map<String, Server> clientes = new HashMap<String, Server>();

    public Server(Socket cliente) {
        this.cliente = cliente;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BufferedReader getLeitor() {
        return leitor;
    }

    public PrintWriter getEscritor() {
        return escritor;
    }

    public static void main(String args[]) {
        try {
            ServerSocket server = new ServerSocket(12345);
            System.out.println("Servidor iniciado na porta 12345");

            while (true) {
                Socket cliente = server.accept();
                Server newCliente = new Server(cliente);
                Thread thread = new Thread(newCliente);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Cliente conectado do IP " + cliente.getInetAddress().getHostAddress());

        try {
            //Scanner entrada = new Scanner(cliente.getInputStream());
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            escritor = new PrintWriter(cliente.getOutputStream(), true);
            escritor.println("Digite seu nome");
            String msg = leitor.readLine();
            efeturaLogin(msg);

            while (true) {
                msg = leitor.readLine();
                if (msg.equalsIgnoreCase("Exit")) {
                    this.cliente.close();
                } else if (msg.toLowerCase().startsWith("::msg")) {
                    String nomeDestinatario = msg.substring(5, msg.length());
                    System.out.println("Enviado para " + nomeDestinatario);
                    Server destinatario = clientes.get(nomeDestinatario);
                    if (destinatario == null) {
                        escritor.println("Cliente nao existe");
                    } else {
                        escritor.println("digite uma mensagem para " + destinatario.getNickName());
                        destinatario.getEscritor().println(this.nickName + " disse " + leitor.readLine());
                        System.out.println(this.nickName + " enviou para " + destinatario.getNickName());
                    }
                    // Listar os nomes
                } else if (msg.equalsIgnoreCase("::listar")) {
                    listaDeUsuarios();
                } else {
                    for (String c : clientes.keySet()) {
                        Server destinatario = clientes.get(c);
                        destinatario.getEscritor().println(this.nickName + " disse " + msg);
                        System.out.println(this.nickName + " enviou pra todos");
                        //escritor.println(c + " Voce disse: " + msg);
                    }
                }
            }

            //System.out.println("Cliente conectado do IP " + cliente.getInetAddress().getHostAddress() + " finalizou conexão");
            //leitor.close();
            //this.cliente.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println(this.nickName + " desconectou");
        }
    }

    private void listaDeUsuarios() {
        StringBuffer str = new StringBuffer();
        for (String c : clientes.keySet()) {
            str.append(c);
            str.append(",");
        }
        if (str.length() > 0)
            str.delete(str.length() - 1, str.length());
        escritor.println(" usuarios: " + str.toString());
        System.out.println(" usuarios: " + str.toString());
    }

    private void efeturaLogin(String msg) throws IOException {
        while (true) {
            this.nickName = msg.toLowerCase().replaceAll(",", "");
            if (this.nickName.isEmpty()) {
                escritor.println("Nome Fazio digite novamente!");
            } else if (clientes.containsKey(this.nickName)) {
                escritor.println("Nome repetido digite novamente!");
                System.exit(1);
            } else {
                escritor.println("ola " + this.nickName);
                clientes.put(this.nickName, this);
                System.out.println(nickName + " entrou no chat");
                break;
            }
        }
    }

}