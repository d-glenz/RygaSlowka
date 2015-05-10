package application;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;

/**
 * A sample showing how to use JUNG's layout classes to position vertices in a
 * graph.
 * 
 * @author jeffreyguenther
 * @author timheng
 */
public class JUNGAndJavaFX extends Application {

	private static final int CIRCLE_SIZE = 15; // default circle size

	private Graph<String, Number> graph1;
	Layout<String, Number> circleLayout;
	Group viz1;
	
	@Override
	public void start(Stage stage) {
		BorderPane rootLayout = null;

		MyController controller = new MyController();
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setController(controller);
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

		renderGraph(graph1, circleLayout, viz1);

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
		this.graph1 = TestGraphs.getOneComponentGraph();

		// define the layout we want to use for the graph
		// The layout will be modified by the VisualizationModel
		Layout<String, Number> layout;

		switch (lt) {
		case FR:
			layout = new FRLayout<String, Number>(graph1);
			break;
		case ISOM:
			layout = new ISOMLayout<String, Number>(graph1);
			break;
		case KK:
			layout = new KKLayout<String, Number>(graph1);
			break;
		case SPRING:
			layout = new SpringLayout<String, Number>(graph1);
			break;
		default:
			layout = new CircleLayout<String, Number>(graph1);
			break;
		}

		// = new AggregateLayout<String, Number>(
		// graph1);

		// Define the visualization model. This is how JUNG calculates the
		// layout for the graph. It updates the layout object passed in.
		new DefaultVisualizationModel<>(layout, new Dimension(400, 400));

		return layout;
	}
	
	private double dragDeltaX, dragDeltaY; 

	/**
	 * Render a graph to a particular Group
	 * 
	 * This is purely JAVAFX Stuff
	 * 
	 * @param graph
	 * @param layout
	 * @param viz
	 */
	private void renderGraph(Graph<String, Number> graph,
			Layout<String, Number> layout, Group viz) {
		// draw the vertices in the graph
		for (String v : graph.getVertices()) {
			// Get the position of the vertex
			Point2D p = (Point2D) layout.transform(v);

			// draw the vertex as a circle
			final Circle circle = new Circle();
			circle.setCenterX(p.getX());
			circle.setCenterY(p.getY());
			circle.setRadius(CIRCLE_SIZE);
			
//			final Polygon poly = new Polygon( 10, 10, 100, 10, 200, 100, 50, 200 );
//			final AtomicInteger polyCoordinateIndex = new AtomicInteger( 0);
//			circle.centerXProperty().addListener( new ChangeListener<Number>() {
//		        @Override
//		        public void changed( ObservableValue<? extends Number> observable, Number oldValue, Number newValue ) {
//		          poly.getPoints().set( polyCoordinateIndex.get(), newValue.doubleValue() );
//		        }
//		      } );
//		      circle.centerYProperty().addListener( new ChangeListener<Number>() {
//		        @Override
//		        public void changed( ObservableValue<? extends Number> observable, Number oldValue, Number newValue ) {
//		          poly.getPoints().set( polyCoordinateIndex.get() + 1, (Double) newValue );
//		        }
//		      } );
//			
//		      circle.setOnMousePressed( new EventHandler<MouseEvent>() {
//		          @Override public void handle( MouseEvent mouseEvent ) {
//		            dragDeltaX = circle.getCenterX() - mouseEvent.getSceneX();
//		            dragDeltaY = circle.getCenterY() - mouseEvent.getSceneY();
//		          }
//		        } );
//
//		        circle.setOnMouseDragged( new EventHandler<MouseEvent>() {
//		          @Override public void handle( MouseEvent mouseEvent ) {
//		            circle.setCenterX( mouseEvent.getSceneX() + dragDeltaX );
//		            circle.setCenterY( mouseEvent.getSceneY() + dragDeltaY );
//		            renderGraph(graph1,circleLayout,viz1);
//		            circle.setCursor( Cursor.MOVE );
//		          }
//		        } );
//
//		        circle.setOnMouseEntered( new EventHandler<MouseEvent>() {
//		          @Override public void handle( MouseEvent mouseEvent ) {
//		            circle.setCursor( Cursor.HAND );
//		          }
//		        } );
//
//		        circle.setOnMouseReleased( new EventHandler<MouseEvent>() {
//		          @Override public void handle( MouseEvent mouseEvent ) {
//		            circle.setCursor( Cursor.HAND );
//		          }
//		        } );
		      
			// add it to the group, so it is shown on screen
			viz.getChildren().add(circle);
		}

		// draw the edges
		for (Number n : graph.getEdges()) {
			// get the end points of the edge
			Pair<String> endpoints = graph.getEndpoints(n);

			// Get the end points as Point2D objects so we can use them in the
			// builder
			Point2D pStart = (Point2D) layout.transform(endpoints.getFirst());
			Point2D pEnd = (Point2D) layout.transform(endpoints.getSecond());

			// Draw the line
			Line line = new Line();
			line.setStartX(pStart.getX());
			line.setStartY(pStart.getY());
			line.setEndX(pEnd.getX());
			line.setEndY(pEnd.getY());
			// add the edges to the screen
			viz.getChildren().add(line);
		}
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