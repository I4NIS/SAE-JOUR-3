package fr.but.info.sae122.seance3;

import fr.but.info.sae122.seance3.model.Graph;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

public class PlaceMouseController extends MouseController implements Initializable {

	private Controller controller;

	public PlaceMouseController(Graph graph, Controller controller) {
		super(graph);
		this.controller = controller;
	}

	public void onMousePressed(MouseEvent evt) {
	    boolean bon = false;
	    TextInputDialog dialog = new TextInputDialog("");
	    
	    while (!bon) {
	        String nom = "N" + (controller.nodes.size() + 1);
	        dialog = new TextInputDialog(nom);
	        dialog.setContentText("Entrez le nom de votre nœud :");
	        dialog.showAndWait();
	        
	        if (!this.graph.getNodes().contains(dialog.getResult())) {
	            bon = true;
	        } else {
	            // Appliquer le style CSS pour le texte en rouge
	            DialogPane dialogPane = dialog.getDialogPane();
	            dialogPane.lookupButton(ButtonType.OK).setStyle("-fx-text-fill: red;");
	        }
	    }
	    
	    if (dialog.getResult() != null) {
	        this.graph.addNode(dialog.getResult());
	        GraphicNode node = new GraphicNode(evt.getX(), evt.getY(), 50, "red");
	        controller.addNode(dialog.getResult(), node);
	    }
	    
	    controller.setMouseController(new IdleMouseController(graph, this.controller));
	}

}
