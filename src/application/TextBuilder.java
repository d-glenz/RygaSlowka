package application;

import java.awt.geom.Point2D;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import edu.uci.ics.jung.algorithms.layout.Layout;

public class TextBuilder implements ShapeBuilder<Text> {

	private static final int TEXT_SIZE = 25; // default circle size

	/**
	 * Draws the circles and attaches eventhandler for the circles.
	 * 
	 * @param layout
	 *            to be drawn
	 * @param vert
	 *            is String to be converted into Circle
	 * @return the Circle
	 */
	public Text create(Layout<String, Number> layout, String vert) {
		// get the xy of the vertex.
		Point2D p = layout.transform(vert);
		Text text = new Text();
		text.setFont(Font.font("Arial", FontWeight.NORMAL, TEXT_SIZE));
		text.setX(p.getX());
		text.setY(p.getY());
		text.setFill(Color.BLACK);

		text.setOnMouseClicked(e -> System.out.println(e.getSource()));

		text.setOnMousePressed(new EventHandler<MouseEvent>() {
			// Change the color to Rosybrown when pressed.
			@Override
			public void handle(MouseEvent t) {
				Text c = (Text) t.getSource();
				c.setFill(Color.ROSYBROWN);
				t.consume();
			}
		});

		text.setOnMouseReleased(new EventHandler<MouseEvent>() {
			// Change it back to default color when released.
			@Override
			public void handle(MouseEvent t) {
				Text c = (Text) t.getSource();
				c.setFill(Color.WHITE);
				t.consume();
			}
		});

		text.setOnMouseDragged(new EventHandler<MouseEvent>() {
			// Update the circle's position when it's dragged.
			@Override
			public void handle(MouseEvent t) {
				Text c = (Text) t.getSource();
				c.setX(t.getX());
				c.setY(t.getY());
				t.consume();
			}
		});
		return text;
	}

}
