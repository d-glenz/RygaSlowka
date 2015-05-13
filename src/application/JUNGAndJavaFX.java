package application;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.TextShape;
import view.Window;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import graph.RygaGraph;
import graph.RygaGraph.LayoutType;

/**
 * A sample showing how to use JUNG's layout classes to position vertices in a
 * graph.
 * 
 * @author jeffreyguenther
 * @author timheng
 */
public class JUNGAndJavaFX extends Application {

	@Override
	public void start(Stage stage) {
		Graph<String, Number> g = TestGraphs.getOneComponentGraph();
		Graph<TextShape, Line> dataGraph = new SparseGraph<TextShape, Line>();
		Layout<String, Number> layout = new FRLayout<>(g);
		Window w = new Window(stage);
		new DefaultVisualizationModel<>(layout, w.getStageSize());
		dataGraph = convert(g, layout);
		RygaGraph<TextShape> graph = new RygaGraph<TextShape>(dataGraph, LayoutType.FR);
		graph.draw(w.getGroup());
	}

	private Graph<TextShape, Line> convert(Graph<String, Number> g,
			Layout<String, Number> layout) {
		Map<String, TextShape> toCircle = new HashMap<>();
		Graph<TextShape, Line> dataGraph = new SparseGraph<>();

		for (Number n : g.getEdges()) {
			Pair<String> endpoints = g.getEndpoints(n);

			TextShape first = null;
			TextShape second = null;

			if (!toCircle.containsKey(endpoints.getFirst())) {
				first = TextShape.createShape(layout, endpoints.getFirst());
				toCircle.put(endpoints.getFirst(), first);
			} else {
				first = toCircle.get(endpoints.getFirst());
			}

			if (!toCircle.containsKey(endpoints.getSecond())) {
				second = TextShape.createShape(layout, endpoints.getSecond());
				toCircle.put(endpoints.getSecond(), second);
			} else {
				second = toCircle.get(endpoints.getSecond());
			}

			dataGraph.addEdge(RygaGraph.createLine(first, second), first,
					second);

		}
		return dataGraph;
	}

	/**
	 * Fallback to start FX application
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
