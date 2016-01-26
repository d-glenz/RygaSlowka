package hardcode.ryga.controller;

import java.awt.geom.Point2D;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import org.apache.commons.collections15.Transformer;

import com.visulytic.jungfx.visualization.FXEdgeRenderer;
import com.visulytic.jungfx.visualization.FXVisualizationViewer;
import com.visulytic.jungfx.visualization.IFXRenderContext;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;

public class JungController extends FXVisualizationViewer {

  @SuppressWarnings("unchecked")
  public JungController(FRLayout layout) {
    super(layout);
    
    getFXRenderContext().setVertexNodeTransformer(
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

    getFXRenderer().setEdgeRenderer(new FXEdgeRenderer<String, Integer>() {
      public void paintEdge(IFXRenderContext<String, Integer> rc,
          Layout<String, Integer> layout, Integer e) {
        Pane pane = rc.getPane();
        edu.uci.ics.jung.graph.Graph<String, Integer> graph = layout.getGraph();
        Point2D start = layout.transform(graph.getSource(e));
        Point2D end = layout.transform(graph.getDest(e));
        Line l = new Line(start.getX(), start.getY(), end.getX(), end.getY());

        // l.startXProperty().bind(); //TODO //FIXME
        pane.getChildren().add(l);
      }
    });
  }

}
