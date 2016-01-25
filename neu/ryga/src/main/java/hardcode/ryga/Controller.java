package hardcode.ryga;

import hardcode.ryga.ui.graph.CellType;
import hardcode.ryga.ui.graph.Graph;
import hardcode.ryga.ui.graph.Model;
import hardcode.ryga.ui.layout.base.Layout;
import hardcode.ryga.ui.layout.random.RandomLayout;
import hardcode.vortaro.db.FakeDatabase;
import hardcode.vortaro.gui.GuiController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class Controller {

	private Graph graph = new Graph();

	private GuiController controller;

	public Controller(Stage stage) {
		ui(stage);
	}

	private void ui(Stage stage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			BorderPane root = fxmlLoader.load(getClass().getResource("App.fxml").openStream());

			controller = (GuiController) fxmlLoader.getController();
			
			FakeDatabase database = new FakeDatabase(null);
			
			controller.init(database);

			Scene scene = new Scene(root, 700, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

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
				"plocal:/home/michael/Temp/orient/db"); // TODO

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
