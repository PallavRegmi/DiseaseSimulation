package Disease;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    private SimulationEngine engine = new SimulationEngine();

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> startSimulation(gc, canvas.getWidth(), canvas.getHeight()));

        VBox root = new VBox(10, canvas, startButton);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Disease Simulation");
        primaryStage.show();
    }

    private void startSimulation(GraphicsContext gc, double width, double height) {
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                engine.update();
                drawAgents(gc, width, height);
            }
        }.start();
    }

    private void drawAgents(GraphicsContext gc, double width, double height) {
        gc.clearRect(0, 0, width, height);
        for (Agent agent : engine.getAgents()) {
            switch (agent.getState()) {
                case VULNERABLE:
                    gc.setFill(javafx.scene.paint.Color.BLUE);
                    break;
                case SICK:
                    gc.setFill(javafx.scene.paint.Color.RED);
                    break;
                case IMMUNE:
                    gc.setFill(javafx.scene.paint.Color.GREEN);
                    break;
                case DEAD:
                    gc.setFill(javafx.scene.paint.Color.BLACK);
                    break;
            }
            gc.fillOval(agent.getX(), agent.getY(), 5, 5);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
