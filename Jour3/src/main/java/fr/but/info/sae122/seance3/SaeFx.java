package fr.but.info.sae122.seance3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import fr.but.info.sae122.seance3.model.Graph;
import fr.but.info.sae122.seance3.model.GraphGenerator;

public class SaeFx extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(SaeFx.class.getResource("graphe.fxml"));
			Controller c = new Controller(new Graph());
			fxmlLoader.setController(c);
			Scene scene = new Scene(fxmlLoader.load(), 800, 600);
			primaryStage.setTitle("Modelisateur de Graph - By Faboo, Zalgow, I4NIS et Cypri1");

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}