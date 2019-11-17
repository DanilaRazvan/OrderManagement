package presentation;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import model.Client;

import java.util.ArrayList;
import java.util.Observable;

import bll.ClientBLL;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

@SuppressWarnings({ "unused" })
public class ClientsUI {

	private Scene scene;
	private Pane canvas;

	private TextField name;
	private TextField address;
	private TextField email;
	private TextField phone;

	private Button addB;
	private Button updateB;
	private Button deleteB;

	private TableView<Client> clientsT;

	public ClientsUI() {
		this.name = new TextField();
		this.name.setPromptText("NAME");
		this.name.setAlignment(Pos.CENTER);
		this.name.setPrefSize(130, 30);
		this.name.setTranslateX(30);
		this.name.setTranslateY(40);

		this.address = new TextField();
		this.address.setPromptText("ADDRESS");
		this.address.setAlignment(Pos.CENTER);
		this.address.setPrefSize(130, 30);
		this.address.setTranslateX(30);
		this.address.setTranslateY(90);

		this.email = new TextField();
		this.email.setPromptText("EMAIL");
		this.email.setAlignment(Pos.CENTER);
		this.email.setPrefSize(130, 30);
		this.email.setTranslateX(30);
		this.email.setTranslateY(140);

		this.phone = new TextField();
		this.phone.setPromptText("PHONE");
		this.phone.setAlignment(Pos.CENTER);
		this.phone.setPrefSize(130, 30);
		this.phone.setTranslateX(30);
		this.phone.setTranslateY(190);

		this.clientsT = new TableView<Client>();
		Client c = new Client();
		TableGen<Client> t = new TableGen<Client>();
		this.clientsT = t.genTable(c);
		this.clientsT.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.clientsT.setPrefSize(860, 300);
		this.clientsT.setTranslateX(190);
		this.clientsT.setTranslateY(30);

		/**
		 * set an ActionListener on the rows of the table
		 */
		clientsT.setRowFactory(val -> {
			TableRow<Client> r = new TableRow<Client>();
			r.setOnMouseClicked(e -> {
				try {
					setTexts(r.getItem().getName(), r.getItem().getAddress(), r.getItem().getEmail(),
							r.getItem().getPhoneNumber());
				} catch (NullPointerException ex) {
					Pane canvass = new Pane();
					Scene scene = new Scene(canvass, 250, 100);
					Label message = new Label("Please select a client");
					message.setPrefSize(250, 100);
					message.setAlignment(Pos.CENTER);
					canvass.getChildren().add(message);
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
		this.canvas.getChildren().addAll(this.name, this.address, this.email, this.phone, this.addB, this.updateB,
				this.deleteB, this.clientsT);
		this.scene = new Scene(canvas, 1080, 360);

		setHandlers();
		showTableData();
	}

	private boolean checkClients(Client c) throws Exception {
		ClientBLL cbll = new ClientBLL();
		ArrayList<Client> clients = cbll.findAll();

		for (Client cl : clients) {
			if (cl.getName().equals(c.getName())) {
				throw new Exception("Name already exists");
			}
			
			if (cl.getEmail().equals(c.getEmail())) {
				throw new Exception("Email already exists");
			}

			if (cl.getPhoneNumber().equals(c.getPhoneNumber())) {
				throw new Exception("Phone number already exists");
			}
		}

		return true;
	}

	/**
	 * Sets the listeners of the buttons
	 */
	private void setHandlers() {
		ClientBLL cbll = new ClientBLL();

		/**
		 * if all text fields filled correctly than attempts to insert a Client into the
		 * database
		 */
		this.addB.setOnAction(e -> {
			if (name.getText().isEmpty() || address.getText().isEmpty() || email.getText().isEmpty()
					|| phone.getText().isEmpty()) {
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
				Client c = new Client(name.getText(), address.getText(), email.getText(), phone.getText());
				try {
					if (cbll.checkValidators(c) && checkClients(c)) {
						cbll.insert(c);
						setEmptyTexts();
						clientsT.getItems().clear();
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
		 * Client from the database
		 */
		this.updateB.setOnAction(e -> {
			if (name.getText().isEmpty() || address.getText().isEmpty() || email.getText().isEmpty()
					|| phone.getText().isEmpty()) {
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
				Client c = new Client(clientsT.getSelectionModel().getSelectedItem().getClientID(), name.getText(),
						address.getText(), email.getText(), phone.getText());
				try {
					if (cbll.checkValidators(c)) {
						cbll.update(c, clientsT.getSelectionModel().getSelectedItem().getClientID());
						setEmptyTexts();
						clientsT.getItems().clear();
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
		 * attempts to delete the selected client from the database
		 */
		this.deleteB.setOnAction(e -> {

			try {
				cbll.delete(clientsT.getSelectionModel().getSelectedItem().getClientID());
				setEmptyTexts();
				clientsT.getItems().clear();
				showTableData();
			} catch (NullPointerException ex) {
				Pane canvass = new Pane();
				Scene scene = new Scene(canvass, 250, 100);
				Label message = new Label("Please select a client");
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
		this.address.setText("");
		this.email.setText("");
		this.phone.setText("");
	}

	private void setTexts(String name, String address, String email, String phone) {
		this.name.setText(name);
		this.address.setText(address);
		this.email.setText(email);
		this.phone.setText(phone);
	}

	private void showTableData() {
		ClientBLL cbll = new ClientBLL();
		ArrayList<Client> clients = cbll.findAll();
		ObservableList<Client> d = FXCollections.observableArrayList(clients);
		clientsT.setItems(d);
	}

	public Scene getScene() {
		return scene;
	}

}
