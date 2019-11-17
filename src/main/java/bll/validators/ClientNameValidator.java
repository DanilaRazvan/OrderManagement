package bll.validators;

import model.Client;

public class ClientNameValidator implements Validator<Client> {
	
	@Override
	public boolean validate(Client t) throws Exception {
		if (!t.getName().isEmpty() && !t.getName().matches("^[a-zA-Z\\s]*$")) {
			
			throw new Exception("Wrong name");
		}
		return true;
	}

}
