package presentation;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class UI {
	private Scene scene;
	private Pane canvas;

	private Button clients;
	private Button products;
	private Button order;

	public UI() {
		this.clients = new Button("Clients operations");
		this.clients.setPrefSize(200, 50);
		this.clients.setTranslateX(100);
		this.clients.setTranslateY(50);

		this.products = new Button("Products operations");
		this.products.setPrefSize(200, 50);
		this.products.setTranslateX(100);
		this.products.setTranslateY(150);

		this.order = new Button("Management");
		this.order.setPrefSize(200, 50);
		this.order.setTranslateX(100);
		this.order.setTranslateY(250);

		this.canvas = new Pane();
		this.scene = new Scene(canvas, 400, 350);
		this.canvas.getChildren().addAll(this.clients, this.products, this.order);

		setHandlers();
	}

	/**
	 * sets the listeners for the buttons
	 */
	private void setHandlers() {
		this.clients.setOnAction(e -> {
			Stage s = new Stage();
			s.setScene(new ClientsUI().getScene());
			s.show();
			s.setTitle("Clients Management");
			s.setResizable(false);
		});

		this.products.setOnAction(e -> {
			Stage s = new Stage();
			s.setScene(new ProductsUI().getScene());
			s.show();
			s.setTitle("Products Management");
			s.setResizable(false);
		});
		
		this.order.setOnAction(e -> {
			Stage s = new Stage();
			s.setScene(new ManagementUI().getScene());
			s.show();
			s.setTitle("Orders Management");
			s.setResizable(false);
		});
	}
	
	public Scene getScene() {
		return this.scene;
	}
}
