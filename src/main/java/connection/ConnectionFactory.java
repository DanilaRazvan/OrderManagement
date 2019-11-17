package connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {

	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/OrdersManagement?useSSL=false";
	private static final String USER = "root";
	private static final String PASS = "root";
	
	private static ConnectionFactory singleInstance = new ConnectionFactory();
	
	private ConnectionFactory() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Connection createConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DBURL, USER, PASS);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static Connection getConnection() {
		return singleInstance.createConnection();
	}
	
	public static void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
		}
	}
	
	public static void close(Statement statement) {
		try {
			statement.close();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
		}
	}

	public static void close(ResultSet resultSet) {
		try {
			resultSet.close();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
		}
	}
}
