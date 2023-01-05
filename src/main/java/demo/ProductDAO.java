package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDAO{
	private Connection connection = null;
	
	public ProductDAO() throws ClassNotFoundException {
		this.connection = new MyConnection().getConnection();
	}
	
	public ArrayList<Product> gets() throws SQLException {
		ArrayList<Product> products = new ArrayList<>();
		String sql = "SELECT * FROM product;";
		Statement stm = this.connection.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				int price = rs.getInt(3);
				int quantity = rs.getInt(4);
				Product product = new Product(id, name, price, quantity);
				products.add(product);
			}
			return products;
		}
		return null;
	}
	
	public Product get(int id) throws SQLException {
		Product product = new Product();
		String sql = "SELECT * FROM product WHERE id = ?;";
		PreparedStatement preStm = this.connection.prepareStatement(sql);
		preStm.setInt(1, id);
		ResultSet rs = preStm.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				String name = rs.getString(2);
				int price = rs.getInt(3);
				int quantity = rs.getInt(4);
				product.setId(id);
				product.setName(name);
				product.setPrice(price);
				product.setQuantity(quantity);
			}
			return product;
		}
		return null;
	}
	
	public boolean insert(Product product) throws SQLException {
		String sql = "INSERT INTO product(name, price, quantity) VALUES(?, ?, ?);";
		PreparedStatement preStm = this.connection.prepareStatement(sql);
		preStm.setString(1, product.getName());
		preStm.setInt(2, product.getPrice());
		preStm.setInt(3, product.getQuantity());
		int result = preStm.executeUpdate();
		if (result == 1)
			return true;
		return false;
	}
	
	public boolean update(Product product) throws SQLException {
		String sql = "UPDATE product SET name = ?, price = ?, quantity = ? WHERE id = ?;";
		PreparedStatement preStm = this.connection.prepareStatement(sql);
		preStm.setString(1, product.getName());
		preStm.setInt(2, product.getPrice());
		preStm.setInt(3, product.getQuantity());
		preStm.setInt(4, product.getId());
		int result = preStm.executeUpdate();
		if (result == 1)
			return true;
		return false;
	}
	
	public boolean delete(int id) throws SQLException {
		String sql = "DELETE FROM product WHERE id = ?;";
		PreparedStatement preStm = this.connection.prepareStatement(sql);
		preStm.setInt(1, id);
		int result = preStm.executeUpdate();
		if (result == 1)
			return true;
		return false;
	}
}
