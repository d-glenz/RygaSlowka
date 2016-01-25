package hardcode.ryga.ui.layout.random;

import hardcode.ryga.ui.layout.base.Layout;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Piotr Kołaczkowski, http://stackoverflow.com/questions/18511095/logic-to-strategically-place-items-in-a-container-with-minimum-overlapping-conne
 * 
 *
 */
public class SimLayouter extends Layout {

    Graph graph;

    public SimLayouter(Graph graph) {
        this.graph = graph;
    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

//        for (Cell cell : cells) {
//            double x = rnd.nextDouble() * 500;
//            double y = rnd.nextDouble() * 500;
//            cell.relocate(x, y);
//        }
        
        List<Force> forces = calculateForces(cells);
        
        for (int i = 0; i < cells.size(); i++) {
        	Cell cell = cells.get(i);
        	double x = forces.get(i).x;
        	double y = forces.get(i).y;
        	cell.relocate(x, y);
        }
        
//        var forces = calculateForces(graph);
//        for (var i = 0; i < graph.length; i++) {
//          graph[i].x += forces[i].x;      
//          graph[i].y += forces[i].y;           
//        } 
    }
    
    private double repelForce(double distanceSqr) {
        return 5000.0 / distanceSqr;
      }

      private double attractForce(double distanceSqr) {
        return -distanceSqr / 20000.0;
      }

      private double gravityForce(double distanceSqr) {
        return -Math.sqrt(distanceSqr) / 1000.0;
      }
      
      private double distanceSqr(double dx, double dy) { 
          return dx * dx + dy * dy; 
        }

      
      private Force force (Cell nodeA, Cell nodeB) {
    	  double dx = nodeA.getCoordinates();
      }
      
      /*
        function force(nodeA, nodeB, distanceFn) {
          var dx = nodeA.x - nodeB.x;
          var dy = nodeA.y - nodeB.y;
          var angle = Math.atan2(dy, dx);
          var ds = distanceFn(distanceSqr(dx, dy));
          return { x: Math.cos(angle) * ds, y: Math.sin(angle) * ds };
        }
       */
    
    private List<Force> calculateForces(List<Cell> cells) {
    	ArrayList<Force> forces = new ArrayList<>();  
        for (int i = 0; i < cells.size(); i++) {
        	forces.add(new Force());
        	//forces[i] = { x: 0.0, y: 0.0 };

          // repelling between nodes:
          for (int j = 0; j < cells.size(); j++) {
            if (i == j) {
            	continue;
            }
            Force f = force(cells.get(i), cells.get(j), repelForce);
            forces.get(i).x += f.x;
            forces.get(i).y += f.y;
          }

          // attraction between connected nodes:
          for (int j = 0; j < graph[i].edges.length; j++) {
            var f = force(graph[i], graph[i].edges[j], attractForce);
            forces[i].x += f.x;
            forces[i].y += f.y;          
          }          

          // gravity:
          var center = { x: 400, y: 300 };
          var f = force(graph[i], center, gravityForce);
          forces[i].x += f.x;
          forces[i].y += f.y;           
        }  
        return forces;
      }
    
    class Force {
    	public Force() {
    		x = 0.0;
    		y = 0.0;
    	}
    	public double x;
    	public double y;
    }
}
