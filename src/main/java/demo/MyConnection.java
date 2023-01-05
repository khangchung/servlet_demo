package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
	private final String url = "jdbc:mysql://localhost:3306/demo";
	private final String user = "lab";
	private final String password = "123456";
	private Connection connection;
	
	public MyConnection() throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		try {
			this.connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			this.connection = null;
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
