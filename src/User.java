
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class User extends Thread {
	private Socket clientSocket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;

	User(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		this.dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
		this.dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());

		String userName = this.dataInputStream.readUTF();
		Server.addUser(userName);
		Server.addClientSocket(this.clientSocket);

		start();
	}

	@Override
	public void run() {
		super.run();
	}
}
