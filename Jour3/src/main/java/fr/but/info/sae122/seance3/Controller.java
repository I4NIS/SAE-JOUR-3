package fr.but.info.sae122.seance3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import fr.but.info.sae122.seance3.model.AugmentingPath;
import fr.but.info.sae122.seance3.model.Edge;
import fr.but.info.sae122.seance3.model.Graph;
import fr.but.info.sae122.seance3.model.GraphIO;
import fr.but.info.sae122.seance3.model.MaxFlow;
import fr.but.info.sae122.seance3.model.MaxFlowWithoutResidualGraph;
import fr.but.info.sae122.seance3.model.Path;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.fxml.Initializable;

public class Controller implements Initializable {

	
	@FXML
	private Button charge;
	@FXML
	private Button noeud;
	@FXML
	private Button pArete;
	@FXML
	private Button mArete;
	@FXML
	private Button sauve;
	@FXML
	private ToggleButton calcule;
	@FXML
	private ComboBox<String> menuSource;
	@FXML
	private ComboBox<String> menuDestination;
	@FXML
	private TextField nbFlux;
	@FXML
	private Button trouverChemin;
	@FXML
	public Button amelioreFlux;
	@FXML
	private Canvas canvas;
	@FXML
	private Label barEtat;
	@FXML
	private BorderPane page;
	@FXML
	private ToolBar toolBar;
	@FXML
	private Pane pane;

	public Path path;
	
	@FXML
	private VBox vbox;

	private Graph graphe;
	public HashMap<String, GraphicNode> nodes;

	private SimpleStringProperty source;
	private SimpleStringProperty sink;

	private MaxFlowWithoutResidualGraph maxFlow;

	private MouseController mouseController;

	private SimpleObjectProperty<Integer> fluxMax = new SimpleObjectProperty<>(0);

	@FXML
	public static final Background LIGHTGRAY = new Background(new BackgroundFill(Color.LIGHTGRAY, null, null));

	public Controller(Graph graphe) {
		this.graphe = graphe;
		this.nodes = new HashMap<String, GraphicNode>();
		sink = new SimpleStringProperty("");
		source = new SimpleStringProperty("");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		pane.setBackground(LIGHTGRAY);
		vbox.setBackground(LIGHTGRAY);

		this.mouseController = new IdleMouseController(graphe, this);
		
		canvas.setOnMouseMoved(event -> onMouseMoved(event));
		canvas.setOnMousePressed(event -> onMousePressed(event));
		canvas.setOnMouseDragged(event -> onMouseDragged(event));
		canvas.setOnMouseReleased(event -> onMouseReleased(event));
		canvas.setOnMouseClicked(event -> onMouseClicked(event));

		canvas.heightProperty().bind(Bindings.subtract(page.heightProperty(), toolBar.heightProperty()));
		canvas.widthProperty().bind(page.widthProperty());

		fillMenu();
		remplirNodes();

		menuSource.setOnAction(evt -> {
			String value = menuSource.getValue().toString();
			menuSource.setPromptText(value);
			source.set(value);
		});

		pane.visibleProperty().bind(calcule.selectedProperty());

		menuDestination.setOnAction(evt -> {
			String value = menuDestination.getValue().toString();
			menuDestination.setPromptText(value);
			sink.set(value);
		});

		charge.setOnMouseClicked(event -> {
			charger();
			dessin();
		});

		canvas.widthProperty().addListener((observable, oldValue, newValue) -> {
			dessin();
		});

		canvas.heightProperty().addListener((observable, oldValue, newValue) -> {
			dessin();
		});

		sauve.setOnMouseClicked(event -> {
			sauvegarder();
		});

		noeud.setOnAction(event -> {
			setMouseController(new PlaceMouseController(graphe, this));
			guideUser("Placer le noeud a l'écran...", Cursor.CROSSHAIR);
		});

		pArete.setOnAction(event -> {
			setMouseController(new LinkMouseController(graphe, this, "add"));
			guideUser("Cliquez sur le noeud de depart...", Cursor.CROSSHAIR);

		});

		mArete.setOnAction(event -> {
			setMouseController(new LinkMouseController(graphe, this, "supp"));
			guideUser("Cliquez sur le noeud destination", Cursor.CROSSHAIR);
		});

		pane.visibleProperty().addListener(evt -> {
			if (pane.isVisible()) {
				menuSource.setItems(FXCollections.observableArrayList(graphe.getNodes()));
				menuDestination.setItems(FXCollections.observableArrayList(graphe.getNodes()));
				for (Edge e : graphe.getEdges()) {
					e.setFlow(0);
				}

				nbFlux.textProperty().bind(fluxMax.asString());
				amelioreFlux.setDisable(true);
			}
		});

		trouverChemin.disableProperty().bind(menuSource.getSelectionModel().selectedItemProperty().isNull().or(menuDestination.getSelectionModel().selectedItemProperty().isNull()));

		trouverChemin.setOnMouseClicked(evt -> {
			try {
				maxFlow = new MaxFlowWithoutResidualGraph(graphe, source.get(), sink.get());
				path = maxFlow.getAugmentingPath();
				fluxMax.set(path.getFlow());
				amelioreFlux.setDisable(false);
				dessin();
			} catch (Exception e) {

				Alert dialogError = new Alert(AlertType.ERROR);
				dialogError.setContentText("Plus d'augmenting path");
				dialogError.show();
			}
		});

		amelioreFlux.setOnMouseClicked(event -> {
			maxFlow.increaseFlow(path);
			fluxMax.set(maxFlow.getSinkFlow());
			path = null;
			amelioreFlux.setDisable(true);
			dessin();
		});

		menuSource.setOnAction(evt -> {
			String s = menuSource.getValue();
			menuSource.setPromptText(s);
			source.set(s);
			for (Edge e : graphe.getEdges()) {
				e.setFlow(0);
			}
			fluxMax.set(0);
		});

		menuDestination.setOnAction(evt -> {
			String s = menuDestination.getValue();
			menuDestination.setPromptText(s);
			sink.set(s);
			for (Edge e : graphe.getEdges()) {
				e.setFlow(0);
			}
			fluxMax.set(0);
		});

	}

	public void dessin() {
		canvas.getGraphicsContext2D().clearRect(0, 0, page.getWidth(), page.getHeight());
		dessinEdges();
		dessinNodes();
	}

	public void dessinNodes() {
		GraphicsContext dessin = canvas.getGraphicsContext2D();
		for (String s : nodes.keySet()) {
			dessin.setFill(Paint.valueOf("#17aeff"));
			dessin.setStroke(Paint.valueOf("#4a3e3d"));
			dessin.fillRoundRect(nodes.get(s).getX(), nodes.get(s).getY(), nodes.get(s).getRadius(),
					nodes.get(s).getRadius(), 50, 50);
			dessin.strokeRoundRect(nodes.get(s).getX(), nodes.get(s).getY(), nodes.get(s).getRadius(),
					nodes.get(s).getRadius(), 50, 50);
			dessin.strokeText(s, nodes.get(s).getX() + nodes.get(s).getRadius() / 3,
					nodes.get(s).getY() + nodes.get(s).getRadius() / 2);
		}
	}

	public void dessinEdges() {
		GraphicsContext dessin = canvas.getGraphicsContext2D();
		for (Edge e : graphe.getEdges()) {
			GraphicNode fromNode = nodes.get(e.getFromNode());
			GraphicNode toNode = nodes.get(e.getToNode());
			dessin.setStroke(Paint.valueOf("black"));
			dessin.setFill(Paint.valueOf("black"));
			if (path != null)
				if (path.getPath().contains(e)) {
					dessin.setStroke(Paint.valueOf("green"));
					dessin.setFill(Paint.valueOf("green"));
				}
			drawArrow(dessin, fromNode.getX() + 25, fromNode.getY() + 25, toNode.getX() + 25, toNode.getY() + 25,
					e.getFromNode() + " -> " + e.getToNode() + " : " + e.getFlow() + "/" + e.getCapacity());
		}
	}

	public void sauvegarder() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV (séparateur : point virgule)", "*.csv"));

		File selectedFile = fileChooser.showSaveDialog(null);

		if (selectedFile != null) {
			OutputStream to = null;
			try {
				to = new FileOutputStream(selectedFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				Alert dialogError = new Alert(AlertType.ERROR);
				dialogError.setContentText("Mauvais fichier");
				dialogError.show();
			}

			System.out.println(selectedFile.getAbsolutePath());

			GraphIO.write(graphe, to);

		} else {
			System.out.println("file isn't valid !");
		}
	}

	public void charger() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charge un graphe");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV (separateur : point virgule)", "*.csv"));

		File choosedFile = fileChooser.showOpenDialog(null);
		if (choosedFile != null) {
			InputStream from = null;

			try {
				from = new FileInputStream(choosedFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				Alert dialogError = new Alert(AlertType.ERROR);
				dialogError.setContentText("Mauvais fichier");
				dialogError.show();
			}

			try {
				graphe = GraphIO.read(from);
			} catch (IOException e) {
				e.printStackTrace();
			}

			nodes = new HashMap<String, GraphicNode>();
			remplirNodes();

		}
	}

	public void remplirNodes() {
		int rayon = 150;
		int centerX = 400;
		int centerY = 200;
		int angle = 0;

		for (String s : graphe.getNodes()) {

			double radians = Math.toRadians(angle);
			int x = (int) (centerX + rayon * Math.cos(radians));
			int y = (int) (centerY + rayon * Math.sin(radians));

			GraphicNode graphicNode = new GraphicNode(x, y, 50, "red");
			nodes.put(s, graphicNode);
			angle += 360 / graphe.getNodes().size();
		}

	}

	public void fillMenu() {
		menuSource.getItems().addAll(graphe.getNodes());
		menuDestination.getItems().addAll(graphe.getNodes());
	}

	public void guideUser(String textBar, Cursor cursor) {
		barEtat.setText(textBar);
		canvas.setCursor(cursor);
	}

	public void setMouseController(MouseController mouseControlleur) {
		this.mouseController = mouseControlleur;
	}

	public void addNode(String nom, GraphicNode node) {
		nodes.put(nom, node);
		dessin();
	}
	
	public void onMouseMoved(MouseEvent evt) {
		mouseController.onMouseMoved(evt);
	}

	public void onMouseDragged(MouseEvent evt) {
		mouseController.onMouseDragged(evt);
	}

	public void onMousePressed(MouseEvent evt) {
		mouseController.onMousePressed(evt);
	}

	public void onMouseReleased(MouseEvent evt) {
		mouseController.onMouseReleased(evt);
	}

	public void onMouseClicked(MouseEvent evt) {
		mouseController.onMouseClicked(evt);
	}

	

	public void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2, String nom) {
		gc.save();
		int ARR_SIZE = 8;

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);

		Transform transform = Transform.translate(x1, y1);
		transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
		gc.setTransform(new Affine(transform));

		gc.strokeLine(0, 0, len, 0);
		gc.fillPolygon(new double[] { len - 20, len - ARR_SIZE - 40, len - ARR_SIZE - 40, len },
				new double[] { 0, -ARR_SIZE, ARR_SIZE, 0 }, 4);
		gc.strokeText(nom, len / 2 - 50, 12);
		gc.restore();
	}
}
