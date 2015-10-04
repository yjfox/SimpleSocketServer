package simplesocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	private final int PORT;
	private final BlockingQueue<Socket> blkque;
	private ServerSocket serverSocket;
	private boolean running;


	SocketServer(BlockingQueue<Socket> blkque, int port) {
		this.PORT = port;
		this.running = false;
		this.blkque = blkque;
	}
	
	public void setRunning(boolean val) {
		this.running = val;
	}

	public void startServer() {
		try {
			serverSocket = new ServerSocket(PORT);
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen() {
		running = true;
		while (running) {
			try {
				//blocking method, will block queue if it is full
				blkque.add(serverSocket.accept());
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
		final int BLOCK_QUEUE_SIZE = 3;
		final BlockingQueue<Socket> blkque = new BlockingQueue<Socket>(BLOCK_QUEUE_SIZE);
		SocketServer server = new SocketServer(blkque, PORT);
		server.startServer();
		SocketConsumer skConsumer = new SocketConsumer(blkque);
		skConsumer.startConsumer();
	}
}
