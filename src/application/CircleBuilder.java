package application;

import java.awt.geom.Point2D;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import edu.uci.ics.jung.algorithms.layout.Layout;

public class CircleBuilder implements ShapeBuilder<Circle> {
	
	private static final int CIRCLE_SIZE = 15; // default circle size
	/**
	 * Draws the circles and attaches eventhandler for the circles.
	 * 
	 * @param layout
	 *            to be drawn
	 * @param vert
	 *            is String to be converted into Circle
	 * @return the Circle
	 */
	public Circle create(Layout<String, Number> layout, String vert) {
		// get the xy of the vertex.
		Point2D p = layout.transform(vert);
		Circle circle = new Circle(p.getX(), p.getY(), CIRCLE_SIZE, Color.WHITE);
		circle.setStrokeType(StrokeType.OUTSIDE);
		circle.setStroke(Color.BLACK);
		circle.setStrokeWidth(2);

		circle.setOnMouseClicked(e -> System.out.println(e.getSource()));

		circle.setOnMousePressed(new EventHandler<MouseEvent>() {
			// Change the color to Rosybrown when pressed.
			@Override
			public void handle(MouseEvent t) {
				Circle c = (Circle) t.getSource();
				c.setFill(Color.ROSYBROWN);
				t.consume();
			}
		});

		circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
			// Change it back to default color when released.
			@Override
			public void handle(MouseEvent t) {
				Circle c = (Circle) t.getSource();
				c.setFill(Color.WHITE);
				t.consume();
			}
		});

		circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
			// Update the circle's position when it's dragged.
			@Override
			public void handle(MouseEvent t) {
				Circle c = (Circle) t.getSource();
				c.setCenterX(t.getX());
				c.setCenterY(t.getY());
				t.consume();
			}
		});
		return circle;
	}
}
