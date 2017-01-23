import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Driver {
	Connection co = null;
	Statement stmt = null;//for query

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
		String sql = "INSERT INTO numbers (number,username) VALUES('"+number+"','"+username+"')";
		stmt.executeUpdate(sql);

	}

	void sendRequest(String username1, String username2) throws SQLException {
		String sql = "INSERT INTO numbers (sender_username,receiver_username) VALUES('"+username1+"','"+username2+"')";
		stmt.executeUpdate(sql);

	}

	void acceptRequest(String username1, String username2) throws SQLException {//delete from request and insert in friends
		String sql1="DELETE FROM requests where( sender_username='"+username1+"' and receiver_username='"+username2+"')";
		String sql2= "INSERT INTO friends (username_1,username_2) VALUES('"+username1+"','"+username2+"')";
		stmt.executeUpdate(sql1);
		stmt.executeUpdate(sql2);

	}

	void initChat(String title) {
		

	}

	void addMemberToChat(String chatTitle, String username) {

	}

	void setMessage(String message, String chatTitle, String username) {

	}

	boolean checkUser(String username, String password) {

		return false;
	}

	void close() throws SQLException {

		stmt.close();
		co.close();

	}

}
