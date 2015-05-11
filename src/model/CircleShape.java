package model;

import java.awt.geom.Point2D;

import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import edu.uci.ics.jung.algorithms.layout.Layout;

public class CircleShape extends MyShape<Circle> implements
		EventHandler<MouseEvent> {

	Circle circle;

	private final static int CIRCLE_SIZE = 15;

	public CircleShape(Point2D p) {
		super(p);
		this.circle = new Circle(p.getX(), p.getY(), CIRCLE_SIZE, Color.WHITE);
		this.circle.setStrokeType(StrokeType.OUTSIDE);
		this.circle.setStroke(Color.BLACK);
		this.circle.setStrokeWidth(2);
		this.circle.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		this.circle.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
		this.circle.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
	}

	@Override
	public DoubleProperty getXProperty() {
		return circle.centerXProperty();
	}

	@Override
	public DoubleProperty getYProperty() {
		return circle.centerYProperty();
	}

	public static CircleShape createShape(Layout<String, Number> layout,
			String first) {
		Point2D p = layout.transform(first);
		return new CircleShape(p);
	}

	public Node[] getNode() {
		return new Node[]{circle};
	}

	@Override
	public void handle(MouseEvent arg0) {
		if (arg0.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
			Circle c = (Circle) arg0.getSource();
			c.setFill(Color.ROSYBROWN);
			arg0.consume();
		} else if (arg0.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
			Circle c = (Circle) arg0.getSource();
			c.setFill(Color.WHITE);
			arg0.consume();
		} else if (arg0.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
			Circle c = (Circle) arg0.getSource();
			c.setCenterX(arg0.getX());
			c.setCenterY(arg0.getY());
			arg0.consume();
		}
	}

}
