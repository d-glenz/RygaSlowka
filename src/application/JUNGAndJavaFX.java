package application;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;

/**
 * A sample showing how to use JUNG's layout classes to position vertices in a
 * graph.
 * 
 * @author jeffreyguenther
 * @author timheng
 */
public class JUNGAndJavaFX extends Application {

	private static final int CIRCLE_SIZE = 15; // default circle size

	private Graph<Circle, Line> graph1;
	Layout<String, Number> circleLayout;
	Group viz1;
	
	@Override
	public void start(Stage stage) {
		BorderPane rootLayout = null;

		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.setLocation(getClass().getResource("/RootLayout.fxml"));
			rootLayout = (BorderPane) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(rootLayout, 800, 400, Color.WHITE);
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();

		// create a group for the visualization
		viz1 = new Group();

		circleLayout = doJungStuff(LayoutType.ISOM);

		// draw the graph

		drawGraph(graph1, viz1);

		rootLayout.getChildren().add(viz1);

		stage.setTitle("Displaying One JUNG Graph");
		stage.setScene(scene);
		stage.show();

	}

	enum LayoutType {
		FR, ISOM, KK, CIRCLE, SPRING
	}

	private Layout<String, Number> doJungStuff(LayoutType lt) {
		// create a sample graph using JUNG's TestGraphs class.

		Graph<String, Number> temp = TestGraphs.getOneComponentGraph();

		// define the layout we want to use for the graph
		// The layout will be modified by the VisualizationModel
		Layout<String, Number> layout;

		switch (lt) {
		case FR:
			layout = new FRLayout<String, Number>(temp);
			break;
		case ISOM:
			layout = new ISOMLayout<String, Number>(temp);
			break;
		case KK:
			layout = new KKLayout<String, Number>(temp);
			break;
		case SPRING:
			layout = new SpringLayout<String, Number>(temp);
			break;
		default:
			layout = new CircleLayout<String, Number>(temp);
			break;
		}

		this.graph1 = convertGraph(TestGraphs.getOneComponentGraph(), layout);

		// = new AggregateLayout<String, Number>(
		// graph1);

		// Define the visualization model. This is how JUNG calculates the
		// layout for the graph. It updates the layout object passed in.
		new DefaultVisualizationModel<>(layout, new Dimension(400, 400));

		return layout;
	}

	/**
	 * Render a graph to a particular Group
	 * 
	 * This is purely JAVAFX Stuff
	 * 
	 * @param graph12
	 * @param circleLayout2
	 * @param viz
	 */
	public void drawGraph(Graph<Circle, Line> graph, Group group) {
		for (Circle c : graph.getVertices()) {
			if (!group.getChildren().contains(c)) {
				group.getChildren().add(c);
			}

		}

		for (Line l : graph.getEdges()) {
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
	public UndirectedSparseMultigraph<Circle, Line> convertGraph(
			Graph<String, Number> graph, Layout<String, Number> layout) {
		// look up the circle object used for the string vertex
		Map<String, Circle> toCircle = new HashMap<>();

		// look up the line object used for the number edge
		Map<Number, Line> toLine = new HashMap<>();

		// new graph with JavaFX objects in it.
		UndirectedSparseMultigraph<Circle, Line> undirectGraph = new UndirectedSparseMultigraph<>();

		// Do the layout to get coords for the circles.
		@SuppressWarnings("unused")
		VisualizationModel<String, Number> vm1 = new DefaultVisualizationModel<>(
				layout, new Dimension(400, 400));

		// Draw the edges from graph 1
		for (Number n : graph.getEdges()) {
			// get the endpoint verts
			Pair<String> endpoints = graph.getEndpoints(n);

			// create the circles
			Circle first = null;
			Circle second = null;

			if (!toCircle.containsKey(endpoints.getFirst())) {
				first = createCircleVertex(layout, endpoints.getFirst());
				toCircle.put(endpoints.getFirst(), first);
			} else {
				first = toCircle.get(endpoints.getFirst());
			}

			if (!toCircle.containsKey(endpoints.getSecond())) {
				second = createCircleVertex(layout, endpoints.getSecond());
				toCircle.put(endpoints.getSecond(), second);
			} else {
				second = toCircle.get(endpoints.getSecond());
			}

			Line line = new Line();
			line.setStroke(Color.BLACK);
			line.startXProperty().bind(first.centerXProperty());
			line.startYProperty().bind(first.centerYProperty());
			line.endXProperty().bind(second.centerXProperty());
			line.endYProperty().bind(second.centerYProperty());

			toLine.put(n, line);

			undirectGraph.addEdge(line, first, second);
		}

		return undirectGraph;

	}

	public void testStacks() {
		Circle circle = createCircleVertex(circleLayout, "");
		Text text = new Text("42");
		text.setBoundsType(TextBoundsType.VISUAL);
		StackPane stack = new StackPane();
		stack.getChildren().addAll(circle, text);

		// Circle c = (Circle) stack.getChildren().get(0);
		// Text t=(Text)stack.getChildren().get(1);
	}

	/**
	 * Draws the circles and attaches eventhandler for the circles.
	 * 
	 * @param layout
	 *            to be drawn
	 * @param vert
	 *            is String to be converted into Circle
	 * @return the Circle
	 */
	public Circle createCircleVertex(Layout<String, Number> layout, String vert) {
		// get the xy of the vertex.
		Point2D p = layout.transform(vert);
		Circle circle = new Circle();
		circle.setCenterX(p.getX());
		circle.setCenterY(p.getY());
		circle.setRadius(CIRCLE_SIZE);
		circle.setFill(Color.BLACK);

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
				c.setFill(Color.BLACK);
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

	/**
	 * Fallback to start FX application 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
