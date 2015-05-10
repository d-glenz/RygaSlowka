package application;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;

public class GraphUtil {
	/**
	 * Render a graph to a particular Group
	 * 
	 * This is purely JAVAFX Stuff
	 * 
	 * @param graph12
	 * @param circleLayout2
	 * @param viz
	 */
	public static <S extends Node> void drawGraph(Graph<S, Line> graph12,
			Group group) {
		for (S c : graph12.getVertices()) {
			if (!group.getChildren().contains(c)) {
				group.getChildren().add(c);
			}

		}

		for (Line l : graph12.getEdges()) {
			if (!group.getChildren().contains(l)) {
				group.getChildren().add(l);
			}

		}
	}

	/**
	 * Converts String and Number graph to Circle and Line graph.
	 * 
	 * @param graph
	 *            to layout
	 * @param layout
	 *            to position nodes
	 * @return Circle, Line graph with the given layout
	 * 
	 * @author
	 */
	public static <S extends Node> UndirectedSparseMultigraph<S, Line> convertGraph(
			Graph<String, Number> graph, Layout<String, Number> layout,
			ShapeBuilder<S> s) {
		// look up the circle object used for the string vertex
		Map<String, S> toCircle = new HashMap<>();

		// look up the line object used for the number edge
		Map<Number, Line> toLine = new HashMap<>();

		// new graph with JavaFX objects in it.
		UndirectedSparseMultigraph<S, Line> undirectGraph = new UndirectedSparseMultigraph<>();

		// Do the layout to get coords for the circles.

		@SuppressWarnings("unused")
		VisualizationModel<String, Number> vm1 = new DefaultVisualizationModel<>(
				layout, new Dimension(400, 400));

		// Draw the edges from graph 1
		for (Number n : graph.getEdges()) {
			// get the endpoint verts
			Pair<String> endpoints = graph.getEndpoints(n);

			// create the circles
			S first = null;
			S second = null;

			if (!toCircle.containsKey(endpoints.getFirst())) {
				first = s.create(layout, endpoints.getFirst());
				toCircle.put(endpoints.getFirst(), first);
			} else {
				first = toCircle.get(endpoints.getFirst());
			}

			if (!toCircle.containsKey(endpoints.getSecond())) {
				second = s.create(layout, endpoints.getSecond());
				toCircle.put(endpoints.getSecond(), second);
			} else {
				second = toCircle.get(endpoints.getSecond());
			}

			Line line = new Line();
			line.setStroke(Color.BLACK);
			line.startXProperty().bind(((Circle) first).centerXProperty());
			line.startYProperty().bind(((Circle) first).centerYProperty());
			line.endXProperty().bind(((Circle) second).centerXProperty());
			line.endYProperty().bind(((Circle) second).centerYProperty());

			toLine.put(n, line);

			undirectGraph.addEdge(line, first, second);
		}

		return undirectGraph;

	}
}
