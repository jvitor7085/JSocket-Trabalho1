package exemploEnviaTecladoMultithreading;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
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
			PrintStream saida;
			System.out.println("O cliente conectou ao servidor");

			// Prepara para leitura do teclado
			Scanner teclado = new Scanner(System.in);

			// Cria objeto para enviar a mensagem ao servidor
			saida = new PrintStream(this.cliente.getOutputStream());

			// Envia mensagem ao servidor
			while (teclado.hasNextLine()) {
				saida.println(teclado.nextLine());
			}

			saida.close();
			teclado.close();
			this.cliente.close();
			System.out.println("Fim do cliente!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}