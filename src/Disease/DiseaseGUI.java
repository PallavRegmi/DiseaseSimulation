package Disease;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class DiseaseGUI extends Application {

    private static int distance;
    private static int incubation;
    private static int sickTime;
    private static double recovery;
    private static int initialSick;
    private static int height;
    private static int width;
    private static int row;
    private static int col;
    private static int num;
    private static int initialImmune;
    private static long START_TIME;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        stage.setTitle("Disease Simulation Project");

        BorderPane border = new BorderPane();
        Scene guiScene = new Scene(new StackPane(border));

        // create nodes for the GUI and set backgrounds and colors
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #E9967A; -fx-padding: 10;");
        vbox.setSpacing(8); // Added spacing

        HBox legend = new HBox(10);
        legend.setStyle("-fx-background-color: #E9967A; -fx-padding: 10;");
        legend.setSpacing(8); // Added spacing

        vbox.setMaxWidth(140);
        vbox.setMinWidth(140);
        legend.setMinHeight(40);
        legend.setMaxHeight(40);
        CornerRadii cr = new CornerRadii(2);
        Insets ins = new Insets(2,2,2,2);
        BackgroundFill bgf = new BackgroundFill(Color.BLUEVIOLET, cr, ins);
        Background bg = new Background(bgf);
        vbox.setBackground(bg);
        Canvas canvas = new Canvas(800,800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Create circles and labels for the legend
        Label vulnerableEmoji = new Label("👤");
        Label incubationEmoji = new Label("🦠");
        Label sickEmoji = new Label("😷");
        Label immuneEmoji = new Label("🍏");
        Label ghostEmoji = new Label("👻");
        Label vulnerable = new Label("Vulnerable");
        Label incubation = new Label("Incubating");
        Label sick = new Label("Sick");
        Label immune = new Label("Immune");
        Label ghost = new Label("Ghost");
        Label deadEmoji = new Label("🪦");
        Label dead = new Label("Dead");
        Font legendFont = Font.font("Calibri", FontWeight.BOLD, 12);

        // set fonts for the legend
        vulnerable.setFont(legendFont);
        incubation.setFont(legendFont);
        sick.setFont(legendFont);
        immune.setFont(legendFont);
        ghost.setFont(legendFont);
        dead.setFont(legendFont);

        // create VBoxes for the legend
        VBox vulnerableFont = new VBox();
        VBox incubatingFont = new VBox();
        VBox sickFont = new VBox();
        VBox immuneFont = new VBox();
        VBox ghostFont = new VBox();
        VBox deadFont = new VBox();

        vulnerableFont.getChildren().addAll(vulnerable, vulnerableEmoji);
        incubatingFont.getChildren().addAll(incubation, incubationEmoji);
        sickFont.getChildren().addAll(sick, sickEmoji);
        immuneFont.getChildren().addAll(immune, immuneEmoji);
        ghostFont.getChildren().addAll(ghost, ghostEmoji);
        deadFont.getChildren().addAll(dead, deadEmoji);
        legend.getChildren().addAll(vulnerableFont, incubatingFont, sickFont,
                immuneFont, ghostFont, deadFont);

        // create button for submitting new parameters
        Button startButton = new Button();
        startButton.setText("Start");
        Button submit = new Button("Submit");
        StackPane startStack = new StackPane();
        VBox logBox = new VBox();
        startStack.getChildren().add(startButton);

        // set size and alignment for button and StackPane wrapper
        submit.setMinWidth(130);
        submit.setMaxWidth(130);
        startStack.setMinHeight(200);
        startStack.setMaxHeight(200);
        startButton.setMinWidth(130);
        startButton.setMaxWidth(130);
        startStack.setAlignment(Pos.CENTER);

        // create options for the drop-down menus
        ObservableList<Integer> distOpts = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
                33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
                49, 50
        );

        ObservableList<Integer> incubOpts = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30
        );
        ObservableList<Integer> sickTimeOpts = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30
        );
        ObservableList<Double> recovOpts = FXCollections.observableArrayList(
                0.0, 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09,
                0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2,
                0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29, 0.3, 0.31,
                0.32, 0.33, 0.34, 0.35, 0.36, 0.37, 0.38, 0.39, 0.4, 0.41, 0.42,
                0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5, 0.51, 0.52, 0.53,
                0.54, 0.55, 0.56, 0.57, 0.58, 0.59, 0.6, 0.61, 0.62, 0.63, 0.64,
                0.65, 0.66, 0.67, 0.68, 0.69, 0.7, 0.71, 0.72, 0.73, 0.74, 0.75,
                0.76, 0.77, 0.78, 0.79, 0.8, 0.81, 0.82, 0.83, 0.84, 0.85, 0.86,
                0.87, 0.88, 0.89, 0.9, 0.91, 0.92, 0.93, 0.94, 0.95, 0.95, 0.96,
                0.97, 0.98, 0.99, 1.0
        );
        ObservableList<Integer> initSickOpts = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );
        ObservableList<Integer> initImmOpts = FXCollections.observableArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );
        ObservableList<Double> reanOpts = FXCollections.observableArrayList(
                0.0, 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09,
                0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2,
                0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29, 0.3, 0.31,
                0.32, 0.33, 0.34, 0.35, 0.36, 0.37, 0.38, 0.39, 0.4, 0.41, 0.42,
                0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5, 0.51, 0.52, 0.53,
                0.54, 0.55, 0.56, 0.57, 0.58, 0.59, 0.6, 0.61, 0.62, 0.63, 0.64,
                0.65, 0.66, 0.67, 0.68, 0.69, 0.7, 0.71, 0.72, 0.73, 0.74, 0.75,
                0.76, 0.77, 0.78, 0.79, 0.8, 0.81, 0.82, 0.83, 0.84, 0.85, 0.86,
                0.87, 0.88, 0.89, 0.9, 0.91, 0.92, 0.93, 0.94, 0.95, 0.95, 0.96,
                0.97, 0.98, 0.99, 1.0
        );
        ObservableList<Integer> reanTimeOpts = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );

        ObservableList<Integer> starveTimeOpts = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );


        // create drop-down menus
        ComboBox<Integer> distBox = new ComboBox(distOpts);
        ComboBox<Integer> incubBox = new ComboBox(incubOpts);
        ComboBox<Integer> sickTimeBox = new ComboBox(sickTimeOpts);
        ComboBox<Double> recovBox = new ComboBox(recovOpts);
        ComboBox<Integer> initSickBox = new ComboBox(initSickOpts);
        ComboBox<Integer> initImmBox = new ComboBox(initImmOpts);
        ComboBox<Double> reanBox = new ComboBox(reanOpts);
        ComboBox<Integer> reanTimeBox = new ComboBox(reanTimeOpts);
        CheckBox isGhostEnabled = new CheckBox("Enable ghosts");
        TextField configFile = new TextField();
        configFile.setPromptText("Add a config file");
        ComboBox<Integer> starveTimeBox = new ComboBox<Integer>(starveTimeOpts);

        // create labels and font for drop-down menus
        Label distLabel = new Label("Exposure Distance");
        Label incubLabel = new Label("Incubation Period");
        Label sickTimeLabel = new Label("Sickness Time");
        Label recovLabel = new Label("Recovery Probability");
        Label initSickLabel = new Label("Initial Sick");
        Label configLabel = new Label("Configuration File");
        Label initImmLabel = new Label("Initial Immune");
        Label messageLabel = new Label("        Message Log");
        Label reanLabel = new Label("Reanimation Chance");
        Label reanTimeLabel = new Label("Reanimation Time");
        Label starveTimeLabel = new Label("Starvation Time");
        Font labelFont = Font.font("Arial", FontWeight.BOLD, 13);

        // set fonts for drop-down menus
        distLabel.setFont(labelFont);
        incubLabel.setFont(labelFont);
        sickTimeLabel.setFont(labelFont);
        recovLabel.setFont(labelFont);
        initSickLabel.setFont(labelFont);
        configLabel.setFont(labelFont);
        startButton.setFont(labelFont);
        initImmLabel.setFont(labelFont);
        messageLabel.setFont(labelFont);
        reanLabel.setFont(labelFont);
        reanTimeLabel.setFont(labelFont);
        starveTimeLabel.setFont(labelFont);

        // set width size of drop-down menus
        distBox.setMinWidth(130);
        distBox.setMaxWidth(130);
        incubBox.setMinWidth(130);
        incubBox.setMaxWidth(130);
        sickTimeBox.setMinWidth(130);
        sickTimeBox.setMaxWidth(130);
        recovBox.setMinWidth(130);
        recovBox.setMaxWidth(130);
        initSickBox.setMinWidth(130);
        initSickBox.setMaxWidth(130);
        configFile.setMinWidth(130);
        configFile.setMaxWidth(130);
        initImmBox.setMinWidth(130);
        initImmBox.setMaxWidth(130);
        reanBox.setMinWidth(130);
        reanBox.setMaxWidth(130);
        reanTimeBox.setMinWidth(130);
        reanTimeBox.setMaxWidth(130);
        starveTimeBox.setMaxWidth(130);

        // create history of agents log
        TextArea messageLog = new TextArea();
        messageLog.setMinWidth(230);
        messageLog.setMaxWidth(230);
        messageLog.setMinHeight(700);
        messageLog.setMaxHeight(700);
        messageLog.setEditable(false);
        messageLog.setFont(legendFont);

        // set default values for drop-down menus at start
        distBox.setValue(20);
        incubBox.setValue(5);
        sickTimeBox.setValue(10);
        recovBox.setValue(0.95);
        initSickBox.setValue(1);
        initImmBox.setValue(0);
        reanBox.setValue(0.0);
        reanTimeBox.setValue(10);
        starveTimeBox.setValue(10);
        width = 200;
        height = 200;

        // create HashMap for timestamps of events
        HashMap<Integer, HealthState> timestep = new HashMap<>();

        // handles reading in the config file and changing the necessary values
        final AgentLayout AL = new AgentLayout();
        final AgentParameters AP = new AgentParameters();
        final AgentManager AM = new AgentManager();
        final Stage finalStage = stage;

        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String configName = configFile.getText();
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new FileReader(configName));
                    String line = reader.readLine();
                    while(line != null) {
                        if(line.contains("randomgrid")) {
                            String[] randims = line.split(" ");
                            row = Integer.parseInt(randims[1]);
                            col = Integer.parseInt(randims[2]);
                            num = Integer.parseInt(randims[3]);
                            AL.setType("randomgrid");
                            AL.setRows(row);
                            AL.setColumns(col);
                            AL.setNumAgents(num);
                            reanBox.setEditable(false);
                            reanTimeBox.setEditable(false);
                        }
                        else if(line.contains("random")) {
                            num = Integer.parseInt(line.split(" ")[1]);
                            AL.setType("random");
                            AL.setNumAgents(num);
                        }
                        else if(line.contains("grid")) {
                            String[] gridims = line.split(" ");
                            row = Integer.parseInt(gridims[1]);
                            col = Integer.parseInt(gridims[2]);
                            num = row * col;
                            AL.setType("grid");
                            AL.setRows(row);
                            AL.setColumns(col);
                            AL.setNumAgents(num);
                            reanBox.setEditable(false);
                            reanTimeBox.setEditable(false);
                        }
                        else if (line.contains("move")) {
                            int speed = Integer.parseInt(
                                    line.split(" ")[1]
                            );
                            AP.setSpeed(speed);
                        }
                        else if(line.contains("initialsick")) {
                            String[] initConfig = line.split(" ");
                            initialSick = Integer.parseInt(initConfig[1]);
                            AP.setInitialSick(initialSick);
                            initSickBox.setValue(initialSick);
                        }
                        else if(line.contains("dimensions")) {
                            String[] dims = line.split(" ");
                            width = Integer.parseInt(dims[2]);
                            height = Integer.parseInt(dims[2]);

                            AM.setDimensions(width, height);
                            AP.setWrapX(800);
                            AP.setWrapY(800);
                            canvas.setHeight(800);
                            canvas.setWidth(800);
                        }
                        else if(line.contains("exposuredistance")) {
                            String[] distConfig = line.split(" ");
                            distance = Integer.parseInt(distConfig[1]);
                            AP.setInfectDistance(distance);
                        }
                        else if(line.contains("incubation")) {
                            String[] incubConfig = line.split(" ");
                            DiseaseGUI.incubation = Integer.parseInt(incubConfig[1]);
                            AP.setIncubationPeriod(DiseaseGUI.incubation);
                        }
                        else if(line.contains("sickness")) {
                            String[] sickConfig = line.split(" ");
                            sickTime = Integer.parseInt(sickConfig[1]);
                            AP.setSicknessTime(sickTime);
                        }
                        else if(line.contains("recover")) {
                            String[] recovConfig = line.split(" ");
                            recovery = Double.parseDouble(recovConfig[1]);
                            AP.setRecoveryProbability(recovery);
                        }
                        // Additional required feature
                        else if(line.contains("initialimmune")) {
                            String[] immConfig = line.split(" ");
                            initialImmune = Integer.parseInt(immConfig[1]);
                            AP.setInitialImmune(initialImmune);
                        }
                        else if (line.contains("reanimate")) {
                            double reanimateProb = Double.parseDouble(
                                    line.split(" ")[1]
                            );
                            AP.setReanimateProb(reanimateProb);
                            reanBox.setValue(reanimateProb);

                            if (reanimateProb > 0 &&
                                    AL.getType().equals(LayoutType.RANDOM)) {
                                AP.becomeGhost(true);
                                isGhostEnabled.setSelected(true);
                            }
                        }
                        else if (line.contains("starvation")) {
                            int starveTime = Integer.parseInt(
                                    line.split(" ")[1]
                            );
                            AP.setStarvationTime(starveTime);
                        }
                        line = reader.readLine();
                    }
                    reader.close();

                    finalStage.sizeToScene();

                    incubBox.setValue(AP.getIncubationPeriod());
                    sickTimeBox.setValue(AP.getSicknessTime());
                    recovBox.setValue(AP.getRecoveryProb());
                    initSickBox.setValue(AP.getInitialSick());
                    initImmBox.setValue(AP.getInitialImmune());
                    reanBox.setValue(AP.getReanimateProb());
                    reanTimeBox.setValue(AP.getReanimateTime());
                    starveTimeBox.setValue(AP.getStarvationTime());

                    distOpts.clear();

                    for (int i = 0; i<(width); i++) {
                        distOpts.add(i+1);
                    }

                    distBox.setValue(AP.getInfectDistance());

                    int marginX = (int) ((canvas.getWidth() - width) / 2);
                    int marginY = (int) ((canvas.getHeight() - height) / 2);
                    gc.clearRect(0,0,canvas.getWidth(),
                            canvas.getHeight());
                    gc.setFill(Color.BLACK);
                    gc.fillRect(marginX,marginY,width + 10,height + 10);
                }
                catch (IOException e) {
                    System.out.println("Error: File not found");
                }
            }
        });

        // set mouse events for start and restart button
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(startButton.getText().equals("Start")) {
                    startButton.setText("Restart");
                    START_TIME = System.currentTimeMillis();

                    AP.setRecoveryProbability(recovBox.getValue());
                    AP.setIncubationPeriod(incubBox.getValue());
                    AP.setSicknessTime(sickTimeBox.getValue());
                    AP.setInfectDistance(distBox.getValue());
                    AP.setInitialSick(initSickBox.getValue());
                    AP.setInitialImmune(initImmBox.getValue());
                    AP.becomeGhost(isGhostEnabled.isSelected());
                    AP.setReanimateProb(reanBox.getValue());
                    AP.setReanimateTime(reanTimeBox.getValue());
                    AP.setStarvationTime(starveTimeBox.getValue());

                    AM.makeAgents(AL, AP);

                    if(initSickBox.getValue() > num) {
                        initSickBox.setValue(num);
                    }

                    if(initImmBox.getValue() > num) {
                        initImmBox.setValue(num);
                    }

                    for(Agent ag : AM.getAgents()) {
                        timestep.put(AM.getAgents().indexOf(ag) + 1,
                                ag.getHealth());
                    }

                    AnimationTimer a = new AnimationTimer() {


                        @Override
                        public void handle(long now) {
                            int mrgnX = (int)(canvas.getWidth() - width) / 2;
                            int mrgnY = (int)(canvas.getHeight() - height) / 2;

                            gc.setFill(Color.BLACK);
                            gc.fillRect(mrgnX, mrgnY, width + 10,
                                    height + 10);

                            HealthState hs;
                            Point2D pos;
                            Color fill;

                            for(Agent ag : AM.getAgents()) {
                                hs = ag.getHealth();
                                pos = ag.getPosition();
                                fill = switch (hs) {
                                    case VULNERABLE ->Color.YELLOW;
                                    case INCUBATING -> Color.ORANGE;
                                    case SICK -> Color.RED;
                                    case IMMUNE -> Color.GREEN;
                                    case DEAD -> Color.BLACK;
                                    case GHOST -> Color.PURPLE;
                                    case PERMADEAD -> Color.BLACK;
                                    default -> Color.YELLOW;
                                };
                                gc.setFill(fill);
                                if(fill == Color.YELLOW) {
                                    gc.fillText(" 👤",pos.getX() + mrgnX,
                                            pos.getY() + mrgnY);
                                    String m = updateMessage(timestep,hs,AM,ag);
                                    messageLog.appendText(m);
                                }
                                else if(fill == Color.ORANGE) {
                                    gc.fillText(" 🦠", pos.getX() + mrgnX,
                                            pos.getY() + mrgnY);
                                    String m = updateMessage(timestep, hs, AM, ag);
                                    messageLog.appendText(m);

                                }
                                else if(fill == Color.RED) {
                                    gc.fillText(" 😷", pos.getX() + mrgnX,
                                            pos.getY() + mrgnY);
                                    String m = updateMessage(timestep, hs, AM, ag);
                                    messageLog.appendText(m);

                                }
                                else if(fill == Color.GREEN) {
                                    gc.fillText("🍏", pos.getX() + mrgnX,
                                            pos.getY() + mrgnY);
                                    String m = updateMessage(timestep, hs, AM, ag);
                                    messageLog.appendText(m);

                                }
                                else if(fill == Color.PURPLE) {
                                    gc.fillText(" 👻", pos.getX() + mrgnX,
                                            pos.getY() + mrgnY);
                                    String m = updateMessage(timestep, hs, AM, ag);
                                    messageLog.appendText(m);

                                }
                                else {
                                    gc.setTextBaseline(VPos.CENTER);
                                    gc.setTextAlign(TextAlignment.CENTER);
                                    gc.fillText("🪦",
                                            pos.getX() + mrgnX + 3,
                                            pos.getY() + mrgnY + 3);
                                    String m = updateMessage(timestep,hs,AM,ag);
                                    messageLog.appendText(m);
                                }
                            }
                        }
                    };
                    a.start();
                    AM.start();
                }
                else if(startButton.getText().equals("Restart")) {
                    startButton.setText("Start");
                    messageLog.clear();

                }
            }
        });

        // add drop-down menus and buttons to the GUI
        vbox.getChildren().addAll(configLabel, configFile, submit, distLabel,
                distBox, incubLabel, incubBox, sickTimeLabel,
                sickTimeBox, recovLabel, recovBox,
                initSickLabel, initSickBox, initImmLabel,
                initImmBox, isGhostEnabled, reanLabel,
                reanBox, reanTimeLabel, reanTimeBox,
                starveTimeLabel, starveTimeBox, startStack);
        vbox.setAlignment(Pos.TOP_CENTER);

        // set and show the GUI
        logBox.getChildren().addAll(messageLabel, messageLog);
        legend.setAlignment(Pos.CENTER);
        BackgroundFill lgf = new BackgroundFill(Color.BLUE,
                CornerRadii.EMPTY, Insets.EMPTY);
        legend.setBackground(new Background(lgf));
        border.setCenter(canvas);
        border.setLeft(vbox);
        border.setBottom(legend);
        border.setRight(logBox);
        border.setMargin(legend, new Insets(0,0,20,0));
        border.setMargin(vbox, new Insets(20,0,0,0));
        border.setMargin(logBox, new Insets(20,0,0,0));
        stage.setFullScreen(true);
        stage.setScene(guiScene);
        stage.show();

        // Clean up threads if window closed prematurely
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * Helper method that gets the current system time in milliseconds and
     * converts that time into a more readable form in seconds. Since most
     * events occur at around 7 seconds after starting the simulation, the
     * time in seconds is subtracted by 6 to better start from either 0 or 1.
     * @return time in seconds
     */
    public long getCurrentTime() {
        return ((System.currentTimeMillis() - START_TIME) / 1000);
    }

    /**
     * Helper method that creates the message for the message log.
     * @param m HashMap of state events
     * @param hs current HealthState
     * @param am AgentManager object
     * @param a current Agent
     * @return String representation of the message
     */
    public String updateMessage(HashMap<Integer, HealthState> m,
                                HealthState hs, AgentManager am, Agent a) {
        String message = "";
        long currentTime = getCurrentTime();
        int index = am.getAgents().indexOf(a);
        if(hs != HealthState.DEAD) {
            if(m.containsKey(index) && hs != m.get(index)) {
                m.put(index, hs);
                message = "Agent " + index + " became " + hs + " at " +
                        currentTime + "days\n";
            }
        }
        else {
            if(m.containsKey(index) && hs != m.get(index)) {
                m.put(index, hs);
                message = "Agent " + index + " DIED at " + currentTime + " days\n";
            }
        }
        return message;
    }
}


// Ek choti hera ta