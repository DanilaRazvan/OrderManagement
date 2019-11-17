package bll.validators;

import model.Client;

public class PhoneValidator implements Validator<Client> {
	
	@Override
	public boolean validate(Client t) throws Exception {
		if (!t.getPhoneNumber().isEmpty() && !t.getPhoneNumber().matches("^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$")) {
			throw new Exception("Wrong phone number");
		}
		return true;
	}

}
