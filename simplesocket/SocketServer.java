package simplesocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	private ServerSocket serverSocket;
	private int port;
	private boolean running;

	SocketServer(int port) {
		this.port = port;
		this.running = false;
	}

	public void startServer() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		listen();
	}

	private void listen() {
		running = true;
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				PrintWriter out = new PrintWriter( socket.getOutputStream() );
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (true) {
					String readLine = in.readLine();
					System.out.println(readLine);
					if (readLine.equals("end")) {
						break;
					}
				}
				System.out.print(out.toString());
				out.print("echo hello world");
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		SocketServer server = new SocketServer(9988);
		server.startServer();
	}
}
