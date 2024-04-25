/***************************************************
 *CS351: Section 3SW
 *Project 4 - Disease Simulation Project
 *Authors : Ashmit Agrawal & Pallav Regmi
 *University of New Mexico - School of Engineering
 ***************************************************/

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
import java.util.*;



public class DiseaseMainGUI extends Application {

    private static int displacement;
    private static int incubTime;
    private static int sickTime;
    private static double healthy;
    private static int initSick;
    private static int length;
    private static int breadth;
    private static int horizontal;
    private static int vertical;
    private static int num;
    private static int healthyAtFirst;
    private static long start;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        stage.setTitle("Disease Simulation Project");

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(new StackPane(borderPane));

        VBox changeDetail = new VBox();
        changeDetail.setStyle("-fx-background-color: #86dcaa; -fx-padding: 10;");
        changeDetail.setSpacing(8); // Added spacing

        HBox healthStates = new HBox(10);
        healthStates.setStyle("-fx-background-color: #86dcaa; -fx-padding: 10;");
        healthStates.setSpacing(8); // Added spacing

        changeDetail.setMaxWidth(140);
        changeDetail.setMinWidth(140);
        healthStates.setMinHeight(40);
        healthStates.setMaxHeight(40);
        CornerRadii cradii = new CornerRadii(2);
        Insets inset = new Insets(2,2,2,2);
        BackgroundFill bgCol = new BackgroundFill(Color.BLUEVIOLET, cradii, inset);
        Background bg = new Background(bgCol);
        changeDetail.setBackground(bg);
        Canvas canvas = new Canvas(500,500);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.web("#000000"));
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Label and Emojis indicating Agents State
        Label vulnerableLabel = new Label("Vulnerable");
        Label vulnerableEmojiDepiction = new Label("👤");
        Label incubationLabel = new Label("Incubating");
        Label incubationEmojiDepiction = new Label("🦠");
        Label sickLabel = new Label("Sick");
        Label sickEmojiDepiction = new Label("😷");
        Label immuneLabel = new Label("Immune");
        Label immuneEmojiDepiction = new Label("🍏");
        Label ghostLabel = new Label("Ghost");
        Label ghostEmojiDepiction = new Label("👻");
        Label deadEmojiDepiction = new Label("🪦");
        Label deadLabel = new Label("Dead");
        Font healthStateFont = Font.font("Calibri", FontWeight.BOLD, 12);

        // Fonts
        vulnerableLabel.setFont(healthStateFont);
        incubationLabel.setFont(healthStateFont);
        sickLabel.setFont(healthStateFont);
        immuneLabel.setFont(healthStateFont);
        ghostLabel.setFont(healthStateFont);
        deadLabel.setFont(healthStateFont);

        VBox vulnerableFontDetails = new VBox();
        VBox incubatingFontDetails = new VBox();
        VBox sickFontDetails = new VBox();
        VBox immuneFontDetails = new VBox();
        VBox ghostFontDetails = new VBox();
        VBox deadFontDetails = new VBox();



        vulnerableFontDetails.getChildren().addAll(vulnerableLabel, vulnerableEmojiDepiction);
        incubatingFontDetails.getChildren().addAll(incubationLabel, incubationEmojiDepiction);
        sickFontDetails.getChildren().addAll(sickLabel, sickEmojiDepiction);
        immuneFontDetails.getChildren().addAll(immuneLabel, immuneEmojiDepiction);
        ghostFontDetails.getChildren().addAll(ghostLabel, ghostEmojiDepiction);
        deadFontDetails.getChildren().addAll(deadLabel, deadEmojiDepiction);
        healthStates.getChildren().addAll(vulnerableFontDetails, incubatingFontDetails, sickFontDetails,
                immuneFontDetails, ghostFontDetails, deadFontDetails);

        // Start and Submit Button
        Button start = new Button();
        start.setText("Start");
        Button submit = new Button("Submit");
        StackPane stackPane = new StackPane();
        VBox messageBox = new VBox();
        stackPane.getChildren().add(start);

        // set size and alignment for button and StackPane wrapper
        submit.setMinWidth(130);
        submit.setMaxWidth(130);
        stackPane.setMinHeight(200);
        stackPane.setMaxHeight(200);
        start.setMinWidth(130);
        start.setMaxWidth(130);
        stackPane.setAlignment(Pos.CENTER);

        // create options for the drop-down menus
        ObservableList<Integer> distances = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
                33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
                49, 50
        );

        ObservableList<Integer> incubation = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30
        );
        ObservableList<Integer> sickList = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30
        );
        ObservableList<Double> healthyList = FXCollections.observableArrayList(
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
        ObservableList<Integer> initSickOptsList = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );
        ObservableList<Integer> initImmOptsList = FXCollections.observableArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );
        ObservableList<Double> reanOptsList = FXCollections.observableArrayList(
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
        ObservableList<Integer> reanTimeOptsList = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );

        ObservableList<Integer> starveTimeOptsList = FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );


        // create drop-down menus
        ComboBox<Integer> distanceBox = new ComboBox(distances);
        ComboBox<Integer> incubBox = new ComboBox(incubation);
        ComboBox<Integer> sickBox = new ComboBox(sickList);
        ComboBox<Double> recoverBox = new ComboBox(healthyList);
        ComboBox<Integer> carrierBox = new ComboBox(initSickOptsList);
        ComboBox<Integer> HealthyBox = new ComboBox(initImmOptsList);
        ComboBox<Double> reanimateBox = new ComboBox(reanOptsList);
        ComboBox<Integer> reanimateTimeBox = new ComboBox(reanTimeOptsList);
        CheckBox isGhostEnabledCheckBox = new CheckBox("Enable ghosts");
        TextField configurationFileInput = new TextField();
        configurationFileInput.setPromptText("Add a configuration file");
        ComboBox<Integer> magixComboBox = new ComboBox<Integer>(starveTimeOptsList);

        // create labels and font for drop-down menus
        Label exposureDistanceLabel = new Label("Exposure Distance");
        Label incubationPeriodLabels = new Label("Incubation Period");
        Label sicknessTimeLabel = new Label("Sickness Time");
        Label recoveryProbLabel = new Label("Recovery Probability");
        Label initialSickLabel = new Label("Initial Sick");
        Label configFileLabel = new Label("Configuration File");
        Label initImmuneLabel = new Label("Initial Immune");
        Label informationLogLabel = new Label("        Message Log");
        Label reanimationChanceLabel = new Label("Ghost Chance");
        Label reanimationTimeLabel = new Label("Ghost Time");
        Label reanimatedLifeTime = new Label("Reanimated Life Time");
        Font textFont = Font.font("Calibri", FontWeight.BOLD, 13);

        //Fonts
        exposureDistanceLabel.setFont(textFont);
        incubationPeriodLabels.setFont(textFont);
        sicknessTimeLabel.setFont(textFont);
        recoveryProbLabel.setFont(textFont);
        initialSickLabel.setFont(textFont);
        configFileLabel.setFont(textFont);
        start.setFont(textFont);
        initImmuneLabel.setFont(textFont);
        informationLogLabel.setFont(textFont);
        reanimationChanceLabel.setFont(textFont);
        reanimationTimeLabel.setFont(textFont);
        reanimatedLifeTime.setFont(textFont);

        distanceBox.setMinWidth(130);
        distanceBox.setMaxWidth(130);
        incubBox.setMinWidth(130);
        incubBox.setMaxWidth(130);
        sickBox.setMinWidth(130);
        sickBox.setMaxWidth(130);
        recoverBox.setMinWidth(130);
        recoverBox.setMaxWidth(130);
        carrierBox.setMinWidth(130);
        carrierBox.setMaxWidth(130);
        configurationFileInput.setMinWidth(130);
        configurationFileInput.setMaxWidth(130);
        HealthyBox.setMinWidth(130);
        HealthyBox.setMaxWidth(130);
        reanimateBox.setMinWidth(130);
        reanimateBox.setMaxWidth(130);
        reanimateTimeBox.setMinWidth(130);
        reanimateTimeBox.setMaxWidth(130);
        magixComboBox.setMaxWidth(130);

        TextArea textArea = new TextArea();
        textArea.setMinWidth(230);
        textArea.setMaxWidth(230);
        textArea.setMinHeight(700);
        textArea.setMaxHeight(700);
        textArea.setEditable(false);
        textArea.setFont(healthStateFont);

        //Default Values of parameters

        distanceBox.setValue(20);
        incubBox.setValue(5);
        sickBox.setValue(10);
        recoverBox.setValue(0.95);
        carrierBox.setValue(1);
        HealthyBox.setValue(0);
        reanimateBox.setValue(0.0);
        reanimateTimeBox.setValue(10);
        magixComboBox.setValue(10);
        breadth = 200;
        length = 200;

        HashMap<Integer, AgentHealthStateEnum> timestep = new HashMap<>();

        // Reads configuration file
        final AgentInfoLayout agentInfoLayout = new AgentInfoLayout();
        final AgentParameters agentParameters = new AgentParameters();
        final AgentManagement agentManagement = new AgentManagement();
        final Stage lastStage = stage;

        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String inputFile = configurationFileInput.getText();
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new FileReader(inputFile));
                    String line = reader.readLine();
                    while(line != null) {
                        if(line.contains("randomgrid")) {
                            String[] randims = line.split(" ");
                            horizontal = Integer.parseInt(randims[1]);
                            vertical = Integer.parseInt(randims[2]);
                            num = Integer.parseInt(randims[3]);
                            agentInfoLayout.setLayoutType("randomgrid");
                            agentInfoLayout.setRws(horizontal);
                            agentInfoLayout.setCols(vertical);
                            agentInfoLayout.setAgentNumber(num);
                            reanimateBox.setEditable(false);
                            reanimateTimeBox.setEditable(false);
                        }
                        else if(line.contains("random")) {
                            num = Integer.parseInt(line.split(" ")[1]);
                            agentInfoLayout.setLayoutType("random");
                            agentInfoLayout.setAgentNumber(num);
                        }
                        else if(line.contains("grid")) {
                            String[] gridims = line.split(" ");
                            horizontal = Integer.parseInt(gridims[1]);
                            vertical = Integer.parseInt(gridims[2]);
                            num = horizontal * vertical;
                            agentInfoLayout.setLayoutType("grid");
                            agentInfoLayout.setRws(horizontal);
                            agentInfoLayout.setCols(vertical);
                            agentInfoLayout.setAgentNumber(num);
                            reanimateBox.setEditable(false);
                            reanimateTimeBox.setEditable(false);
                        }
                        else if (line.contains("move")) {
                            int velocity = Integer.parseInt(
                                    line.split(" ")[1]
                            );
                            agentParameters.setSpeed(velocity);
                        }
                        else if(line.contains("initialsick")) {
                            String[] initialConfig = line.split(" ");
                            initSick = Integer.parseInt(initialConfig[1]);
                            agentParameters.setSick(initSick);
                            carrierBox.setValue(initSick);
                        }
                        else if(line.contains("dimensions")) {
                            String[] dimensions = line.split(" ");
                            breadth = Integer.parseInt(dimensions[2]);
                            length = Integer.parseInt(dimensions[2]);

                            agentManagement.setProportions(breadth, length);
                            agentParameters.setX(breadth);
                            agentParameters.setY(length);
                            canvas.setHeight(length);
                            canvas.setWidth(breadth);
                        }
                        else if(line.contains("exposuredistance")) {
                            String[] distanceConfigurations = line.split(" ");
                            displacement = Integer.parseInt(distanceConfigurations[1]);
                            agentParameters.setVulnDist(displacement);
                        }
                        else if(line.contains("incubationLabel")) {
                            String[] incubationConfigurations = line.split(" ");
                            DiseaseMainGUI.incubTime = Integer.parseInt(incubationConfigurations[1]);
                            agentParameters.setIncTime(DiseaseMainGUI.incubTime);
                        }
                        else if(line.contains("sickness")) {
                            String[] sicknessConfiguration = line.split(" ");
                            sickTime = Integer.parseInt(sicknessConfiguration[1]);
                            agentParameters.setSickTime(sickTime);
                        }
                        else if(line.contains("recover")) {
                            String[] recoveryConfigurations = line.split(" ");
                            healthy = Double.parseDouble(recoveryConfigurations[1]);
                            agentParameters.setHealthyChance(healthy);
                        }
                        // Additional required feature
                        else if(line.contains("initialimmune")) {
                            String[] immuneConfigurations = line.split(" ");
                            healthyAtFirst = Integer.parseInt(immuneConfigurations[1]);
                            agentParameters.setImmune(healthyAtFirst);
                        }
                        else if (line.contains("reanimate")) {
                            double reanimationProbability = Double.parseDouble(
                                    line.split(" ")[1]
                            );
                            agentParameters.setReanimateProb(reanimationProbability);
                            reanimateBox.setValue(reanimationProbability);

                            if (reanimationProbability > 0 &&
                                    agentInfoLayout.getLayoutType().equals(LayoutTypeEnum.RANDOM)) {
                                agentParameters.becomeGhost(true);
                                isGhostEnabledCheckBox.setSelected(true);
                            }
                        }
                        else if (line.contains("starvation")) {
                            int starveTime = Integer.parseInt(
                                    line.split(" ")[1]
                            );
                            agentParameters.setGhostLife(starveTime);
                        }
                        line = reader.readLine();
                    }
                    reader.close();

                    lastStage.sizeToScene();

                    incubBox.setValue(agentParameters.getIncTime());
                    sickBox.setValue(agentParameters.getSickTime());
                    recoverBox.setValue(agentParameters.getRecoveryProb());
                    carrierBox.setValue(agentParameters.getSick());
                    HealthyBox.setValue(agentParameters.getImmune());
                    reanimateBox.setValue(agentParameters.getReanimateProb());
                    reanimateTimeBox.setValue(agentParameters.getGhostTime());
                    magixComboBox.setValue(agentParameters.getGhostLife());

                    distances.clear();

                    for (int i = 0; i<(breadth); i++) {
                        distances.add(i+1);
                    }

                    distanceBox.setValue(agentParameters.getVulnDist());

                    int x_margin = (int) ((canvas.getWidth() - breadth) / 2);
                    int y_margin = (int) ((canvas.getHeight() - length) / 2);
                    graphics.clearRect(0,0,canvas.getWidth(),
                            canvas.getHeight());
                    graphics.setFill(Color.BLACK);
                    graphics.fillRect(x_margin,y_margin, breadth + 10, length + 10);
                }
                catch (Exception e) {
                    System.out.println("Error: File not found");
                }
            }
        });


        start.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(start.getText().equals("Start")) {
                    start.setText("Restart");
                    DiseaseMainGUI.start = System.currentTimeMillis();

                    agentParameters.setHealthyChance(recoverBox.getValue());
                    agentParameters.setIncTime(incubBox.getValue());
                    agentParameters.setSickTime(sickBox.getValue());
                    agentParameters.setVulnDist(distanceBox.getValue());
                    agentParameters.setSick(carrierBox.getValue());
                    agentParameters.setImmune(HealthyBox.getValue());
                    agentParameters.becomeGhost(isGhostEnabledCheckBox.isSelected());
                    agentParameters.setReanimateProb(reanimateBox.getValue());
                    agentParameters.setGhostTime(reanimateTimeBox.getValue());
                    agentParameters.setGhostLife(magixComboBox.getValue());

                    agentManagement.createAgents(agentInfoLayout, agentParameters);

                    if(carrierBox.getValue() > num) {
                        carrierBox.setValue(num);
                    }

                    if(HealthyBox.getValue() > num) {
                        HealthyBox.setValue(num);
                    }

                    for(Agent ag : agentManagement.getAgentList()) {
                        timestep.put(agentManagement.getAgentList().indexOf(ag) + 1,
                                ag.getHealthStateOfAgent());
                    }

                    AnimationTimer timer = new AnimationTimer() {


                        @Override
                        public void handle(long now) {
                            int x_margin = (int)(canvas.getWidth() - breadth) / 2;
                            int y_margin = (int)(canvas.getHeight() - length) / 2;
                            graphics.setFill(Color.BLACK);
                            graphics.fillRect(x_margin, y_margin, breadth + 10,
                                    length + 10);

                            AgentHealthStateEnum stateOfAgent;
                            Point2D point2D;
                            Color fillColor;

                            for(Agent agent : agentManagement.getAgentList()) {
                                stateOfAgent = agent.getHealthStateOfAgent();
                                point2D = agent.getPointOfPosition();
                                fillColor = switch (stateOfAgent) {
                                    case VULNERABLE ->Color.YELLOW;
                                    case INCUBATING -> Color.ORANGE;
                                    case SICK -> Color.RED;
                                    case IMMUNE -> Color.GREEN;
                                    case DEAD -> Color.BLACK;
                                    case GHOST -> Color.PURPLE;
                                    case PERMADEAD -> Color.BLACK;
                                    default -> Color.YELLOW;
                                };
                                graphics.setFill(fillColor);
                                if(fillColor == Color.YELLOW) {
                                    graphics.fillText(" 👤",point2D.getX() + x_margin,
                                            point2D.getY() + y_margin);
                                    String updatedMessage = updateMessage(timestep,stateOfAgent,agentManagement,agent);
                                    textArea.appendText(updatedMessage);
                                }
                                else if(fillColor == Color.ORANGE) {
                                    graphics.fillText(" 🦠", point2D.getX() + x_margin,
                                            point2D.getY() + y_margin);
                                    String updatedMessage = updateMessage(timestep, stateOfAgent, agentManagement, agent);
                                    textArea.appendText(updatedMessage);

                                }
                                else if(fillColor == Color.RED) {
                                    graphics.fillText(" 😷", point2D.getX() + x_margin,
                                            point2D.getY() + y_margin);
                                    String updatedMessage = updateMessage(timestep, stateOfAgent, agentManagement, agent);
                                    textArea.appendText(updatedMessage);

                                }
                                else if(fillColor == Color.GREEN) {
                                    graphics.fillText("🍏", point2D.getX() + x_margin,
                                            point2D.getY() + y_margin);
                                    String updatedMessage = updateMessage(timestep, stateOfAgent, agentManagement, agent);
                                    textArea.appendText(updatedMessage);

                                }
                                else if(fillColor == Color.PURPLE) {
                                    graphics.fillText(" 👻", point2D.getX() + x_margin,
                                            point2D.getY() + y_margin);
                                    String updatedMessage = updateMessage(timestep, stateOfAgent, agentManagement, agent);
                                    textArea.appendText(updatedMessage);

                                }
                                else {
                                    graphics.setTextBaseline(VPos.CENTER);
                                    graphics.setTextAlign(TextAlignment.CENTER);
                                    graphics.fillText("🪦",
                                            point2D.getX() + x_margin + 3,
                                            point2D.getY() + y_margin + 3);
                                    String updatedMessage = updateMessage(timestep,stateOfAgent,agentManagement,agent);
                                    textArea.appendText(updatedMessage);
                                }
                            }
                        }
                    };
                    timer.start();
                    agentManagement.start();
                }
                else if(start.getText().equals("Restart")) {
                    start.setText("Start");
                    textArea.clear();

                }
            }
        });

        // Adding above Vbox and button.
        changeDetail.getChildren().addAll(configFileLabel, configurationFileInput, submit, exposureDistanceLabel,
                distanceBox, incubationPeriodLabels, incubBox, sicknessTimeLabel,
                sickBox, recoveryProbLabel, recoverBox,
                initialSickLabel, carrierBox, initImmuneLabel,
                HealthyBox, isGhostEnabledCheckBox, reanimationChanceLabel,
                reanimateBox, reanimationTimeLabel, reanimateTimeBox,
                reanimatedLifeTime, magixComboBox, stackPane);
        changeDetail.setAlignment(Pos.TOP_CENTER);

        messageBox.getChildren().addAll(informationLogLabel, textArea);
        healthStates.setAlignment(Pos.CENTER);
        BackgroundFill backgroundColorFill = new BackgroundFill(Color.BLUE,
                CornerRadii.EMPTY, Insets.EMPTY);
        healthStates.setBackground(new Background(backgroundColorFill));
        borderPane.setCenter(canvas);
        borderPane.setRight(messageBox);
        borderPane.setTop(healthStates);
        borderPane.setLeft(changeDetail);
        borderPane.setMargin(healthStates, new Insets(15,0,0,0));
        borderPane.setMargin(changeDetail, new Insets(0,0,0,0));
        borderPane.setMargin(messageBox, new Insets(20,0,0,0));
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * This helper method fetches the current system time in milliseconds and converts it
     * to a more user-friendly format in seconds. As events typically begin approximately
     * 7 seconds after initiating the simulation, the method subtracts 6 from the time in
     * seconds, allowing the counter to start at either 0 or 1.
     * @return time in seconds
     */

    public long getCurrentTime() {
        return ((System.currentTimeMillis() - start) / 1000);
    }

    /**
     * This helper method constructs a log message based on various parameters related to the agent's state.
     * @param integerHealthStateOfAgentHashMap HashMap containing state events keyed by integer identifiers.
     * @param stateOfAgent the current HealthState of the agent.
     * @param agentManagement an instance of AgentManager handling agent operations.
     * @param agent the current Agent instance.
     * @return String representation of the constructed message.
     */

    public String updateMessage(HashMap<Integer, AgentHealthStateEnum> integerHealthStateOfAgentHashMap,
                                AgentHealthStateEnum stateOfAgent, AgentManagement agentManagement, Agent agent) {
        String initialMessage = "";
        long initialTime = getCurrentTime();
        int agentIndex = agentManagement.getAgentList().indexOf(agent);
        if(stateOfAgent != AgentHealthStateEnum.DEAD) {
            if(integerHealthStateOfAgentHashMap.containsKey(agentIndex) && stateOfAgent != integerHealthStateOfAgentHashMap.get(agentIndex)) {
                integerHealthStateOfAgentHashMap.put(agentIndex, stateOfAgent);
                initialMessage = "Agent " + agentIndex + " became " + stateOfAgent + " at " +
                        initialTime + "days\n";
            }
        }
        else {
            if(integerHealthStateOfAgentHashMap.containsKey(agentIndex) && stateOfAgent != integerHealthStateOfAgentHashMap.get(agentIndex)) {
                integerHealthStateOfAgentHashMap.put(agentIndex, stateOfAgent);
                initialMessage = "Agent " + agentIndex + " DIED at " + initialTime + " days\n";
            }
        }
        return initialMessage;
    }
}

