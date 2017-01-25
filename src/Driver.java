import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Driver {
	Connection co = null;
	Statement stmt = null;// for query

	public Driver() {
		connect();
	}

	void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			co = DriverManager.getConnection("jdbc:sqlite:data.db");
			co.setAutoCommit(true);
			stmt = co.createStatement();
			System.out.println("Opened database successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	

	void insertUser(String username, String password) throws SQLException {
		String sql = "INSERT INTO users (username,password) VALUES('" + username + "','" + password + "')";
		stmt.executeUpdate(sql);
	}

	void insertNumber(String username, String number) throws SQLException {
		String sql = "INSERT INTO numbers (number,username) VALUES('" + number + "','" + username + "')";
		stmt.executeUpdate(sql);

	}

	void sendRequest(String username1, String username2) throws SQLException {
		String sql = "INSERT INTO requests (sender_username,reciever_username) VALUES('" + username1 + "','" + username2
				+ "')";
		stmt.executeUpdate(sql);

	}

	void acceptRequest(String username1, String username2) throws SQLException {
		String sql1 = "DELETE FROM requests where( sender_username='" + username1 + "' and receiver_username='"
				+ username2 + "')";
		String sql2 = "INSERT INTO friends (username_1,username_2) VALUES('" + username1 + "','" + username2 + "')";
		stmt.executeUpdate(sql1);
		stmt.executeUpdate(sql2);

	}

	void initChat(String title) throws SQLException {
		String sql = "INSERT INTO chats(chat_title)VALUES('" + title + "')";
		stmt.executeUpdate(sql);

	}

	void addMemberToChat(String chatTitle, String username) throws SQLException {
		String sql = "INSERT INTO members(username,chat_title) VALUES('" + username + "','" + chatTitle + "')";
		stmt.executeUpdate(sql);

	}
	//changed
	void sendMessage(String chatTitle, String message, String number) throws SQLException {
		String sql = "INSERT INTO messages (chat_title,message,number) VALUES ('" + chatTitle + "','" + message
				+ "','" + number + "')";
		stmt.executeUpdate(sql);

	}

	boolean checkUser(String username, String password) throws SQLException {
		ResultSet rs = stmt.executeQuery(
				"SELECT * FROM users WHERE username = '" + username + "' and password = '" + password + "'");
		return rs.next();
	}
//
	SadafArray<String> getFriends(String username, int dpt) throws SQLException {
		SadafArray<String> friendList = new SadafArray<>();
		if (dpt == 1) {
			ResultSet rs = stmt.executeQuery(
					"SELECT * fROM friends WHERE username_1='" + username + "' or username_2='" + username + "'");
			System.out.println(
					"SELECT * fROM friends WHERE username_1='" + username + "' or username_2='" + username + "'");
			while (rs.next()) {//
				if (username.equals(rs.getString("username_1"))) {
					friendList.add(rs.getString("username_2"));
				} else if (username.equals(rs.getString("username_2"))) {
					friendList.add(rs.getString("username_1"));
				}
			}
		} else {
			ResultSet rs = stmt.executeQuery(
					"SELECT * fROM friends WHERE username_1='" + username + "' or username_2='" + username + "'");
			System.out.println(
					"SELECT * fROM friends WHERE username_1='" + username + "' or username_2='" + username + "'");
			while (rs.next()) {
				if (username.equals(rs.getString("username_1"))) {
					friendList.merg(getFriends(rs.getString("username_2"), dpt - 1));
				} else if (username.equals(rs.getString("username_2"))) {
					friendList.merg(getFriends(rs.getString("username_1"), dpt - 1));
				}
			}
		}
		return friendList;

	}
	
	
	//return reciever_names
	SadafArray<String> getSentRequests(String username) throws SQLException {
		SadafArray<String> send = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * fROM requests WHERE sender_username='" + username + "'");
		while (rs.next()) {
			String str = rs.getString("reciever_username");
			send.add(str);
		}
		return send;
	}
	
	
	//return sender_usernames
	SadafArray<String> getRecievedRequests(String username) throws SQLException {

		SadafArray<String> recieve = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * fROM requests WHERE reciever_username='" + username + "'");
		while (rs.next()) {
			String str = rs.getString("sender_username");
			recieve.add(str);
		}
		return recieve;
	}

	SadafArray<String> getChats(String number) throws SQLException {
		SadafArray<String> chat_name = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * fROM members WHERE number='" + number + "'");
		while (rs.next()) {
			String str = rs.getString("chat_title");
			chat_name.add(str);
		}
		return chat_name;
	}

	SadafArray<String> getMessages(String chatTitle) throws SQLException {//
		SadafArray<String> chat_name = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * fROM chats WHERE chat_title='" + chatTitle + "'");
		while (rs.next()) {
			String str = rs.getString("message") + ":" + rs.getString("number");
			chat_name.add(str);
		}
		return chat_name;
	}

	SadafArray<String> getNumbers(String username) {
		return null;
	}

	SadafArray<String> getChatListByUsername(String username) {
		return null;
	}

	void close() throws SQLException {

		stmt.close();
		co.close();

	}

	void refresh() throws SQLException {
		close();
		connect();
	}

}
