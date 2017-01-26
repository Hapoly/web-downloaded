import java.awt.HeadlessException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Main {
	private static Main programm = new Main();//
	private static LoginPage loginPage;
	private static MainPage mainPage;

	Driver driver = new Driver();

	public static void main(String[] args) throws SQLException {
		loginPage = new LoginPage(programm);
		loginPage.show();
	}

	public void loginEvent(String username, String password) {
		System.out.println("wanna login by " + username + " , " + password);
		try {
			if (driver.checkUser(username, password)) {
				mainPage = new MainPage(programm, username);
				loginPage.dispose();
				mainPage.show();
			} else {
				JOptionPane.showMessageDialog(null, "login failed");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void registerEvent(String username, String password, String[] numbers) throws SQLException {
		System.out.println("wanna register by " + username + " , " + password);
		driver.insertUser(username, password);
		for (String number : numbers) {
			System.out.println(number);
			driver.insertNumber(username, number);
		}
		mainPage = new MainPage(programm, username);
		loginPage.dispose();
		mainPage.show();
	}

	public void requestSendEvent(String username) {
		SendRequestPanel sendRequestPanel = new SendRequestPanel(username, this);
		sendRequestPanel.show();
	}

	public void gotRequestsEvent(String username) {
		GotRequestPanel gotRequestPanel = new GotRequestPanel(username, this);
		gotRequestPanel.show();
	}

	public void newChatEvent(String username) {
		NewChat newChat = new NewChat(username, this);
		newChat.show();
	}
}
