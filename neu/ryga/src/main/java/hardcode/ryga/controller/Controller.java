package hardcode.ryga.controller;

import hardcode.ryga.model.db.FakeDatabase;
import hardcode.ryga.view.gui.GuiController;
import hardcode.ryga.view.ui.graph.CellType;
import hardcode.ryga.view.ui.graph.Graph;
import hardcode.ryga.view.ui.graph.Model;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.collections15.Transformer;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.visulytic.jungfx.visualization.FXEdgeRenderer;
import com.visulytic.jungfx.visualization.FXVisualizationViewer;
import com.visulytic.jungfx.visualization.IFXRenderContext;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
//import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class Controller {

	private Graph graph = new Graph();

	private GuiController guiController;

	public Controller(Stage stage) {
		ui(stage);
	}

	private void ui(Stage stage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			BorderPane root = fxmlLoader.load(getClass().getResource("/App.fxml").openStream());

			guiController = (GuiController) fxmlLoader.getController();
			
			FakeDatabase database = new FakeDatabase(null);
			
			guiController.init(database);
			
			FXVisualizationViewer jung = jung();
			
			guiController.insertGraph(jung);

			Scene scene = new Scene(root, 700, 400);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

			stage.setScene(scene);
			stage.setTitle(S.APP_NAME);
			stage.show();

			
//			graph = new Graph();
//			root.setCenter(graph.getScrollPane());
//			stage.setScene(scene); stage.show();
//			addGraphComponents();
//			Layout layout = new RandomLayout(graph); layout.execute();
			 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	private FXVisualizationViewer jung() {
	    // Graph<String, Number> g = TestGraphs.getOneComponentGraph();
	    //
	    // Graph<TextShape, Line> dataGraph = new SparseGraph<TextShape,
	    // Line>();
	    //
	    // Layout<String, Number> layout = new FRLayout<>(g);
	    //
	    // Window w = new Window(stage);
	    //
	    // new DefaultVisualizationModel<>(layout, w.getStageSize());
	    //
	    // dataGraph = convert(g, layout);
	    //
//	    RygaGraph<TextShape> graph = new RygaGraph<TextShape>(dataGraph,
//	        LayoutType.FR);
	    //
	    // graph.draw(w.getGroup());

		edu.uci.ics.jung.graph.Graph<String, Integer> g = new DirectedSparseGraph<String, Integer>();
	    g.addVertex("A");
	    g.addVertex("B");
	    g.addEdge(1, "A", "B");

	    FRLayout<String, Integer> layout = new FRLayout<>(g);
	    layout.setSize(new Dimension(300, 300));
	    FXVisualizationViewer<String, Integer> root = new FXVisualizationViewer<>(
	        layout);

	    root.getFXRenderContext().setVertexNodeTransformer(
	        new Transformer<String, Node>() {

	          @Override
	          public Node transform(String input) {
	            Circle rect = new Circle(0, 0, 5);
	            rect.setOnMouseDragged(new EventHandler<MouseEvent>() {
	              public void handle(MouseEvent event) {
	                rect.setCenterX(event.getX());
	                rect.setCenterY(event.getY());
	              }
	            });
	            return rect;
	          }
	        });

	    root.getFXRenderer().setEdgeRenderer(new FXEdgeRenderer<String, Integer>() {
	      public void paintEdge(IFXRenderContext<String, Integer> rc,
	          Layout<String, Integer> layout, Integer e) {
	        Pane pane = rc.getPane();
	        edu.uci.ics.jung.graph.Graph<String, Integer> graph = layout.getGraph();
	        Point2D start = layout.transform(graph.getSource(e));
	        Point2D end = layout.transform(graph.getDest(e));
	        Line l = new Line(start.getX(), start.getY(), end.getX(), end.getY());

	        //l.startXProperty().bind(); //TODO //FIXME
	        pane.getChildren().add(l);
	      }
	    });

	    root.renderGraph();
	    
	    Scene scene = new Scene(root, 800, 800);
	    //stage.setScene(scene);
	    //stage.show();
	    
	    return root;
	}
	

  private void addGraphComponents() {

		Model model = graph.getModel();

		graph.beginUpdate();

		model.addCell("Cell A", CellType.RECTANGLE);
		model.addCell("Cell B", CellType.RECTANGLE);
		model.addCell("Cell C", CellType.RECTANGLE);
		model.addCell("Cell D", CellType.TRIANGLE);
		model.addCell("Cell E", CellType.TRIANGLE);
		model.addCell("Cell F", CellType.RECTANGLE);
		model.addCell("Cell G", CellType.RECTANGLE);

		model.addEdge("Cell A", "Cell B");
		model.addEdge("Cell A", "Cell C");
		model.addEdge("Cell B", "Cell C");
		model.addEdge("Cell C", "Cell D");
		model.addEdge("Cell B", "Cell E");
		model.addEdge("Cell D", "Cell F");
		model.addEdge("Cell D", "Cell G");

		graph.endUpdate();
	}

  private void initOrientDB() throws Exception {

		OServer server = OServerMain.create();

		File orientConfigFile = new File("orient-config.xml");
		System.out.println(orientConfigFile.exists());

		server.startup(orientConfigFile);

		server.activate();

		OrientGraph graph = new OrientGraph(
				"plocal:/tmp/orient/db"); // TODO

		// use factory instead
		// OrientGraphFactory factory = new
		// OrientGraphFactory("plocal:C:/temp/graph/db").setupPool(1,10);

		try {
			Vertex luca = graph.addVertex(null); // 1st OPERATION: IMPLICITLY
													// BEGIN A TRANSACTION
			luca.setProperty("name", "Luca");
			Vertex marko = graph.addVertex(null);
			marko.setProperty("name", "Marko");
			Edge lucaKnowsMarko = graph.addEdge("SomeName", luca, marko,
					"knows");
			graph.commit();

			for (Vertex v : graph.getVertices()) {
				System.out.println(v.getProperty("name"));
			}

			for (Edge e : graph.getEdges()) {
				System.out.println(e.getProperty("name"));
			}
		} catch (Exception e) {
			graph.rollback();
		} finally {
			graph.shutdown();
		}

		/*
		 * 
		 * OrientGraph graph = new OrientGraph("local:test", "username",
		 * "password");
		 * 
		 * 
		 * 
		 * 
		 * try{ Vertex luca = graph.addVertex(null); // 1st OPERATION:
		 * IMPLICITLY BEGIN A TRANSACTION luca.setProperty( "name", "Luca" );
		 * Vertex marko = graph.addVertex(null); marko.setProperty( "name",
		 * "Marko" ); Edge lucaKnowsMarko = graph.addEdge(null, luca, marko,
		 * "knows"); graph.commit(); } catch( Exception e ) { graph.rollback();
		 * }
		 */
	}
}
