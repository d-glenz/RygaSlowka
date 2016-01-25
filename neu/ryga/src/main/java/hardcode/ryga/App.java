package hardcode.ryga;

import java.io.File;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {

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
			Vertex luca = graph.addVertex(null); // 1st OPERATION: IMPLICITLY BEGIN A TRANSACTION
			luca.setProperty("name", "Luca");
			Vertex marko = graph.addVertex(null);
			marko.setProperty("name", "Marko");
			Edge lucaKnowsMarko = graph.addEdge("SomeName", luca, marko, "knows");
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
