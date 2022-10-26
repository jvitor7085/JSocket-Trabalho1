package exemploEnviaTecladoMultithreading;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Servidor implements Runnable {
	public Socket cliente;
	// public Map<String, ObjectOutputStream> streammap = new HashMap<String,
	// ObjectOutputStream>();

	public Servidor(Socket cliente) {
		this.cliente = cliente;
	}

	public static void main(String[] args) throws IOException {

		try (// Cria um socket na porta 12345
				ServerSocket servidor = new ServerSocket(12345)) {
			System.out.println("Porta 12345 aberta!");

			// Aguarda alguém se conectar. A execução do servidor
			// fica bloqueada na chamada do método accept da classe
			// ServerSocket. Quando alguém se conectar ao servidor, o
			// método desbloqueia e retorna com um objeto da classe
			// Socket, que é uma porta da comunicação.
			System.out.println("Aguardando conexão do cliente...");

			while (true) {

				Socket cliente = servidor.accept();

				// Cria uma thread do servidor para tratar a conexão
				Servidor tratamento = new Servidor(cliente);

				Thread t = new Thread(tratamento);

				// Inicia a thread para o cliente conectado
				t.start();
			}
		}
	}

	/*
	 * A classe Thread, que foi instancia no servidor, implementa Runnable. Então
	 * você terá que implementar sua lógica de troca de mensagens dentro deste
	 * método 'run'.
	 */
	public void run() {
		System.out.println("Nova conexao com o cliente " + this.cliente.getInetAddress().getHostAddress());
		try {
			// busco o arquivo na memória interna
			Path caminho = Paths.get(
					"C:/Users/Vitor/Documents/PC facul/Sistemas Distribuídos/exemploEnviaTecladoMultithreading/gabarito.txt");

			// transformo em um array de bytes para ficar mais fácil de transformar em
			// string
			byte[] gabarito = Files.readAllBytes(caminho);

			// coloco em uma string
			String conteudo = new String(gabarito);

			// removo as quebras de linha para ficar igual ao arquivo cliente
			String semQuebra = conteudo.replaceAll("\\n", "");

			// recebo o resultado do cliente e coloco na String respostas
			Scanner s = null;
			s = new Scanner(this.cliente.getInputStream());
			String respostas = s.nextLine();

			// comparação para verificar as respostas
			/*
			 * System.out.println("Arquivo recebido contem"); System.out.println(respostas);
			 * System.out.println("Arquivo original contem"); System.out.println(semQuebra);
			 */
			
			// variaveis que contabilizam erros e acertos
			// A variavel acertos começa em -141 pois também seria considerado um acerto o
			// numero da questão e o simbolo "-"(Ex:7-)
			// por isso -141 são 2 itens por questão até 9(ex:7-) e depois 3 itens por
			// questão até a 50(ex:40-)
			int acertos = -141;
			int erros = 0;
			double notaFinal = 0.0;

			// comparação entre o gabarito e as respostas do usuário, caracter por caracter
			for (int i = 0; i < respostas.length(); i++) {
				char r = respostas.charAt(i);
				char c = semQuebra.charAt(i);

				// verifica se é igual, se for igual contabiliza um acerto, se for diferente
				// contabiliza um erro.
				if (r == c)
					acertos++;
				else
					erros++;

			}

			// calculo da nota final, cada acerto equivale a 0,04 pontos(10/250=0,04), então
			// foi calculado a nota final com base nisso.
			notaFinal = acertos * 0.04;
			System.out.println(" acertos=" + acertos + " erros=" + erros + " Nota final:" + notaFinal);
			// Finaliza objetos
			s.close();
			this.cliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}