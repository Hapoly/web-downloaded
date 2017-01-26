import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Timer;

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
			e.printStackTrace();
			System.exit(0);
		}
	}

	void insertUser(String username, String password) throws SQLException {
		String sql = "INSERT INTO users (username,password) VALUES('" + username + "','" + password + "')";
		stmt.executeUpdate(sql);
		stmt.close();
	}

	void insertNumber(String username, String number) throws SQLException {
		String sql = "INSERT INTO numbers (number,username) VALUES('" + number + "','" + username + "')";
		stmt.executeUpdate(sql);
		stmt.close();
	}

	void sendRequest(String username1, String username2) throws SQLException {
		String sql = "INSERT INTO requests (sender_username,reciever_username) VALUES('" + username1 + "','" + username2
				+ "')";
		stmt.executeUpdate(sql);
		stmt.close();
	}

	void acceptRequest(String username1, String username2) throws SQLException {
		String sql1 = "DELETE FROM requests where( sender_username='" + username1 + "' and reciever_username='"
				+ username2 + "')";
		String sql2 = "INSERT INTO friends (username_1,username_2) VALUES('" + username1 + "','" + username2 + "')";
		stmt.executeUpdate(sql1);
		stmt.executeUpdate(sql2);
		stmt.close();
	}

	void initChat(String title) throws SQLException {
		String sql = "INSERT INTO chats(chat_title)VALUES('" + title + "')";
		stmt.executeUpdate(sql);
		stmt.close();

	}

	void addMemberToChat(String chatTitle, String number) throws SQLException {
		String sql = "INSERT INTO members(number,chat_title) VALUES('" + number + "','" + chatTitle + "')";
		stmt.executeUpdate(sql);
		stmt.close();

	}

	// changed
	void sendMessage(String chatTitle, String message, String number) throws SQLException {
		String sql = "INSERT INTO messages (chat_title,message,number) VALUES ('" + chatTitle + "','" + message + "','"
				+ number + "')";
		stmt.executeUpdate(sql);
		stmt.close();
	}

	boolean checkUser(String username, String password) throws SQLException {
		ResultSet rs = stmt.executeQuery(
				"SELECT * FROM users WHERE username = '" + username + "' and password = '" + password + "'");
		boolean result = rs.next();
		stmt.close();
		return result;
	}

	//
	SadafArray<String> getUserInDept(String username, int dpt) throws SQLException {
		SadafArray<String> userList = new SadafArray<>();
		if (dpt < 2) {
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM friends WHERE username_1='" + username + "' or username_2='" + username + "'");
			while (rs.next()) {//
				if (username.equals(rs.getString("username_1"))) {
					userList.add(rs.getString("username_2"));
				} else if (username.equals(rs.getString("username_2"))) {
					userList.add(rs.getString("username_1"));
				}
			}
		} else {
			SadafArray<String> friendList = getUserInDept(username, 1);

			for (int i = 0; i < friendList.size; i++) {
				SadafArray<String> tmpList2 = getUserInDept(friendList.get(i), dpt - 1);
				for (int j = 0; j < tmpList2.size; j++)
					if (!friendList.contains(tmpList2.get(j)) && !tmpList2.get(j).equals(username))
						userList.add(tmpList2.get(j));
			}

		}
		stmt.close();
		return userList;
	}

	SadafArray<String> getNotFriends(String username) throws SQLException {
		SadafArray<String> friendList = new SadafArray<>();
		ResultSet rs = stmt.executeQuery(
				"SELECT * FROM friends WHERE username_1='" + username + "' or username_2='" + username + "'");
		while (rs.next()) {//
			if (username.equals(rs.getString("username_1"))) {
				friendList.add(rs.getString("username_2"));
			} else if (username.equals(rs.getString("username_2"))) {
				friendList.add(rs.getString("username_1"));
			}
		}

		SadafArray<String> result = new SadafArray<>();
		rs = stmt.executeQuery("SELECT * FROM users");
		while (rs.next()) {//
			if (!username.equals(rs.getString("username")) && !friendList.contains(rs.getString("username"))) {
				result.add(rs.getString("username"));
			}
		}
		return result;
	}

	// return reciever_names
	SadafArray<String> getSentRequests(String username) throws SQLException {
		SadafArray<String> send = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * FROM requests WHERE sender_username='" + username + "'");
		while (rs.next()) {
			String str = rs.getString("reciever_username");
			send.add(str);
		}
		stmt.close();
		return send;
	}

	// return sender_usernames
	SadafArray<String> getRecievedRequests(String username) throws SQLException {
		SadafArray<String> recieve = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * FROM requests WHERE reciever_username='" + username + "'");
		while (rs.next()) {
			String str = rs.getString("sender_username");
			recieve.add(str);
		}
		stmt.close();
		return recieve;
	}

	SadafArray<String> getChats(String number) throws SQLException {
		SadafArray<String> chat_name = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * FROM members WHERE number='" + number + "'");
		while (rs.next()) {
			String str = rs.getString("chat_title");
			chat_name.add(str);
		}
		stmt.close();
		return chat_name;
	}

	SadafArray<String> getMessages(String chatTitle) throws SQLException {//
		SadafArray<String> messageList = new SadafArray<>();
		SadafArray<String> numberList = new SadafArray<>();
		SadafArray<String> result = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * FROM messages WHERE chat_title='" + chatTitle + "'");
		while (rs.next()) {
			messageList.add(rs.getString("message"));
			numberList.add(rs.getString("number"));
		}
		for (int i = 0; i < messageList.size; i++) {
			result.add(this.getUsernameFROMNumber(numberList.get(i)) + "(" + numberList.get(i) + "):"
					+ messageList.get(i));
		}
		stmt.close();
		return result;
	}

	SadafArray<String> getNumbers(String username) throws SQLException {
		SadafArray<String> numberList = new SadafArray<>();
		ResultSet rs = stmt.executeQuery("SELECT * FROM numbers WHERE username='" + username + "'");
		while (rs.next()) {
			String str = rs.getString("number");
			numberList.add(str);
		}
		stmt.close();
		return numberList;
	}

	String getUsernameFROMNumber(String number) throws SQLException {
		ResultSet rs = stmt.executeQuery("SELECT * FROM numbers WHERE number='" + number + "'");
		while (rs.next()) {
			return rs.getString("username");
		}
		stmt.close();
		return "NF";
	}

	void close() throws SQLException {
		stmt.close();
		co.close();

	}

}
