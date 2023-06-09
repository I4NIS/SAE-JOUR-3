package fr.but.info.sae122.seance3;

import fr.but.info.sae122.seance3.model.Graph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class IdleMouseController extends MouseController implements Initializable {

	private Controller controller;
	
	public IdleMouseController(Graph graph, Controller controller) {
		super(graph);
		this.controller = controller;
	}

	public void onMouseMoved(MouseEvent evt) {
		for (GraphicNode node : controller.nodes.values()) {
			if (evt.getX() > node.getX() && evt.getX() < node.getX()+node.getRadius() && evt.getY() > node.getY() && evt.getY() < node.getY()+node.getRadius()){
				controller.guideUser("Vous pouvez glisser le noeud...", Cursor.OPEN_HAND);
				controller.setMouseController(new DragMouseController(graph,controller));
			}
		}
		controller.guideUser("", Cursor.DEFAULT);
	}
}
