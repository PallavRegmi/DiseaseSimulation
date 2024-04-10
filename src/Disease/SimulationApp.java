package Disease;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimulationApp extends Application {

        private SimulationEngine engine = new SimulationEngine();
        private int timeStep = 0;
        private Stage primaryStage; // Declare primaryStage as a class member

        @Override
        public void start(Stage primaryStage) {
            this.primaryStage = primaryStage; // Initialize primaryStage

            Canvas canvas = new Canvas(400, 400);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            Button startButton = new Button("Start");
            startButton.setOnAction(event -> startSimulation(gc, canvas.getWidth(), canvas.getHeight()));

            // Chart setup
            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Time");
            yAxis.setLabel("Number of Agents");

            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Disease Spread Simulation");

            XYChart.Series<Number, Number> seriesVulnerable = new XYChart.Series<>();
            XYChart.Series<Number, Number> seriesSick = new XYChart.Series<>();
            XYChart.Series<Number, Number> seriesImmune = new XYChart.Series<>();
            XYChart.Series<Number, Number> seriesDead = new XYChart.Series<>();
            seriesVulnerable.setName("Vulnerable");
            seriesSick.setName("Sick");
            seriesImmune.setName("Immune");
            seriesDead.setName("Dead");

            lineChart.getData().addAll(seriesVulnerable, seriesSick, seriesImmune, seriesDead);

            VBox root = new VBox(10, canvas, startButton, lineChart);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Disease Simulation");
            primaryStage.show();
        }
    private void startSimulation(GraphicsContext gc, double width, double height) {
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                engine.update();
                drawAgents(gc, width, height);
                updateChart();
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

    private void updateChart() {
        // Update chart with agent counts
        XYChart.Series<Number, Number> seriesVulnerable = ((LineChart<Number, Number>) ((VBox) ((VBox) ((Scene) primaryStage.getScene()).getRoot()).getChildren().get(2)).getChildren().get(2)).getData().get(0);
        XYChart.Series<Number, Number> seriesSick = ((LineChart<Number, Number>) ((VBox) ((VBox) ((Scene) primaryStage.getScene()).getRoot()).getChildren().get(2)).getChildren().get(2)).getData().get(1);
        XYChart.Series<Number, Number> seriesImmune = ((LineChart<Number, Number>) ((VBox) ((VBox) ((Scene) primaryStage.getScene()).getRoot()).getChildren().get(2)).getChildren().get(2)).getData().get(2);
        XYChart.Series<Number, Number> seriesDead = ((LineChart<Number, Number>) ((VBox) ((VBox) ((Scene) primaryStage.getScene()).getRoot()).getChildren().get(2)).getChildren().get(2)).getData().get(3);

        seriesVulnerable.getData().add(new XYChart.Data<>(timeStep, engine.getAgentCounts().get(Agent.State.VULNERABLE)));
        seriesSick.getData().add(new XYChart.Data<>(timeStep, engine.getAgentCounts().get(Agent.State.SICK)));
        seriesImmune.getData().add(new XYChart.Data<>(timeStep, engine.getAgentCounts().get(Agent.State.IMMUNE)));
        seriesDead.getData().add(new XYChart.Data<>(timeStep, engine.getAgentCounts().get(Agent.State.DEAD)));

        timeStep++;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
