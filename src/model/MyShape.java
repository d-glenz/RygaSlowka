package model;

import java.awt.geom.Point2D;

import edu.uci.ics.jung.algorithms.layout.Layout;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

public abstract class MyShape<S extends Shape> {

	S shape;

	public MyShape(Point2D p) {
		super();
	}

	public Node getNode() {
		return shape;
	}

	public S getShape() {
		return shape;
	}

	public abstract DoubleProperty getXProperty();

	public abstract DoubleProperty getYProperty();

	public static MyShape<?> createShape(Layout<String, Number> layout,
			String first) {
		return null;
	}

}
