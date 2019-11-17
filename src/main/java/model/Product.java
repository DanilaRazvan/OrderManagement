package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {

	private Long productID;
	private String name;
	private Long quantity;
	private Double price;
	
	public Product() {
		super();
	}
	
	public Product(Long productID, String name, Long quantity, Double price) {
		super();
		this.productID = productID;
		this.name = name;
		this.quantity = quantity;
		this.price = BigDecimal.valueOf(price)
			    .setScale(3, RoundingMode.HALF_UP)
			    .doubleValue();
	}
	
	public Product(String name, Long quantity, Double price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.price = BigDecimal.valueOf(price)
			    .setScale(3, RoundingMode.HALF_UP)
			    .doubleValue();
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = BigDecimal.valueOf(price)
			    .setScale(3, RoundingMode.HALF_UP)
			    .doubleValue();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
