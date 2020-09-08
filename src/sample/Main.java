package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.CustomPanes.CustomAlterPane;
import sample.CustomPanes.CustomEffMapPane;
import sample.CustomPanes.CustomGraphPane;
import sample.CustomPanes.CustomStatusPane;
import sample.RouteNetwork.RouteNetwork;
import java.util.List;

public class Main extends Application {

    // Global Vars:
    private int time = 0;
    private String targetedCityCode = "";
    private CustomAlterPane alterPane = new CustomAlterPane();
    private CustomStatusPane statusPane = new CustomStatusPane();
    private CustomEffMapPane effMapPane = new CustomEffMapPane();
    private CustomGraphPane graphPane = new CustomGraphPane();
    Disease disease;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Collect travel data and format it.
        disease = new Disease("SARS-aprrox.", 2.75, 0.85); // Random realistic values for debugging purposes.
        RouteNetwork routeNetwork = new RouteNetwork();

        DistanceDataCollector distanceDataCollector = new DistanceDataCollector(routeNetwork, disease);

        double re1 =disease.getCaseIncidenceEqn2(disease.getTbTime()/2);

        // Do some initialising.
        CityNode.initCoords(effMapPane.getCanvas(), routeNetwork);

        // Create GUI.
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Simulation");

        BorderPane root = new BorderPane();

        // File system: menu, settings and help
        HBox upperHBox = new HBox(8);
        VBox upperVBox = new VBox(8);
        upperHBox.setPadding(new Insets(4, 4, 4, 4));
        MenuButton fileButton = new MenuButton("File");
        MenuItem newMenuItem = new MenuItem("New");

        MenuItem saveMenuItem = new MenuItem("Save");

        MenuItem downloadMenuItem = new MenuItem("Download");

        fileButton.getItems().addAll(newMenuItem, saveMenuItem, downloadMenuItem);

        Button effDisMapButton = new Button("EffDis View");
        effDisMapButton.setOnAction((event -> {
            root.setCenter(effMapPane);
        }));

        Button graphButton = new Button("Graph View");
        graphButton.setOnAction((event -> {
            root.setCenter(graphPane);
        }));

        Button settingsButton = new Button("Settings");

        Button helpButton = new Button("Help");

        upperHBox.getChildren().addAll(fileButton, effDisMapButton, graphButton, settingsButton, helpButton);

        upperVBox.getChildren().add(upperHBox);

        // Search function
        Label label1 = new Label("Search For City:");
        label1.setFont(Font.font(null, FontWeight.BLACK, 15));

        ComboBox searchComboBox = new ComboBox();
        searchComboBox.setEditable(true);

        // This will request focus after everything is initialised
        Platform.runLater(() -> {
            searchComboBox.requestFocus();
        });

        Button searchButton = new Button("Search");
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, searchComboBox, searchButton);
        hb.setSpacing(10);
        hb.setPadding(new Insets(0, 10, 0, 10));
        hb.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5 5 20 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        HBox temp = new HBox();
        temp.setAlignment(Pos.CENTER);
        temp.getChildren().add(hb);

        upperVBox.getChildren().add(temp);

        // Timer bar
        HBox wrapperTimerBar = new HBox();
        wrapperTimerBar.setAlignment(Pos.CENTER);
        HBox timerBarHBox = new HBox();
        wrapperTimerBar.getChildren().add(timerBarHBox);

        // Each section of the timer bar
        HBox sec1 = new HBox();
        HBox sec2 = new HBox();
        HBox sec3 = new HBox();

        // Section1:
        Label dayLabel = new Label("Go to day:");
        TextField dayChooseTF = new TextField("0");
        dayChooseTF.setPrefWidth(40);
        Button dayChooseBt = new Button("Go to");
        sec1.getChildren().addAll(dayLabel, dayChooseTF, dayChooseBt);

        // Section 2:
        Label day2Label = new Label("Current day:");
        TextField currentDayTF = new TextField("0");
        currentDayTF.setEditable(false);
        currentDayTF.setPrefWidth(40);
        sec2.getChildren().addAll(day2Label, currentDayTF);

        // Section 3:
        Button backButton = new Button("Back");
        Button pauseButton = new Button("Pause");
        Button playButton = new Button("Play");
        Button forwardButton = new Button("Forward");
        sec3.getChildren().addAll(backButton, pauseButton, playButton, forwardButton);

        timerBarHBox.getChildren().addAll(sec1, sec2, sec3);

        // Tidy up the timer bar:
        timerBarHBox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 15;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        timerBarHBox.setSpacing(16);
        timerBarHBox.setPadding(new Insets(4, 4, 4, 4));
        sec1.setSpacing(4);
        sec2.setSpacing(4);
        sec3.setSpacing(4);

        root.setTop(upperVBox);
        root.setLeft(statusPane);
        root.setRight(alterPane);

        // Colors and backgrounds.
        upperHBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#F4BD98"), CornerRadii.EMPTY, Insets.EMPTY)));
        String background = "#F2E8DF";

        upperVBox.setBackground(new Background(new BackgroundFill(Color.valueOf(background), CornerRadii.EMPTY, Insets.EMPTY)));
        statusPane.setBackground(new Background(new BackgroundFill(Color.valueOf(background), CornerRadii.EMPTY, Insets.EMPTY)));
        alterPane.setBackground(new Background(new BackgroundFill(Color.valueOf(background), CornerRadii.EMPTY, Insets.EMPTY)));
        wrapperTimerBar.setBackground(new Background(new BackgroundFill(Color.valueOf(background), CornerRadii.EMPTY, Insets.EMPTY)));

        // Default view on opening.
        VBox wrapperCenterDefault = new VBox();
        wrapperCenterDefault.getChildren().addAll(effMapPane, wrapperTimerBar);
        root.setCenter(wrapperCenterDefault);

        // View on button action.
        effDisMapButton.setOnAction((event -> {
            VBox wrapperCenterEffDisMap = new VBox();
            wrapperCenterEffDisMap.getChildren().addAll(effMapPane, wrapperTimerBar);
            root.setCenter(wrapperCenterEffDisMap);
        }));

        // View on button action.
        graphButton.setOnAction((event -> {
            VBox wrapperCenterGraph = new VBox();
            wrapperCenterGraph.getChildren().addAll(graphPane, wrapperTimerBar);
            root.setCenter(wrapperCenterGraph);
        }));

        // Implement the time bar and corresponding buttons.

        // Set a time line to increment time variable.
        Timeline timelineTimer = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> {
                            changeTimeBy(1, currentDayTF);
                            updateInfoPanes(routeNetwork, searchComboBox);
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timelineTimer.setCycleCount(Timeline.INDEFINITE);

        backButton.setOnAction(actionEvent -> {
            timelineTimer.pause();
            changeTimeBy(-1, currentDayTF);
            updateInfoPanes(routeNetwork, searchComboBox);
        });
        forwardButton.setOnAction(actionEvent -> {
            timelineTimer.pause();
            changeTimeBy(1, currentDayTF);
            updateInfoPanes(routeNetwork, searchComboBox);
        });
        pauseButton.setOnAction(actionEvent -> {
            timelineTimer.pause();
        });
        playButton.setOnAction(actionEvent -> {
            timelineTimer.play();
        });
        dayChooseBt.setOnAction(actionEvent -> {
            timelineTimer.pause();
            setTime(Integer.valueOf(dayChooseTF.getText()), currentDayTF);
            updateInfoPanes(routeNetwork, searchComboBox);
        });

        // Implement the search bar and corresponding button.
        searchButton.setOnAction(actionEvent -> {
            targetedCityCode = searchComboBox.getEditor().textProperty().getValue().split(" ")[0];

            // Calculate Multipaths and routing.
            routeNetwork.generateMultipathsForTarget(CityNode.findByName(targetedCityCode), routeNetwork);
            CityNode.findByName(targetedCityCode).plotCIGraph();
            updateInfoPanes(routeNetwork, searchComboBox);
        });

        searchComboBox.getEditor().textProperty().addListener(keyEvent -> {
            searchComboBox.requestFocus();
            searchComboBox.getSelectionModel().clearSelection();
            String text = searchComboBox.getEditor().textProperty().getValue();
            List<String> foundCitiesList = CityNode.findLike(text);
            if (foundCitiesList == null) return;
            searchComboBox.getItems().setAll(foundCitiesList);
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    // Getters and setters for our global time variable
    private void changeTimeBy(int timeChange, TextField view) {
        int timetemp = this.time + timeChange;
        if(timetemp >= 0) {
            time += timeChange;
            view.setText(String.valueOf(time));
        }
    }

    private void setTime(int time, TextField view) {
        if(time >= 0)
        {
            this.time = time;
            view.setText(String.valueOf(this.time));
        }
    }

    // This method will update the info panes based on any new information given,
    // e.g. time changed, city selected, routeNetwork updated.
    private void updateInfoPanes(RouteNetwork routeNetwork, ComboBox searchComboBox)
    {
        // Find city
        CityNode targetCity = CityNode.findByName(targetedCityCode);
        CityNode sourceCity = CityNode.getCenterTarget();

        if(targetCity == null) return; // Error handling.

        // Change colour back to normal.
        if(CityNode.getCurrentlySelectedNode() != null)
            CityNode.getCurrentlySelectedNode().setPaint(Color.BLACK);
        if(targetCity != null) {
            CityNode.setCurrentlySelectedNode(targetCity);
            targetCity.setPaint(Color.LAWNGREEN);
            CityNode.setLocMovement(true);
        }

        // Populate the info panes.
        searchComboBox.setValue("");

        // Status:
        statusPane.setCityName(targetCity.getName());
        statusPane.setCityPop(String.valueOf(targetCity.getCityPopulation()));
        statusPane.setCityEffDis(String.valueOf(routeNetwork.getMinEffDis(sourceCity, targetCity)));
        statusPane.setCityCaseIncidence(String.valueOf(targetCity.getCIEqn().get(time)));

        // Alter:
        alterPane.setAlterCityName(targetCity.getName());
        alterPane.setAlterCityRestrictions("None"); // TODO need to complete this once mechanics are in.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
