package model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

public class Order {

	private Long orderID;
	private Long clientID;
	private String date;
	
	public Order() {
		super();
	}
	
	public Order(Long clientID) {
		super();
		this.clientID = clientID;
		
		Date d = Date.valueOf(LocalDateTime.now().toLocalDate());
		Time t = Time.valueOf(LocalDateTime.now().toLocalTime());
		this.date = d.toString() + " " + t.toString();
	}

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}

	public Long getClientID() {
		return clientID;
	}

	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", customerID=" + clientID + ", date=" + date.toString() + "]";
	}
	
}
