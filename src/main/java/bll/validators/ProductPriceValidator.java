package bll.validators;

import model.Product;

public class ProductPriceValidator implements Validator<Product> {
	
	@Override
	public boolean validate(Product t) throws Exception {
		if (!String.valueOf(t.getPrice()).matches("^\\d{1,6}(\\.\\d{1,2})?$")) {
			throw new Exception("Wrong price");
		}
		return true;
	}
}
