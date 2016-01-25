package hardcode.ryga.view.jung;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@SuppressWarnings("unused")
public class ContextPaneController implements QueryResultInjectee {
	
	private enum Mode {
		SEARCH;
	}
	
  private TextField text;
	private VBox box;
	
	private Mode mode;
	
	private ContextPaneQueryChanged queryChangedListener;
	
	public ContextPaneController (TextField text, VBox box) {
		this.text = text;
		this.box = box;
		this.mode = Mode.SEARCH;
		
		text.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				textValueChanged(oldValue, newValue);
			}
		});
		
		box.setVisible(false);
	}
	
	private void textValueChanged(String oldVal, String newVal) {
		System.out.println(oldVal + " -> " + newVal);
		
		queryChangedListener.queryChanged(oldVal, newVal, this);
		
		
		
		if(newVal.isEmpty()) {
			box.setVisible(false);
		} else {
			box.setVisible(true);
		}
	}
	
	public void setQueryChangedListener(ContextPaneQueryChanged listener) {
		this.queryChangedListener = listener;
	}
	
	
	
}
