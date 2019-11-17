package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import connection.ConnectionFactory;
import model.Product;

public class ProductDAO extends AbstractDAO<Product> {
	
	private String createSelectPriceQueryByName(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT productID FROM Product WHERE name = " + name);
		return sb.toString();
	}
	
	public Long findByNameGetPrice(String name) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectPriceQueryByName(name);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				return resultSet.getLong(4);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "ProductDAO:findByName - get ID" + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}
	
}
