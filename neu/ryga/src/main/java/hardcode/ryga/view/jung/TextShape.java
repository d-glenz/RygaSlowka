package hardcode.ryga.view.jung;

import java.awt.geom.Point2D;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import edu.uci.ics.jung.algorithms.layout.Layout;

public class TextShape extends MyShape<Text> implements
		EventHandler<MouseEvent> {

	Text text;
	Rectangle redBorder;

	public TextShape(Point2D p) {
		super(p);
		this.text = new Text(p.getX(), p.getY(), "test");
		this.text.setFont(Font.font("Arial", 25));
		this.text.setFill(Color.BLACK);
		redBorder = new Rectangle(0, 0, Color.TRANSPARENT);
		redBorder.setStroke(Color.RED);
		redBorder.setManaged(false);
		redBorder.setLayoutX(text.getBoundsInParent().getMinX());
		redBorder.setLayoutY(text.getBoundsInParent().getMinY());
		redBorder.setWidth(text.getBoundsInParent().getWidth());
		redBorder.setHeight(text.getBoundsInParent().getHeight());
		this.text.boundsInParentProperty().addListener(
				new ChangeListener<Bounds>() {
					@Override
					public void changed(ObservableValue<? extends Bounds> arg0,
							Bounds arg1, Bounds arg2) {
						redBorder
								.setLayoutX(text.getBoundsInParent().getMinX());
						redBorder
								.setLayoutY(text.getBoundsInParent().getMinY());
						redBorder.setWidth(text.getBoundsInParent().getWidth());
						redBorder.setHeight(text.getBoundsInParent()
								.getHeight());
					}
				});
		this.redBorder.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		this.redBorder.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
		this.redBorder.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
	}

	@Override
	public DoubleProperty getXProperty() {
		return text.xProperty();
	}

	@Override
	public DoubleProperty getYProperty() {
		return text.yProperty();
	}

	public static TextShape createShape(Layout<String, Number> layout,
			String first) {
		Point2D p = layout.transform(first);
		return new TextShape(p);
	}

	public Node[] getNode() {
		return new Node[] { text, redBorder };
	}

	@Override
	@SuppressWarnings("unused")
	public void handle(MouseEvent arg0) {
		if (arg0.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
			Rectangle c = (Rectangle) arg0.getSource();
			arg0.consume();
		} else if (arg0.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
			Rectangle c = (Rectangle) arg0.getSource();
			arg0.consume();
		} else if (arg0.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
			if (arg0.getSource() instanceof Rectangle) {
				text.setX(arg0.getSceneX());
				text.setY(arg0.getSceneY());
				System.out.println(arg0.getSceneX() + ":" + arg0.getSceneY());
				arg0.consume();
			}
		}
	}

}
