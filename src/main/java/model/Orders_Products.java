package model;

public class Orders_Products {
	private Long orders_productsID;
	private Long productID;
	private Long orderID;
	private Long quantity;
	
	public Orders_Products() {
		super();
	}
	
	public Orders_Products(Long orderID, Long productID, Long quantity) {
		super();
		this.orderID = orderID;
		this.productID = productID;
		this.quantity = quantity;
	}
	
	public Long getOrders_productsID() {
		return orders_productsID;
	}

	public void setOrders_productsID(Long orders_productsID) {
		this.orders_productsID = orders_productsID;
	}

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	public String toString() {
		return "Order[" + this.orderID + "]; Product[" + this.productID + "]; Quantity[" + this.quantity + "]";
	}
}
