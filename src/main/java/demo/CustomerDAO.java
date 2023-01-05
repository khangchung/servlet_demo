package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
	private Connection connection;
	
	public CustomerDAO() throws ClassNotFoundException {
		this.connection = new MyConnection().getConnection();
	}
	
	public Customer get(String username) throws SQLException {
		Customer customer = new Customer();
		String sql = "SELECT username, password, role FROM customer WHERE username = ?;";
		PreparedStatement preStm = this.connection.prepareStatement(sql);
		preStm.setString(1, username);
		ResultSet rs = preStm.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				String password = rs.getString(2);
				String role = rs.getString(3);
			}
			return customer;
		}
		return null;
	}
}
