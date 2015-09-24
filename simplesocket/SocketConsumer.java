package simplesocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConsumer {
	static class SocketWorker implements Runnable {
		Socket socket;

		SocketWorker(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				while (true) {
					String readLine = in.readLine();
					System.out.println(readLine);
					out.print(readLine.toUpperCase());
					out.flush();
					if (readLine.equals("end")) {
						break;
					}
				}
				out.print("Bye");
				out.flush();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				socket = null;
			}
		}
	}

	public void add(Socket socket) {
		SocketWorker skWorker = new SocketWorker(socket);
		start(skWorker);
	}

	private void start(SocketWorker skWorker) {
		Thread skWorkerThread = new Thread(skWorker);
		skWorkerThread.start();
	}

}
