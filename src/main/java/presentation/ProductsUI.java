package presentation;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Client;
import model.Product;

import java.util.ArrayList;

import bll.ClientBLL;
import bll.ProductBLL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@SuppressWarnings({ "unused", "restriction" })
public class ProductsUI {

	private Scene scene;
	private Pane canvas;

	private TextField name;
	private TextField quantity;
	private TextField price;

	private Button addB;
	private Button updateB;
	private Button deleteB;

	private TableView<Product> productsT;

	public ProductsUI() {
		this.name = new TextField();
		this.name.setPromptText("NAME");
		this.name.setAlignment(Pos.CENTER);
		this.name.setPrefSize(130, 30);
		this.name.setTranslateX(30);
		this.name.setTranslateY(40);

		this.quantity = new TextField();
		this.quantity.setPromptText("QUANTITY");
		this.quantity.setAlignment(Pos.CENTER);
		this.quantity.setPrefSize(130, 30);
		this.quantity.setTranslateX(30);
		this.quantity.setTranslateY(90);

		this.price = new TextField();
		this.price.setPromptText("PRICE");
		this.price.setAlignment(Pos.CENTER);
		this.price.setPrefSize(130, 30);
		this.price.setTranslateX(30);
		this.price.setTranslateY(140);

		this.productsT = new TableView<Product>();
		Product p = new Product();
		TableGen<Product> t = new TableGen<Product>();
		this.productsT = t.genTable(p);
		this.productsT.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.productsT.setPrefSize(860, 300);
		this.productsT.setTranslateX(190);
		this.productsT.setTranslateY(30);
		/**
		 * sets a listener on the table rows
		 */
		this.productsT.setRowFactory(val -> {
			TableRow<Product> r = new TableRow<Product>();
			r.setOnMouseClicked(e -> {
				try {
					setTexts(r.getItem().getName(), r.getItem().getQuantity().toString(),
							String.valueOf(r.getItem().getPrice()));
				} catch (NullPointerException ex) {
					Pane canvass = new Pane();
					Scene scene = new Scene(canvass, 250, 100);
					Label message = new Label("Please select a product");
					message.setPrefSize(250, 100);
					message.setAlignment(Pos.CENTER);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("Attention");
					stage.show();
				}
			});

			return r;
		});

		this.addB = new Button("ADD");
		this.addB.setPrefSize(50, 30);
		this.addB.setTranslateX(30);
		this.addB.setTranslateY(240);

		this.updateB = new Button("UPDATE");
		this.updateB.setPrefSize(70, 30);
		this.updateB.setTranslateX(90);
		this.updateB.setTranslateY(240);

		this.deleteB = new Button("DELETE");
		this.deleteB.setPrefSize(70, 30);
		this.deleteB.setTranslateX(60);
		this.deleteB.setTranslateY(290);

		this.canvas = new Pane();
		this.canvas.getChildren().addAll(this.name, this.quantity, this.price, this.addB, this.updateB, this.deleteB,
				this.productsT);
		this.scene = new Scene(canvas, 1080, 360);

		setHandlers();
		showTableData();
	}

	private boolean checkProducts(Product p) throws Exception {
		ProductBLL pbll = new ProductBLL();
		ArrayList<Product> products = pbll.findAll();

		for (Product pr : products) {
			if (pr.getName().equals(p.getName())) {
				throw new Exception("Product already exxists");
			}
		}

		return true;
	}

	/**
	 * sets the listeners for buttons
	 */
	private void setHandlers() {
		ProductBLL pbll = new ProductBLL();

		/**
		 * if all text fields filled correctly than attempts to insert a Product into
		 * the database
		 */
		this.addB.setOnAction(e -> {
			if (name.getText().isEmpty() || quantity.getText().isEmpty() || price.getText().isEmpty()) {
				Pane canvass = new Pane();
				Scene scene = new Scene(canvass, 250, 100);
				Label message = new Label("Empty TextBox");
				message.setPrefSize(250, 100);
				message.setAlignment(Pos.CENTER);
				canvass.getChildren().add(message);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Attention");
				stage.show();
			} else {
				Product p = new Product(name.getText(), Long.valueOf(quantity.getText()),
						Double.valueOf(price.getText()));
				try {
					if (pbll.checkValidators(p) && checkProducts(p)) {
						pbll.insert(p);
						productsT.getItems().clear();
						setEmptyTexts();
						showTableData();
					}
				} catch (Exception ex) {
					Pane canvass = new Pane();
					Scene scene = new Scene(canvass, 250, 100);
					Label message = new Label(ex.getMessage());
					message.setPrefSize(250, 100);
					message.setAlignment(Pos.CENTER);
					canvass.getChildren().add(message);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("Attention");
					stage.show();
				}
			}
		});

		/**
		 * if all text fields filled correctly than attempts to update the selected
		 * Product from the database
		 */
		this.updateB.setOnAction(e -> {
			if (name.getText().isEmpty() || quantity.getText().isEmpty() || price.getText().isEmpty()) {
				Pane canvass = new Pane();
				Scene scene = new Scene(canvass, 250, 100);
				Label message = new Label("Empty TextBox");
				message.setPrefSize(250, 100);
				message.setAlignment(Pos.CENTER);
				canvass.getChildren().add(message);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Attention");
				stage.show();
			} else {
				Product p = new Product(productsT.getSelectionModel().getSelectedItem().getProductID(), name.getText(),
						Long.valueOf(quantity.getText()), Double.valueOf(price.getText()));
				try {
					if (pbll.checkValidators(p)) {
						pbll.update(p, productsT.getSelectionModel().getSelectedItem().getProductID());
						productsT.getItems().clear();
						showTableData();
						setEmptyTexts();
					}
				} catch (Exception ex) {
					Pane canvass = new Pane();
					Scene scene = new Scene(canvass, 250, 100);
					Label message = new Label(ex.getMessage());
					message.setPrefSize(250, 100);
					message.setAlignment(Pos.CENTER);
					canvass.getChildren().add(message);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("Attention");
					stage.show();
				}
			}
		});

		/**
		 * attempts to delete the selected Product from the database
		 */
		this.deleteB.setOnAction(e -> {
			try {
				pbll.delete(productsT.getSelectionModel().getSelectedItem().getProductID());
				setEmptyTexts();
				productsT.getItems().clear();
				showTableData();
			} catch (NullPointerException ex) {
				Pane canvass = new Pane();
				Scene scene = new Scene(canvass, 250, 100);
				Label message = new Label("Empty TextBox");
				message.setPrefSize(250, 100);
				message.setAlignment(Pos.CENTER);
				canvass.getChildren().add(message);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Attention");
				stage.show();
			}
		});
	}

	private void setEmptyTexts() {
		this.name.setText("");
		this.quantity.setText("");
		this.price.setText("");
	}

	private void setTexts(String name, String quantity, String price) {
		this.name.setText(name);
		this.quantity.setText(quantity);
		this.price.setText(price);
	}

	private void showTableData() {
		ProductBLL pbll = new ProductBLL();
		ArrayList<Product> products = pbll.findAll();
		ObservableList<Product> d = FXCollections.observableArrayList(products);
		productsT.setItems(d);
	}

	public Scene getScene() {
		return scene;
	}
}
