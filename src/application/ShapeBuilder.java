package application;

import javafx.scene.Node;
import edu.uci.ics.jung.algorithms.layout.Layout;

public interface ShapeBuilder<S extends Node> {

	public abstract S create(Layout<String, Number> layout, String vert);

}