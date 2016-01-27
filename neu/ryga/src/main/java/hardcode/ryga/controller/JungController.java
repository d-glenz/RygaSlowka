package hardcode.ryga.controller;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

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
      rc.getPane().getChildren()
          .add(new Line(start.getX(), start.getY(), end.getX(), end.getY()));
    }
  }

  /**
   * Note: Mouse movement in current setup not possible.
   * 
   * @author Dominik Glenz
   *
   */
  private final class VertexToNodeTransformer implements
      Transformer<String, Node> {
    public Node transform(String input) {
      Circle rect = new Circle(0, 0, 5);
      Text text = new Text(0, 15, input);
      Group group = new Group();

      group.getChildren().add(rect);
      group.getChildren().add(text);

      return group;
    }
  }

  public JungController(Graph<String, Integer> graph, Dimension dimension) {
    super(new FRLayout<>(graph, dimension));
    getFXRenderContext()
        .setVertexNodeTransformer(new VertexToNodeTransformer());
    getFXRenderer().setEdgeRenderer(new EdgeRenderer());
  }
}
