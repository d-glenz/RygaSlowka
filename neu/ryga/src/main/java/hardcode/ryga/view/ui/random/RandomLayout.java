package hardcode.ryga.view.ui.random;

import hardcode.ryga.view.ui.graph.Cell;
import hardcode.ryga.view.ui.graph.Graph;
import hardcode.ryga.view.ui.layout.base.Layout;

import java.util.List;
import java.util.Random;

/**
 * 
 * @author Roland,http://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx 
 * 
 *
 */
public class RandomLayout extends Layout {

    Graph graph;

    Random rnd = new Random();

    public RandomLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {

            double x = rnd.nextDouble() * 500;
            double y = rnd.nextDouble() * 500;

            cell.relocate(x, y);

        }

    }

}
