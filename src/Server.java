
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class Server {
	private static Vector clientSockets = new Vector();
	private static Vector users = new Vector();

	Server() throws IOException {
		ServerSocket serverSocket = new ServerSocket(3000);
		while (true) {
			Socket newUser = serverSocket.accept();
			User user = new User(newUser);

		}
	}

	public static void addClientSocket(Socket socket) {
		Server.clientSockets.add(socket);
	}

	public static void addUser(String user) {
		Server.users.add(user);
	}

}
