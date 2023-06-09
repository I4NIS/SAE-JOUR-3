package fr.but.info.sae122.seance3;

import fr.but.info.sae122.seance3.model.Graph;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

public class DragMouseController extends MouseController implements Initializable {

	private Controller controller;
	private GraphicNode node;

	public DragMouseController(Graph graph, Controller controller) {
		super(graph);
		this.controller = controller;
	}

	public void onMouseDragged(MouseEvent evt) {
		for (GraphicNode node : controller.nodes.values()) {
			if (evt.getX() > node.getX() && evt.getX() < node.getX() + node.getRadius() && evt.getY() > node.getY()
					&& evt.getY() < node.getY() + node.getRadius()) {
				controller.guideUser("Deposez le noeud ou vous voulez...", Cursor.CLOSED_HAND);
				this.node = node;
				
			}
		}
		if (node != null) {
			node.setX(evt.getX());
			node.setY(evt.getY());
			controller.dessin();
		}
	}
	
	public void onMouseReleased(MouseEvent evt) {
		this.node =null;
	}

	public void onMouseMoved(MouseEvent evt) {
		for (GraphicNode node : controller.nodes.values()) {
			if (evt.getX() > node.getX() && evt.getX() < node.getX() + node.getRadius() && evt.getY() > node.getY()
					&& evt.getY() < node.getY() + node.getRadius()) {
				controller.guideUser("Vous pouvez glisser le noeud...", Cursor.OPEN_HAND);
				break;
			} else {
				controller.guideUser("", Cursor.DEFAULT);
			}
		}

	}

}
