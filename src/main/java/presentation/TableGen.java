package presentation;

import java.lang.reflect.Field;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;

@SuppressWarnings("restriction")
public class TableGen<T> {

	/**
	 * Creates a table and its header through reflexion techniques
	 * 
	 * @param o - Object of class T - class of an instance of type T
	 * @return a TableView
	 */
	public TableView<T> genTable(Object o) {
		TableView<T> table = new TableView<T>();
		
		for(Field field: o.getClass().getDeclaredFields()) {
			TableColumn<T, ?> col = new TableColumn<>(field.getName());
			col.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
			table.getColumns().add(col);
		}
		
		return table;
	}
	
}
