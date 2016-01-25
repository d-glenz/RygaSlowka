package hardcode.ryga.ui;

import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.control.*;

/**
 * 
 * @author Andy Till (andytill, https://gist.github.com/andytill/3024651), 
 * modified by Michael Winkler
 * 
 *
 */
public class FxUtils {

	/**
	 * Find a {@link Node} within a {@link Parent} by it's ID.
	 * <p>
	 * This might not cover all possible {@link Parent} implementations but it's
	 * a decent crack. {@link Control} implementations all seem to have their
	 * own method of storing children along side the usual
	 * {@link Parent#getChildrenUnmodifiable()} method.
	 *
	 * @param parent
	 *            The parent of the node you're looking for.
	 * @param id
	 *            The ID of node you're looking for.
	 * @return The {@link Node} with a matching ID or {@code null}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getChildByID(Parent parent, String id) {
		String nodeId = null;
		if (parent instanceof TitledPane) {
			TitledPane titledPane = (TitledPane) parent;
			Node content = titledPane.getContent();
			nodeId = content.idProperty().get();
			if (nodeId != null && nodeId.equals(id)) {
				return (T) content;
			}
			if (content instanceof Parent) {
				T child = getChildByID((Parent) content, id);
				if (child != null) {
					return child;
				}
			}
		}
		
		ObservableList<?> oList = null;
		if(parent instanceof TabPane) {
			oList = ((TabPane) parent).getTabs();
		} else {
			oList = parent.getChildrenUnmodifiable();
		}
		
		for (Object object : oList) {
			Node node;
			if(object instanceof Node) {
				node = (Node) object;
			} else if (object instanceof Tab) {
				node = ((Tab)object).getContent();
			} else {
				return null;
			}
			
			nodeId = node.idProperty().get();
			if (nodeId != null && nodeId.equals(id)) {
				return (T) node;
			}
			if (node instanceof SplitPane) {
				SplitPane splitPane = (SplitPane) node;
				for (Node itemNode : splitPane.getItems()) {
					nodeId = itemNode.idProperty().get();
					if (nodeId != null && nodeId.equals(id)) {
						return (T) itemNode;
					}
					if (itemNode instanceof Parent) {
						T child = getChildByID((Parent) itemNode, id);
						if (child != null) {
							return child;
						}
					}
				}
			} else if (node instanceof Accordion) {
				Accordion accordion = (Accordion) node;
				for (TitledPane titledPane : accordion.getPanes()) {
					nodeId = titledPane.idProperty().get();
					if (nodeId != null && nodeId.equals(id)) {
						return (T) titledPane;
					}
					T child = getChildByID(titledPane, id);
					if (child != null) {
						return child;
					}
				}
			} else if (node instanceof Parent) {
				T child = getChildByID((Parent) node, id);
				if (child != null) {
					return child;
				}
			}
		}
		
		return null;
	}
}