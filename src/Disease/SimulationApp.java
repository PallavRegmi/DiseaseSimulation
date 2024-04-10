package Disease;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(600, 400);
        GraphicsContext gc = canvas.getGraphicsConfiguration();

        Button startButton = new Button("Start");
       

        VBox root = new VBox(10, canvas, startButton);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Disease Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
