package bll.validators;

import model.Product;

public class ProductNameValidator implements Validator<Product> {
	
	@Override
	public boolean validate(Product t) throws Exception {
		if (!t.getName().isEmpty() && !t.getName().matches("^[a-zA-Z\\s0-9]*$")) {
			
			throw new Exception("Wrong name");
		}
		return true;
	}
}
