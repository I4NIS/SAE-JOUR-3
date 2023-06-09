package fr.but.info.sae122.seance3;

import fr.but.info.sae122.seance3.model.Graph;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

public class LinkMouseController extends MouseController implements Initializable {

	private Controller controller;
	private int cpt;
	private GraphicNode node1;
	private GraphicNode node2;
	private String mode;

	public LinkMouseController(Graph graph, Controller controller, String mode) {
		super(graph);
		this.controller = controller;
		this.cpt = 0;
		this.mode = mode;
		node1 = null;
		node2 = null;
	}

	public void onMousePressed(MouseEvent evt) {
		if (cpt == 0)
			for (GraphicNode node : controller.nodes.values()) {
				if (evt.getX() > node.getX() && evt.getX() < node.getX() + node.getRadius() && evt.getY() > node.getY()
						&& evt.getY() < node.getY() + node.getRadius()) {
					cpt++;
					node1 = node;
					controller.guideUser("Cliquez sur le noeud destination...", Cursor.CROSSHAIR);
				}
			}
		else {
			if (mode.equals("add")) {
				for (GraphicNode node : controller.nodes.values()) {
					if (evt.getX() > node.getX() && evt.getX() < node.getX() + node.getRadius()
							&& evt.getY() > node.getY() && evt.getY() < node.getY() + node.getRadius()) {
						node2 = node;
						String key1 = getKey(node1);
						String key2 = getKey(node2);
						try {
							graph.getEdge(key1, key2);
							boolean bon = false;
							TextInputDialog dialog = new TextInputDialog("");
							while (!bon) {
								dialog = new TextInputDialog();
								dialog.setContentText("Entre la nouvelle capacité :");
								dialog.showAndWait();
								if (graph.getEdge(key1, key2).getFlow() < Integer.parseInt(dialog.getResult())) {
									bon = true;
									graph.getEdge(key1, key2).setCapacity(Integer.parseInt(dialog.getResult()));
								}
							}
						} catch (Exception err1) {
							TextInputDialog dialog = new TextInputDialog();
							dialog.setContentText("Entre la capacité :");
							dialog.showAndWait();
							try {
								if (dialog.getResult() == null) {
									Alert dialogError = new Alert(AlertType.ERROR);
									dialogError.setContentText("Tu ne peux pas laisser ce champ vide !");
									dialogError.show();
								} else {
									if (Integer.parseInt(dialog.getResult()) >= 0) {
										if (!key1.equals(key2)) {
											graph.addEdge(key1, key2, Integer.parseInt(dialog.getResult()));
										} else {
											throw new IllegalArgumentException();
										}
									} else {
										throw new NumberFormatException();
									}
								}
							} catch (NumberFormatException err2) {
								Alert dialogError = new Alert(AlertType.ERROR);
								dialogError.setContentText("Tu dois mettre un entier superier ou égal à zéro");
								dialogError.show();
							} catch (IllegalArgumentException err2) {
								Alert dialogError = new Alert(AlertType.ERROR);
								dialogError.setContentText("Tu ne peux pas lier une arete à elle meme");
								dialogError.show();
							}
						}

						controller.setMouseController(new IdleMouseController(graph, this.controller));
						controller.dessin();
					}
				}
			} else {
				for (GraphicNode node : controller.nodes.values()) {
					if (evt.getX() > node.getX() && evt.getX() < node.getX() + 2 * node.getRadius()
							&& evt.getY() > node.getY() && evt.getY() < node.getY() + 2 * node.getRadius()) {
						node2 = node;
						String key1 = getKey(node1);
						String key2 = getKey(node2);
						try {
							if (controller.path != null)
								if (controller.path.getPath().contains(graph.getEdge(key1, key2))) {
									controller.path = null;
									controller.amelioreFlux.setDisable(true);
								}
							graph.getEdge(key1, key2);
							graph.removeEdge(key1, key2);
						} catch (Exception e) {
							Alert a = new Alert(AlertType.ERROR);
							a.setContentText("Il n'y a pas d'arete entre ces 2 noeud");
							a.show();
							controller.setMouseController(new IdleMouseController(graph, this.controller));
							controller.dessin();
						}
						controller.setMouseController(new IdleMouseController(graph, this.controller));
						controller.dessin();
					}
				}
			}
		}
	}

	public String getKey(GraphicNode node) {
		for (String string : controller.nodes.keySet())
			if (controller.nodes.get(string) == node) {
				return string;
			}
		return null;
	}
}
