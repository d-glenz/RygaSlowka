package hardcode.ryga.view.jung;

import java.awt.Dimension;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;

public class RygaGraph<S extends MyShape<?>> {

  Graph<String, Number> dataGraph;
  Graph<S, Line> displayGraph;
  Layout<String, Number> layout;

  public enum LayoutType {
    FR, ISOM, KK, CIRCLE, SPRING
  }

  public RygaGraph(Graph<S, Line> dataGraph, LayoutType lt) {
    super();
    this.layout = newLayout(lt, this.dataGraph);
    this.displayGraph = dataGraph;
  }

  public static Layout<String, Number> newLayout(LayoutType lt,
      Graph<String, Number> graph) {
    Layout<String, Number> layout = null;
    switch (lt) {
    case CIRCLE:
      layout = new CircleLayout<>(graph);
      break;
    case FR:
      layout = new FRLayout<>(graph);
      break;
    case ISOM:
      layout = new ISOMLayout<>(graph);
      break;
    case KK:
      layout = new KKLayout<>(graph);
      break;
    case SPRING:
      layout = new SpringLayout<>(graph);
      break;
    }
    return layout;
  }

  @SuppressWarnings("unused")
  public Graph<MyShape<?>, Line> convert(Graph<String, Number> dataGraph2) {
    Graph<MyShape<?>, Line> result = new SparseGraph<MyShape<?>, Line>();

    VisualizationModel<String, Number> vm = new DefaultVisualizationModel<>(
        layout, new Dimension(400, 400));

    for (Number n : dataGraph.getEdges()) {
      Pair<String> endpoints = dataGraph.getEndpoints(n);
      MyShape<?> first = S.createShape(layout, endpoints.getFirst());
      MyShape<?> second = S.createShape(layout, endpoints.getSecond());

      result.addEdge(createLine(first, second), first, second);
    }

    return result;
  }

  public static Line createLine(MyShape<?> first, MyShape<?> second) {
    Line line = new Line();
    line.setStroke(Color.BLACK);
    line.startXProperty().bind(first.getXProperty());
    line.startYProperty().bind(first.getYProperty());
    line.endXProperty().bind(second.getXProperty());
    line.endYProperty().bind(second.getYProperty());
    return line;
  }

  /**
   * Draws the graph on a JavaFX group
   * 
   * @param group
   *          JavaFX group
   */
  public void draw(Group group) {
    for (MyShape<?> c : displayGraph.getVertices())
      if (!group.getChildren().contains(c))
        group.getChildren().addAll(c.getNode());

    for (Line l : displayGraph.getEdges())
      if (!group.getChildren().contains(l))
        group.getChildren().add(l);
  }
}
