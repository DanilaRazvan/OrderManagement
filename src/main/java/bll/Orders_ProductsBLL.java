package bll;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import dao.Orders_ProductsDAO;
import model.Orders_Products;

public class Orders_ProductsBLL {


	private Orders_ProductsDAO opDAO = new Orders_ProductsDAO();
	
	public Orders_ProductsBLL() {
		
	}
	
	public Orders_Products findById(Long id) throws NoSuchElementException {
		Orders_Products op = opDAO.findByID(id);
		
		if (op == null) {
			throw new NoSuchElementException("ERROR 404! Order_Product with id = " + id + " not found!");
		}
		
		return op;	
	}
	
	public ArrayList<Orders_Products> findAll() throws NoSuchElementException{
		ArrayList<Orders_Products> ops = opDAO.findAll();
		
		if(ops == null) {
			throw new NoSuchElementException("ERROR 404! Orders_Products not found!");
		}
		
		return ops;
	}
	
	public Long insert(Orders_Products op) {
		return opDAO.insert(op);
	}
	
	public boolean update(Orders_Products op, Long id) {
		return opDAO.update(op, id);
	}
	
	public boolean delete(Long id) {
		return opDAO.delete(id);
	}
	
	public ArrayList<Orders_Products> findAllForID(Long id) throws NoSuchElementException {
		ArrayList<Orders_Products> list = opDAO.findAllForID(id);
		
		if(list == null) {
			throw new NoSuchElementException("ERROR 404! Order_product not found");
		}
	
		return list;
	}
}
