package bll;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import bll.validators.ProductNameValidator;
import bll.validators.ProductPriceValidator;
import bll.validators.ProductQuantityValidator;
import bll.validators.Validator;
import dao.ProductDAO;
import model.Product;

public class ProductBLL {

	private ArrayList<Validator<Product>> val = new ArrayList<Validator<Product>>();;
	private ProductDAO pDAO = new ProductDAO();
	
	public ProductBLL() {
		val.add(new ProductNameValidator());
		val.add(new ProductPriceValidator());
		val.add(new ProductQuantityValidator());
	}

	/**
	 * Finds a Product in the database
	 * 
	 * @param id - Product id to be found
	 * @return - the found Product
	 */
	public Product findById(Long id) throws NoSuchElementException {
		Product p = pDAO.findByID(id);
		
		if (p == null) {
			throw new NoSuchElementException("ERROR 404! Product with id = " + id + " not found!");
		}
		
		return p;
	}

	/**
	 * Finds a Product in the Database
	 * 
	 * @param name - Product name to be found
	 * @return - the found Product
	 */
	public Product findByName(String name) throws NoSuchElementException {
		Product c = pDAO.findByName(name);
		
		if (c == null) {
			throw new NoSuchElementException("ERROR 404! Client with id = " + name + " not found!");
		}
		
		return c;	
	}

	/**
	 * Finds all Products in the Database
	 * 
	 * @return an ArrayList with all Products from the database
	 */
	public ArrayList<Product> findAll() throws NoSuchElementException{
		ArrayList<Product> products = pDAO.findAll();
		
		if(products == null) {
			throw new NoSuchElementException("ERROR 404! Products not found!");
		}
		
		return products;
	}

	/**
	 * 
	 * inserts a Product into the database
	 * 
	 * @param c - a Product
	 * @return a Long - the id of inserted Product
	 */
	public Long insert(Product p) {
		return pDAO.insert(p);
	}

	/**
	 * 
	 * Updates a Product from the database
	 * 
	 * @param c - a Product with the new fields
	 * @param id - id of the Product to be updated
	 * @return true if the updated succeded, false on the other hand
	 */
	public boolean update(Product p, Long id) {
		return pDAO.update(p, id);
	}

	/**
	 * Delete a Product from Database
	 * 
	 * @param id - id of the Product to be deleted
	 * @return true if the delete succeded, false on the other hand
	 */
	public boolean delete(Long id) {
		return pDAO.delete(id);
	}

	/**
	 * 
	 * Checks if the values from a Product are correct
	 * 
	 * @param c - a Product
	 * @return true if all the fields have correct values, false on the other hand
	 * @throws Exception containing a message with a warning regarding the incorrect fields
	 */
	public boolean checkValidators(Product c) throws Exception {
		for(Validator<Product> v: val) {
			if(!v.validate(c)) {
				return false; 
			}
		}
		
		return true;
	}
}
