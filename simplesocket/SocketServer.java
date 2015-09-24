package simplesocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	private final int PORT;
	private boolean running;

	private ServerSocket serverSocket;

	SocketServer(int port) {
		this.PORT = port;
		this.running = false;
	}

	public void startServer() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		listen();
	}

	private void listen() {
		running = true;
		SocketConsumer skConsumer = new SocketConsumer();
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				skConsumer.add(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!running) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				serverSocket = null;
			}
		}
	}

	public static void main(String[] args) {
		final int PORT = 9988;
		SocketServer server = new SocketServer(PORT);
		server.startServer();
	}
}
