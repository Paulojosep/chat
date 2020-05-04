package server;

import java.io.*;
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
    private static final Map<String, Server> clientes = new HashMap<String, Server>(); // variavel que guarda toods os clintes logados no servidor

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
            System.out.println("/////Aguardando clientes...////////");

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
        try {
            //Scanner entrada = new Scanner(cliente.getInputStream());
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            escritor = new PrintWriter(cliente.getOutputStream(), true);
            escritor.println("Digite seu nome");
            String msg = leitor.readLine();
            efeturaLogin(msg);

            while (true) {
                msg = leitor.readLine();
                if (msg.equalsIgnoreCase("Exit")) { // Sair
                    this.cliente.close();
                } else if (msg.toLowerCase().startsWith("::msg")) { // Enviar menssagem para um cliente
                    sendMessage(msg);
                } else if (msg.equalsIgnoreCase("::listar")) { // Listar os nomes
                    listaDeUsuarios();
                }else if (msg.equalsIgnoreCase("::new")){ // Alterar Nome
                    alterarNome();
                } else {
                    for (String c : clientes.keySet()) {
                        Server destinatario = clientes.get(c);
                        destinatario.getEscritor().println(this.nickName + " disse " + msg);

                    }
                    System.out.println(this.nickName + " enviou pra todos");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println(this.nickName + " desconectou"); // cliente se desconectou
            clientes.remove(this.nickName); // remove o clinte que se desconectou do servidor
        }
    }

    // Lista de usuarios
    private void listaDeUsuarios() {
        StringBuffer str = new StringBuffer();
        for (String c : clientes.keySet()) {
            str.append(c);
            str.append(",");
        }
        if (str.length() > 0)
            str.delete(str.length() - 1, str.length());
        escritor.println("usuarios: " + str.toString());
        System.out.println(this.nickName + " >> usuarios: " + str.toString());
    }

    // metodo de fazer login
    private void efeturaLogin(String msg) throws IOException {
        while (true) {
            this.nickName = msg.toLowerCase().replaceAll(",", "");
            if (this.nickName.isEmpty()) {//verifica se o nome nao e vazio
                escritor.println("Nome Fazio digite novamente!");
            } else if (clientes.containsKey(this.nickName)) {// verifica se o nome não são iguais aos outros clientes
                System.out.println("Nome ja existe"); //eviar esta mennsagem pro servidor
                escritor.println("Servidor disse: error nome ja existe! digite novamente"); //envia esta mennsagem pro cliente
                msg = leitor.readLine();
            } else {
                escritor.println("Servidor disse: ola " + this.nickName);
                clientes.put(this.nickName, this);
                System.out.println(nickName + " entrou no chat");
                break;
            }
        }
    }

    // metodo de alterar o nick do cliente
    private void alterarNome() throws IOException {
        escritor.println("Digite o seu novo nome");
        String newName = leitor.readLine();
            if (clientes.containsKey(newName)) { //verifica se o nome do nick for iguais ao dos outros, se for..
                escritor.println("nick já atribuído a outro cliente");//envia esta menssagem pro cliente
            } else {
                clientes.remove(this.nickName);
                //newName = leitor.readLine();
                setNickName(newName);
                escritor.println("Novo nome e: " + getNickName());
                clientes.put(newName, this);
                listaDeUsuarios();
            }
    }

    // Metodo enviar menssagem para 1 cliente (cliente -> cliente)
    private void sendMessage(String msg) throws IOException {
        String nomeDestinatario = msg.substring(5, msg.length());
        Server destinatario = clientes.get(nomeDestinatario);
        if (destinatario == null) { // Verifica se o usuario digitou for vazio
            escritor.println("Cliente nao existe");
        } else {
            escritor.println("digite uma mensagem para " + destinatario.getNickName());
            destinatario.getEscritor().println(this.nickName + " disse: " + leitor.readLine());
            System.out.println(this.nickName + " enviou menssagem para " + destinatario.getNickName());
        }
    }
}