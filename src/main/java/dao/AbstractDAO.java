package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

@SuppressWarnings("unchecked")
public class AbstractDAO<T> {

	protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
	protected final Class<T> type;

	public AbstractDAO() {
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private String createSelectQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append(" * ");
		sb.append("FROM ");
		sb.append(type.getSimpleName());

		return sb.toString();
	}

	private String createSelectQuery(String field) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append(" * ");
		sb.append("FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE " + field + " = ?");

		return sb.toString();
	}

	/**
	 * Creates a connection with the database;
	 * Executes the correct querry and calls a function to create the Objects to be returned
	 * 
	 * @return ArrayList of Class T - class of the instance that calls this function
	 */
	public ArrayList<T> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery();
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
			return (ArrayList<T>) createObjects(resultSet);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}

		return null;
	}
	
	/**
	 * Creates a connection with the database;
	 * Executes the correct querry and calls a function to create the Object to be returned
	 * 
	 * @param id - id to be found in the database
	 * @return instance of Class T - class of the instance that calls this function
	 */
	public T findByID(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery(type.getDeclaredFields()[0].getName());		
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			return createObjects(resultSet).get(0);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findById" + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}
	
	/**
	 * Creates a connection with the database;
	 * Executes the correct querry and calls a function to create the Object to be returned
	 * 
	 * @param name - string to be found in the database in the name column
	 * @return instance of Class T - class of the instance that calls this function
	 */
	public T findByName(String name) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery(type.getDeclaredFields()[1].getName());		
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			resultSet = statement.executeQuery();
			return (createObjects(resultSet).size() == 0)? null:createObjects(resultSet).get(0);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findById" + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Creates the objects through reflection techniques
	 * 
	 * @param resultSet - a resultset obtained by executing a querry
	 * @return a List of objects of type class of the instance that calls the function
	 */
	private List<T> createObjects(ResultSet resultSet) {
		List<T> list = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				T instance = type.newInstance();
				
				for (Field field : type.getDeclaredFields()) {
					Object value = resultSet.getObject(field.getName());
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getWriteMethod();
					method.invoke(instance, value);
				}
				list.add(instance);
				if(list.size() == 0) {
					throw new Exception("Size 0");
				}
			}
		} catch (InstantiationException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
		} catch (IllegalAccessException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
		} catch (InvocationTargetException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
		} catch (SecurityException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
		} catch (IntrospectionException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
		} catch (Exception e) {
			if(e.getMessage().compareTo("Size 0") == 0) {
				System.out.println("error - size 0");
			}
		}

		return list;
	}
	
	/**
	 * Creates a valid query for insertion into a database
	 * 
	 * @param t instance of class T
	 * @return a String - the created querry
	 */
	private String createInsertQuery(T t) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `");
		sb.append(type.getSimpleName() + "` (");
		boolean first = true;
		boolean second = true;
		for (Field field : type.getDeclaredFields()) {
			if (first) {
				first = false;
			} else if(second) {
				sb.append(field.getName());
				second = false;
			} else {
				sb.append(", " + field.getName());
			}
		}
		sb.append(") VALUES (");

		try {
			first = true;
			second = true;
			for (Field field : type.getDeclaredFields()) {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
				Method method = propertyDescriptor.getReadMethod();
				Object value = method.invoke(t);
				
				if (first) {
					//sb.append("'" + field.get(t) + "'");
					first = false;
				} else if(second) {
					sb.append("'" + value.toString() + "'");
					second = false;
				} else {
					sb.append(", '" + value.toString() + "'");
				}
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		sb.append(")");
		
		System.out.println(sb.toString());
		
		return sb.toString();
	}

	/**
	 * Inserts an Object of class T into th database
	 * 
	 * @param t instance of class T
	 * @return Long - the id of the inserted Object
	 */
	public Long insert(T t) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createInsertQuery(t);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.executeUpdate();
			
			ResultSet resultSet = statement.getGeneratedKeys();
			if(resultSet.next()) {
				return resultSet.getLong(1);
			}
			
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		
		return Long.valueOf(-1);
	}

	/**
	 * Creates a valid query for insertion into a database
	 * 
	 * @param t - Object with the new values
	 * @param id of the object tot be updated
	 * @return String - the created query
	 */
	private String createUpdateQuery(T t, Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE " + type.getSimpleName() + " SET ");

		try {
			String idc = "";
			boolean first = true;
			for (Field field : type.getDeclaredFields()) {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
				Method method = propertyDescriptor.getReadMethod();
				Object value = method.invoke(t);
				//System.out.println(value.toString());
				if (first) {
					sb.append(field.getName() + "='" + value.toString() + "'");
					idc = field.getName() + "='" + id + "'";
					first = false;
				} else {
					sb.append(", " + field.getName() + "='" + value.toString() + "'");
				}
			}
			
			sb.append(" WHERE " + idc);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Updates an Object of the database
	 * 
	 * @param t - object with the new values
	 * @param id - id of the object to be updated
	 * @return a boolean: true if succeded, false on the other hand
	 */
	public boolean update(T t, Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createUpdateQuery(t, id);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
			return false;
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}

		return true;
	}

	/**
	 * Creates a valid deletion query
	 * 
	 * @param id - objects id to be deleted
	 * @return String - the querry
	 */
	private String createDeleteQuery(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM " + type.getSimpleName() + " WHERE " + type.getDeclaredFields()[0].getName() + "=" + id);
		return sb.toString();
	}

	/**
	 * Deletes an object from the database
	 * 
	 * @param id - objects id to be deleted
	 * @return boolean: true if succeded, false on the other hand
	 */
	public boolean delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createDeleteQuery(id);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
			return false;
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}

		return true;
	}

	/**
	 * Method called for finding the object that have the same id from a many-to-many table
	 * 
	 * @param id - id to be found
	 * @return ArrayList of object of class type T
	 */
	public ArrayList<T> findAllForID(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery("orderID");
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setLong(1, id);
			
			System.out.println(statement);
			
			resultSet = statement.executeQuery();
			return (ArrayList<T>) createObjects(resultSet);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}

		return null;
	}
}
