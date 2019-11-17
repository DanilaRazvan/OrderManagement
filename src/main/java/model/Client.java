package model;

public class Client {
	
	private Long clientID;
	private String name;
	private String address;
	private String email;
	private String phoneNumber;
	
	public Client() {
		super();
	}
	
	public Client(Long id, String name, String address, String email, String phone) {
		super();
		this.clientID = id;
		this.name = name;
		this.address = address;
		this.email = email;
		this.phoneNumber = phone;
	}
	
	public Client(String name, String address, String email, String phone) {
		super();
		this.name = name;
		this.address = address;
		this.email = email;
		this.phoneNumber = phone;
	}

	public Long getClientID() {
		return clientID;
	}

	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		return "Client with ID = " + this.clientID + "; Name = " + this.name + "; Address = " + this.address + "; Email = " + this.email + "; Phone = " + this.phoneNumber;
	}
	
}
