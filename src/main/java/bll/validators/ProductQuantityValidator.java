package bll.validators;

import model.Product;

public class ProductQuantityValidator implements Validator<Product> {
	
	@Override
	public boolean validate(Product t) throws Exception {
		if (!t.getQuantity().toString().isEmpty() && !t.getQuantity().toString().matches("^\\d+$")) {
			
			throw new Exception("Wrong quantity");
		}
		return true;
	}
}
