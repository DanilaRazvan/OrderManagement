package presentation;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.Orders_ProductsBLL;
import bll.ProductBLL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.Client;
import model.Order;
import model.Orders_Products;
import model.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

@SuppressWarnings({ "unused", "unchecked" })
public class ManagementUI {

	private Scene scene;
	private Pane canvas;

	private Button addB;
	private Button delB;
	private Button submitB;

	private TableView<Product> productsT = new TableView<Product>();
	private TableView<Client> clientsT = new TableView<Client>();
	private TableView<Orders_Products> orderT = new TableView<Orders_Products>();

	private TextField searchClient;
	private TextField searchProduct;
	private TextField quantityText;

	private Order order;
	private Client client;

	private static DecimalFormat df2 = new DecimalFormat(".##");

	public ManagementUI() {

		this.addB = new Button("ADD");
		this.addB.setPrefSize(56, 20);
		this.addB.setTranslateX(40);
		this.addB.setTranslateY(30);

		this.delB = new Button("DEL");
		this.delB.setPrefSize(56, 20);
		this.delB.setTranslateX(100);
		this.delB.setTranslateY(30);

		this.submitB = new Button("SUBMIT");
		this.submitB.setPrefSize(60, 20);
		this.submitB.setTranslateX(280);
		this.submitB.setTranslateY(30);

		this.quantityText = new TextField();
		this.quantityText.setPromptText("Quantity");
		this.quantityText.setPrefSize(300, 20);
		this.quantityText.setTranslateX(40);
		this.quantityText.setTranslateY(80);

		this.searchProduct = new TextField();
		this.searchProduct.setPromptText("Search Product");
		this.searchProduct.setPrefSize(300, 20);
		this.searchProduct.setTranslateX(390);
		this.searchProduct.setTranslateY(30);

		this.productsT = new TableView<Product>();
		Product p = new Product();
		TableGen<Product> tp = new TableGen<Product>();
		this.productsT = tp.genTable(p);
		this.productsT.setPrefSize(300, 250);
		this.productsT.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.productsT.setTranslateX(390);
		this.productsT.setTranslateY(80);

		this.searchClient = new TextField();
		this.searchClient.setPromptText("Search Client");
		this.searchClient.setPrefSize(300, 20);
		this.searchClient.setTranslateX(740);
		this.searchClient.setTranslateY(30);

		this.clientsT = new TableView<Client>();
		Client c = new Client();
		TableGen<Client> tc = new TableGen<Client>();
		this.clientsT = tc.genTable(c);
		this.clientsT.setPrefSize(300, 250);
		this.clientsT.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.clientsT.setTranslateX(740);
		this.clientsT.setTranslateY(80);
		this.clientsT.setEditable(true);
		/**
		 * set an listener on the rows If row clicked create a new order and disable the
		 * clients table
		 */
		this.clientsT.setRowFactory(row -> {
			TableRow<Client> r = new TableRow<Client>();
			r.setOnMouseClicked(e -> {
				if (!r.isEmpty()) {
					client = r.getItem();
					order = new Order(client.getClientID());
					OrderBLL obll = new OrderBLL();
					Long orderID = obll.insert(order);
					order.setOrderID(orderID);
					clientsT.setDisable(true);
				}
			});

			return r;
		});

		this.orderT.setEditable(true);
		TableColumn<Orders_Products, String> pName = new TableColumn<Orders_Products, String>("Name");
		TableColumn<Orders_Products, Long> pQuantity = new TableColumn<Orders_Products, Long>("Quantity");

		pName.setCellValueFactory(val -> {
			ProductBLL pbll = new ProductBLL();
			Product prod = pbll.findById(val.getValue().getProductID());

			StringProperty var = new SimpleStringProperty((String) prod.getName());
			return var;
		});

		pQuantity.setCellValueFactory(val -> {
			ReadOnlyObjectWrapper<Long> q = new ReadOnlyObjectWrapper<Long>(val.getValue().getQuantity());
			return q;
		});

		this.orderT.getColumns().addAll(pName, pQuantity);
		this.orderT.setPrefSize(300, 210);
		this.orderT.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.orderT.setTranslateX(40);
		this.orderT.setTranslateY(120);

		this.canvas = new Pane();
		this.canvas.getChildren().addAll(this.addB, this.delB, this.submitB, this.searchProduct, this.productsT,
				this.searchClient, this.clientsT, this.orderT, this.quantityText);
		this.scene = new Scene(canvas, 1080, 360);

		setHandlers();

		showClientsTableData(null);
		showProductsTableData(null);
	}

	private void showClientsTableData(Client c) {
		if (c == null) {
			ClientBLL cbll = new ClientBLL();
			ArrayList<Client> clients = cbll.findAll();
			ObservableList<Client> d = FXCollections.observableArrayList(clients);
			clientsT.setItems(d);
		} else {
			ObservableList<Client> d = FXCollections.observableArrayList(c);
			clientsT.setItems(d);
		}
	}

	private void showProductsTableData(Product p) {
		if (p == null) {
			ProductBLL pbll = new ProductBLL();
			ArrayList<Product> products = pbll.findAll();
			ObservableList<Product> d = FXCollections.observableArrayList(products);
			productsT.setItems(d);
		} else {
			ObservableList<Product> d = FXCollections.observableArrayList(p);
			productsT.setItems(d);
		}
	}

	private void showOrderTableData() {
		Orders_ProductsBLL opbll = new Orders_ProductsBLL();
		ArrayList<Orders_Products> op = opbll.findAllForID(order.getOrderID());
		ObservableList<Orders_Products> d = FXCollections.observableArrayList(op);
		orderT.setItems(d);
	}

	/**
	 * sets the listeners for the buttons and text fields
	 */
	private void setHandlers() {
		ClientBLL cbll = new ClientBLL();
		ProductBLL pbll = new ProductBLL();
		Orders_ProductsBLL opbll = new Orders_ProductsBLL();

		/**
		 * if required quantity <= quantity in stock then takes the selected product and
		 * adds it into the order table and also into the database else display a
		 * message
		 */
		this.addB.setOnAction(e -> {
			Long orderId = order.getOrderID();
			Long productId = productsT.getSelectionModel().getSelectedItem().getProductID();
			Long quantity = Long.parseLong(quantityText.getText());
			Orders_Products op = new Orders_Products(orderId, productId, quantity);
			System.out.println(op);
			Product prod = pbll.findById(productId);
			if (quantity <= prod.getQuantity()) {
				System.out.println(orderId + " " + productId + " " + quantity + "\n" + prod.toString());
				opbll.insert(op);
				orderT.getItems().clear();
				showOrderTableData();
			} else {
				Pane canvass = new Pane();
				Scene scene = new Scene(canvass, 250, 100);
				Label message = new Label("Not enough products");
				message.setPrefSize(250, 100);
				message.setAlignment(Pos.CENTER);
				canvass.getChildren().add(message);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Attention");
				stage.show();
			}

		});

		/**
		 * delete a product from the order table
		 */
		this.delB.setOnAction(e -> {
			opbll.delete(orderT.getSelectionModel().getSelectedItem().getOrders_productsID());
			orderT.getItems().clear();
			showOrderTableData();
		});

		/**
		 * updates the database and generates the pdf file with the order details
		 */
		this.submitB.setOnAction(e -> {
			updateDB();
			generateOrderFile();
			getScene().getWindow().hide();
		});

		this.searchClient.setOnKeyPressed(e -> {
			try {
				if (e.getCode().equals(KeyCode.ENTER) && !searchClient.getText().equals("")) {
					Client c = cbll.findByName(searchClient.getText());
					clientsT.getItems().clear();
					showClientsTableData(c);
				}
			} catch (NoSuchElementException ex) {
				clientsT.getItems().clear();
				showClientsTableData(null);

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

		});

		this.searchProduct.setOnKeyPressed(e -> {
			try {
				if (e.getCode().equals(KeyCode.ENTER) && !searchProduct.getText().equals("")) {
					Product p = pbll.findByName(searchProduct.getText());
					productsT.getItems().clear();
					showProductsTableData(p);
				}
			} catch (NoSuchElementException ex) {
				productsT.getItems().clear();
				showProductsTableData(null);

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
		});
	}

	private void updateDB() {
		ProductBLL pbll = new ProductBLL();
		for (Orders_Products op : orderT.getItems()) {
			Product p = pbll.findById(op.getProductID());
			p.setQuantity(p.getQuantity() - op.getQuantity());
			pbll.update(p, p.getProductID());
		}
	}

	/**
	 * creates the pdf file with the order details
	 */
	private void generateOrderFile() {
		Document pdf = new Document();

		try {
			PdfWriter w = PdfWriter.getInstance(pdf, new FileOutputStream("Order" + order.getOrderID() + ".pdf"));
			pdf.open();
			pdf.addTitle("Order#" + order.getOrderID());
			Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			Chunk noOfOrder = new Chunk("Order#" + order.getOrderID() + "\n", font);
			Chunk cl = new Chunk("Client: " + client.getName() + "\n", font);
			Chunk address = new Chunk("Address: " + client.getAddress() + "\n", font);
			Chunk date = new Chunk("Date: " + order.getDate() + "\n", font);

			pdf.add(new Paragraph(noOfOrder));
			pdf.add(new Paragraph(cl));
			pdf.add(new Paragraph(address));
			pdf.add(new Paragraph(date));

			PdfPTable t = new PdfPTable(3);
			t.setWidthPercentage(100);
			t.setSpacingAfter(10f);
			t.setSpacingBefore(10f);
			float[] widths = { 1f, 1f, 1f };
			t.setWidths(widths);
			PdfPCell c1 = new PdfPCell(new Paragraph("Product Name"));
			PdfPCell c2 = new PdfPCell(new Paragraph("Quantity"));
			PdfPCell c3 = new PdfPCell(new Paragraph("Unit Price"));
			c1.setPadding(10);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			t.addCell(c1);
			c2.setPadding(10);
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setVerticalAlignment(Element.ALIGN_CENTER);
			t.addCell(c2);
			c3.setPadding(10);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			c3.setVerticalAlignment(Element.ALIGN_CENTER);
			t.addCell(c3);

			ProductBLL pbll = new ProductBLL();
			Product p;
			double sum = 0;
			for (Orders_Products op : orderT.getItems()) {
				p = pbll.findById(op.getProductID());
				PdfPCell c11 = new PdfPCell(new Paragraph(p.getName() + ""));
				c11.setHorizontalAlignment(Element.ALIGN_CENTER);
				c11.setVerticalAlignment(Element.ALIGN_CENTER);
				t.addCell(c11);

				PdfPCell c22 = new PdfPCell(new Paragraph(op.getQuantity() + ""));
				c22.setHorizontalAlignment(Element.ALIGN_CENTER);
				c22.setVerticalAlignment(Element.ALIGN_CENTER);
				t.addCell(c22);

				PdfPCell c33 = new PdfPCell(new Paragraph("x " + p.getPrice() + ""));
				c33.setHorizontalAlignment(Element.ALIGN_CENTER);
				c33.setVerticalAlignment(Element.ALIGN_CENTER);
				t.addCell(c33);

				sum += p.getPrice() * op.getQuantity();
			}

			pdf.add(t);

			Chunk totalSum = new Chunk("Total Sum = " + df2.format(sum), font);
			pdf.add(totalSum);

			pdf.close();
			w.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}

	}

	public Scene getScene() {
		return scene;
	}
}
