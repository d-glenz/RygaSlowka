package hardcode.ryga.controller;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import org.apache.commons.collections15.Transformer;

import com.visulytic.jungfx.visualization.FXEdgeRenderer;
import com.visulytic.jungfx.visualization.FXVisualizationViewer;
import com.visulytic.jungfx.visualization.IFXRenderContext;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;

public class JungController extends FXVisualizationViewer<String, Integer> {

  private final class EdgeRenderer extends FXEdgeRenderer<String, Integer> {
    public void paintEdge(IFXRenderContext<String, Integer> rc,
        Layout<String, Integer> layout, Integer e) {
      Graph<String, Integer> graph = layout.getGraph();
      Point2D start = layout.transform(graph.getSource(e));
      Point2D end = layout.transform(graph.getDest(e));
      Line l = new Line(start.getX(), start.getY(), end.getX(), end.getY());
      // l.startXProperty().bind(); //TODO //FIXME
      rc.getPane().getChildren().add(l);
    }
  }

  private final class VertexToNodeTransformer implements
      Transformer<String, Node> {
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
  }

  public JungController(Graph<String, Integer> graph, Dimension dimension) {
    super(new FRLayout<>(graph, dimension));
    getFXRenderContext()
        .setVertexNodeTransformer(new VertexToNodeTransformer());
    getFXRenderer().setEdgeRenderer(new EdgeRenderer());
  }
}
