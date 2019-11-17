package bll;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import dao.OrderDAO;
import model.Order;

public class OrderBLL {

	private OrderDAO oDAO = new OrderDAO();
	
	public OrderBLL() {
		
	}
	
	/**
	 * Finds an Order in the database
	 * 
	 * @param id - Order id to be found
	 * @return - the found Order
	 */
	public Order findById(Long id)  throws NoSuchElementException {
		Order o = oDAO.findByID(id);
		
		if (o == null) {
			throw new NoSuchElementException("ERROR 404! Order with id = " + id + " not found!");
		}
		
		return o;	
	}
	
	/**
	 * Finds all Orders in the Database
	 * 
	 * @return an ArrayList with all Orders from the database
	 */
	public ArrayList<Order> findAll() throws NoSuchElementException{
		ArrayList<Order> orders = oDAO.findAll();
		
		if(orders == null) {
			throw new NoSuchElementException("ERROR 404! Orders not found!");
		}
		
		return orders;
	}
	
	/**
	 * 
	 * inserts an Order into the database
	 * 
	 * @param o - an Order
	 * @return a Long - the id of inserted Client
	 */
	public Long insert(Order o) {
		return oDAO.insert(o);
	}
	
	/**
	 * 
	 * Updates an Order from the database
	 * 
	 * @param o - an order with the new fields
	 * @param id - id of the order to be updated
	 * @return true if the updated succeded, false on the other hand
	 */
	public boolean update(Order o, Long id) {
		return oDAO.update(o, id);
	}
	
	/**
	 * Delete an Order from Database
	 * 
	 * @param id - id of the order to be deleted
	 * @return true if the delete succeded, false on the other hand
	 */
	public boolean delete(Long id) {
		return oDAO.delete(id);
	}
}
