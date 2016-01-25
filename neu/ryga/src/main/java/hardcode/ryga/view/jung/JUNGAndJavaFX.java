package hardcode.ryga.view.jung;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import org.apache.commons.collections15.Transformer;

import com.visulytic.jungfx.visualization.FXEdgeRenderer;
import com.visulytic.jungfx.visualization.FXVisualizationViewer;
import com.visulytic.jungfx.visualization.IFXRenderContext;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * A sample showing how to use JUNG's layout classes to position vertices in a graph.
 * 
 * @author jeffreyguenther
 * @author timheng
 */
public class JUNGAndJavaFX extends Application {

  @Override
  public void start(Stage stage) {
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
//    RygaGraph<TextShape> graph = new RygaGraph<TextShape>(dataGraph,
//        LayoutType.FR);
    //
    // graph.draw(w.getGroup());

    Graph<String, Integer> g = new DirectedSparseGraph<String, Integer>();
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
        Graph<String, Integer> graph = layout.getGraph();
        Point2D start = layout.transform(graph.getSource(e));
        Point2D end = layout.transform(graph.getDest(e));
        Line l = new Line(start.getX(), start.getY(), end.getX(), end.getY());

        //l.startXProperty().bind(); //TODO //FIXME
        pane.getChildren().add(l);
      }
    });

    root.renderGraph();
    Scene scene = new Scene(root, 800, 800);

    stage.setScene(scene);
    stage.show();
  }

  @SuppressWarnings({ "unused" })
  private Graph<TextShape, Line> convert(Graph<String, Number> g,
      Layout<String, Number> layout) {
    Map<String, TextShape> toCircle = new HashMap<>();
    Graph<TextShape, Line> dataGraph = new SparseGraph<TextShape, Line>();

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

      dataGraph.addEdge(RygaGraph.createLine(first, second), first, second);

    }
    return dataGraph;
  }

  /**
   * Fallback to start FX application
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
