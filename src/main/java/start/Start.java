package start;

import javafx.application.*;
import javafx.stage.Stage;
import presentation.UI;

public class Start extends Application {

	public static void main(String[] args) {		
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		UI ui = new UI();
		stage.setTitle("Orders Management");
		stage.setScene(ui.getScene());
		stage.show();
		stage.setResizable(false);
	}
}
