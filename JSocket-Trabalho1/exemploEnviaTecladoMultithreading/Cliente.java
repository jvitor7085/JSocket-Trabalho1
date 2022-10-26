package exemploEnviaTecladoMultithreading;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

//Prefira implementar a interface Runnable do que extender a classe Thread, pois neste caso utilizaremos apena o método run.
public class Cliente implements Runnable {

	private Socket cliente;

	public Cliente(Socket cliente) {
		this.cliente = cliente;
	}

	public static void main(String args[]) throws UnknownHostException, IOException {

		// para se conectar ao servidor, cria-se objeto Socket.
		// O primeiro parâmetro é o IP ou endereço da máquina que
		// se quer conectar e o segundo é a porta da aplicação.
		// Neste caso, usa-se o IP da máquina local (127.0.0.1)
		// e a porta da aplicação ServidorDeEco (12345).
		Socket socket = new Socket("127.0.0.1", 12345);

		/*
		 * Cria um novo objeto Cliente com a conexão socket para que seja executado em
		 * um novo processo. Permitindo assim a conexão de vários clientes com o
		 * servidor.
		 */
		Cliente c = new Cliente(socket);
		Thread t = new Thread(c);
		t.start();
	}

	public void run() {
		try {
			// pega o arquivo da memória para enviar
			Path caminho = Paths.get(
					"C:/Users/Vitor/Documents/PC facul/Sistemas Distribuídos/exemploEnviaTecladoMultithreading/resposta.txt");

			// transformo em um array de bytes para ficar mais fácil de transformar em
			// string
			byte[] gabarito = Files.readAllBytes(caminho);
			String conteudo = new String(gabarito);

			// removo as quebras de linha para enviar sem um for
			String semQuebra = conteudo.replaceAll("\\n", "");

			// cria o atributo saida para enviar os arquivos necessários
			PrintStream saida;
			System.out.println("O cliente conectou ao servidor");
			saida = new PrintStream(this.cliente.getOutputStream());

			// envio o resultado do cliente
			saida.println(semQuebra);

			// fecha todas as instancias abertas;
			saida.close();
			this.cliente.close();
			System.out.println("Fim do cliente!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}