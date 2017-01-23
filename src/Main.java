import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) throws SQLException {
		Driver driver=new Driver();
		driver.insertUser("ava", "8767");
		driver.close();
		

	}
}
