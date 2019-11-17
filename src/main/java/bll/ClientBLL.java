package bll;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import bll.validators.ClientNameValidator;
import bll.validators.EmailValidator;
import bll.validators.PhoneValidator;
import bll.validators.Validator;
import dao.ClientDAO;
import model.Client;

public class ClientBLL {
	
	private ArrayList<Validator<Client>> val = new ArrayList<Validator<Client>>();
	private ClientDAO cDAO = new ClientDAO();
	
	public ClientBLL() {
		val.add(new EmailValidator());
		val.add(new ClientNameValidator());
		val.add(new PhoneValidator());
	}
	
	/**
	 * Finds a Client in the database
	 * 
	 * @param id - Client id to be found
	 * @return - the found Client
	 */
	public Client findById(Long id) throws NoSuchElementException {
		Client c = cDAO.findByID(id);
		
		if (c == null) {
			throw new NoSuchElementException("ERROR 404! Client with id = " + id + " not found!");
		}
		
		return c;	
	}
	
	/**
	 * Finds a Client in the Database
	 * 
	 * @param name - Clients name to be found
	 * @return - the found Client
	 */
	public Client findByName(String name) throws NoSuchElementException {
		Client c = cDAO.findByName(name);
		
		if (c == null) {
			throw new NoSuchElementException("ERROR 404! Client with id = " + name + " not found!");
		}
		
		return c;	
	}
	
	/**
	 * Finds all Clients in the Database
	 * 
	 * @return an ArrayList with all Clients from the database
	 */
	public ArrayList<Client> findAll() throws NoSuchElementException{
		ArrayList<Client> clients = cDAO.findAll();
		
		if(clients == null) {
			throw new NoSuchElementException("ERROR 404! Clients not found!");
		}
		
		return clients;
	}
	
	/**
	 * 
	 * inserts a Client into the database
	 * 
	 * @param c - a Client
	 * @return a Long - the id of inserted Client
	 */
	public Long insert(Client c) {
		return cDAO.insert(c);
	}
	
	/**
	 * 
	 * Updates a Client from the database
	 * 
	 * @param c - a client with the new fields
	 * @param id - id of the client to be updated
	 * @return true if the updated succeded, false on the other hand
	 */
	public boolean update(Client c, Long id) {
		return cDAO.update(c, id);
	}
	
	/**
	 * Delete a Client from Database
	 * 
	 * @param id - id of the client to be deleted
	 * @return true if the delete succeded, false on the other hand
	 */
	public boolean delete(Long id) {
		return cDAO.delete(id);
	}
	
	/**
	 * 
	 * Checks if the values from a Client are correct
	 * 
	 * @param c - a Client
	 * @return true if all the fields have correct values, false on the other hand
	 * @throws Exception containing a message with a warning regarding the incorrect fields
	 */
	public boolean checkValidators(Client c) throws Exception {
		for(Validator<Client> v: val) {
			if(!v.validate(c)) {
				return false; 
			}
		}
		
		return true;
	}
}
