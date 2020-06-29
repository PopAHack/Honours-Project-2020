package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.RouteNetwork.RouteNetwork;

import java.util.List;

public class Main extends Application {

    private int time = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Collect travel data and format it.
        Disease disease = new Disease("TestDisease",1.25,5,8); // Random realistic values for debugging purposes.
        RouteNetwork routeNetwork = new RouteNetwork();
        DistanceDataCollector distanceDataCollector = new DistanceDataCollector(routeNetwork, disease);

        // Create GUI.
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Simulation");

        BorderPane root = new BorderPane();

        // File system: menu, settings and help
        HBox upperHBox = new HBox(8);
        VBox upperVBox = new VBox(8);
        upperHBox.setPadding(new Insets(4,4,4,4));
        MenuButton fileButton = new MenuButton("File");
        MenuItem newMenuItem = new MenuItem("New");

        MenuItem saveMenuItem = new MenuItem("Save");

        MenuItem downloadMenuItem = new MenuItem("Download");

        fileButton.getItems().addAll(newMenuItem, saveMenuItem, downloadMenuItem);

        Button settingsButton = new Button("Settings");

        Button helpButton = new Button("Help");

        upperHBox.getChildren().addAll(fileButton, settingsButton, helpButton);

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
        hb.setPadding(new Insets(0,10,0,10));
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

        // Status page
        // No city is initially selected at the start of the program, so set to default values
        VBox leftVBox = new VBox();
        leftVBox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 0 25 5 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");

        Label statusLabel = new Label("Status Pane");
        statusLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        statusLabel.setPadding(new Insets(0,0,10,0));

        Label cityNameLabel1 = new Label("Selected Cities Name:");
        TextField cityNameTF2 = new TextField("No city selected");
        cityNameTF2.setEditable(false);
        VBox nameVBox = new VBox(cityNameLabel1, cityNameTF2);
        nameVBox.setSpacing(4);
        nameVBox.setPadding(new Insets(2,1,2,1));

        Label cityPopLabel1 = new Label("City Population:");
        TextField cityPopTF2 = new TextField("No city selected");
        cityPopTF2.setEditable(false);
        VBox popVBox = new VBox(cityPopLabel1, cityPopTF2);
        popVBox.setSpacing(4);
        popVBox.setPadding(new Insets(2,1,2,1));

        Label minEffDisLabel1 = new Label("Minimum Effective Distance:");
        TextField minEffDisTF2 = new TextField("No city selected");
        minEffDisTF2.setEditable(false);
        VBox minEffDisVBox = new VBox(minEffDisLabel1, minEffDisTF2);
        minEffDisVBox.setSpacing(4);
        minEffDisVBox.setPadding(new Insets(2,1,2,1));

        Label PDPLabel1 = new Label("PDP Value:");
        TextField PDPTF2 = new TextField("No city selected");
        PDPTF2.setEditable(false);
        VBox PDPVBox = new VBox(PDPLabel1, PDPTF2);
        PDPVBox.setSpacing(4);
        PDPVBox.setPadding(new Insets(2,1,2,1));

        Label RMSLabel1 = new Label("RMS Value:");
        TextField RMSTF2 = new TextField("No city selected");
        RMSTF2.setEditable(false);
        VBox RMSVBox = new VBox(RMSLabel1, RMSTF2);
        RMSVBox.setSpacing(4);
        RMSVBox.setPadding(new Insets(2,1,2,1));

        Label caseIncidenceLabel1 = new Label("caseIncidence Value:");
        TextField caseIncidenceTF2 = new TextField("No city selected");
        caseIncidenceTF2.setEditable(false);
        VBox caseIncidenceVBox = new VBox(caseIncidenceLabel1, caseIncidenceTF2);
        caseIncidenceVBox.setSpacing(4);
        caseIncidenceVBox.setPadding(new Insets(2,1,2,1));

        leftVBox.getChildren().addAll(statusLabel, nameVBox, popVBox, minEffDisVBox, PDPVBox, RMSVBox, caseIncidenceVBox);
        leftVBox.setPrefWidth(300);

        // Alter page
        // No city is initially selected at the start of the program, so set to default values
        VBox rightVBox = new VBox();
        rightVBox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 0 5 5 25;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");

        Label alterLabel = new Label("Alter or Edit Pane");
        alterLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        alterLabel.setPadding(new Insets(0,0,10,0));

        Label cityNameLabel1R = new Label("Selected Cities Name:");
        TextField cityNameTF2R = new TextField("No city selected");
        cityNameTF2R.setEditable(false);
        VBox nameRVBox = new VBox(cityNameLabel1R, cityNameTF2R);
        nameRVBox.setSpacing(4);
        nameRVBox.setPadding(new Insets(2,1,2,1));

        Label setRestrictions1 = new Label("Travel restrictions imposed from target city:");
        TextField setRestrictions2 = new TextField("No city selected"); // Measured in a percentage
        cityPopTF2.setEditable(true);
        VBox resVBox = new VBox(setRestrictions1, setRestrictions2);
        resVBox.setSpacing(4);
        resVBox.setPadding(new Insets(2,1,2,1));

        rightVBox.getChildren().addAll(alterLabel, nameRVBox, resVBox);
        rightVBox.setPrefWidth(300);

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
        sec1.getChildren().addAll(dayLabel,dayChooseTF,dayChooseBt);

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
        timerBarHBox.setPadding(new Insets(4,4,4,4));
        sec1.setSpacing(4);
        sec2.setSpacing(4);
        sec3.setSpacing(4);

        root.setTop(upperVBox);
        root.setLeft(leftVBox);
        root.setRight(rightVBox);

        // Canvas, everything needed to run the animation.
        Pane pane = new Pane();
        ResizableCanvas canvas = new ResizableCanvas();

        pane.getChildren().add(canvas);

        canvas.widthProperty().bind(
                pane.widthProperty());
        canvas.heightProperty().bind(
                pane.heightProperty());

        pane.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 20;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");

        canvas.setOnMousePressed((event) ->
        {
            // Get initial mouse loc on drag start
            CityNode.setMouseInitialX(event.getX());
            CityNode.setMouseInitialY(event.getY());
        });
        canvas.setOnMouseDragged((event) ->
        {
            // Calc distance moved by drag
            double movedX = event.getX() - CityNode.getMouseInitialX();
            double movedY = event.getY() - CityNode.getMouseInitialY();

            // Apply distance moved to all nodes loc
            CityNode.offsetAllCoordBy(movedX, movedY, true);

            // Update current mouse loc
            CityNode.setMouseInitialX(event.getX());
            CityNode.setMouseInitialY(event.getY());
        });

        // Colors and backgrounds
        upperHBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#F4BD98"), CornerRadii.EMPTY, Insets.EMPTY)));
        String background = "#F2E8DF";

        upperVBox.setBackground(new Background(new BackgroundFill(Color.valueOf(background), CornerRadii.EMPTY, Insets.EMPTY)));
        leftVBox.setBackground(new Background(new BackgroundFill(Color.valueOf(background), CornerRadii.EMPTY, Insets.EMPTY)));
        rightVBox.setBackground(new Background(new BackgroundFill(Color.valueOf(background), CornerRadii.EMPTY, Insets.EMPTY)));
        wrapperTimerBar.setBackground(new Background(new BackgroundFill(Color.valueOf(background), CornerRadii.EMPTY, Insets.EMPTY)));

        // Do some initialising
        GraphicsContext gc = canvas.getGraphicsContext2D();
        CityNode.initCoords(canvas, routeNetwork);

        VBox wrapperCenter = new VBox();
        wrapperCenter.getChildren().addAll(pane, wrapperTimerBar);
        root.setCenter(wrapperCenter);

        // Implement the time bar and corresponding buttons.

        // Set a time line to increment time variable.
        Timeline timelineTimer = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> {
                            changeTimeBy(1, currentDayTF);
                            updateInfoPanes(cityNameTF2, cityPopTF2, minEffDisTF2, PDPTF2, RMSTF2, caseIncidenceTF2, searchComboBox, routeNetwork, cityNameTF2R, setRestrictions2);
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timelineTimer.setCycleCount(Timeline.INDEFINITE);

        backButton.setOnAction(actionEvent -> {
            timelineTimer.pause();
            changeTimeBy(-1, currentDayTF);
            updateInfoPanes(cityNameTF2, cityPopTF2, minEffDisTF2, PDPTF2, RMSTF2, caseIncidenceTF2, searchComboBox, routeNetwork, cityNameTF2R, setRestrictions2);
        });
        forwardButton.setOnAction(actionEvent -> {
            timelineTimer.pause();
            changeTimeBy(1, currentDayTF);
            updateInfoPanes(cityNameTF2, cityPopTF2, minEffDisTF2, PDPTF2, RMSTF2, caseIncidenceTF2, searchComboBox, routeNetwork, cityNameTF2R, setRestrictions2);
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
        });

        // Implement the search bar and corresponding button.
        searchButton.setOnAction(actionEvent -> {
            updateInfoPanes(cityNameTF2, cityPopTF2, minEffDisTF2, PDPTF2, RMSTF2, caseIncidenceTF2, searchComboBox, routeNetwork, cityNameTF2R, setRestrictions2);
        });

        searchComboBox.getEditor().textProperty().addListener(keyEvent -> {
            searchComboBox.requestFocus();
            searchComboBox.getSelectionModel().clearSelection();
            String text = searchComboBox.getEditor().textProperty().getValue();
            List<String> foundCitiesList = CityNode.findLike(text);
            if(foundCitiesList == null) return;
            searchComboBox.getItems().setAll(foundCitiesList);
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Set a Time line to draw the animation in this thread
        Timeline timelineGraphics = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> drawSim(canvas.getGraphicsContext2D(), canvas, routeNetwork)
                ),
                new KeyFrame(Duration.millis(10))
        );
        timelineGraphics.setCycleCount(Timeline.INDEFINITE);
        timelineGraphics.play();

        // Set resizing scripts which keep the center target centered during window resizing
        canvas.widthProperty().addListener(evt -> {
            drawSim(gc, canvas, routeNetwork);
            CityNode.offsetAllCoordBy((CityNode.getCenterTarget().getxLoc() - canvas.prefWidth(0)/2)*-1,(CityNode.getCenterTarget().getyLoc() - canvas.prefHeight(0)/2)*-1,true);
        });
        canvas.heightProperty().addListener(evt -> {
            drawSim(gc, canvas, routeNetwork);
            CityNode.offsetAllCoordBy((CityNode.getCenterTarget().getxLoc() - canvas.prefWidth(0)/2)*-1,(CityNode.getCenterTarget().getyLoc() - canvas.prefHeight(0)/2)*-1,true);
        });
    }

    // Getters and setters for our global time variable
    private void changeTimeBy(int timeChange, TextField view) {
        int timetemp = this.time + timeChange;
        if(timetemp >= 0)
        {
            time += timeChange;
            view.setText(String.valueOf(time));
        }
    }
    private int getTime() {
        return time;
    }
    private void setTime(int time, TextField view) {
        if(time >= 0)
        {
            this.time = time;
            view.setText(String.valueOf(this.time));
        }
    }

    private void updateInfoPanes(TextField name, TextField pop, TextField effDis, TextField PDP, TextField RMS, TextField CI, ComboBox searchComboBox, RouteNetwork routeNetwork, TextField name2, TextField restrictions)
    {
        // Find city
        CityNode targetCity = CityNode.findByName((String) searchComboBox.getValue());
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
        name.setText(targetCity.getName());
        pop.setText(String.valueOf(targetCity.getCityPopulation()));
        effDis.setText(String.valueOf(routeNetwork.getMinEffDis(sourceCity, targetCity)));
        PDP.setText(String.valueOf(routeNetwork.getPDP(sourceCity, targetCity, time)));
        RMS.setText(String.valueOf(routeNetwork.getRMS(sourceCity, targetCity)));
        CI.setText(String.valueOf(targetCity.getCaseIncidenceEqn2(time)));

        // Alter:
        name2.setText(targetCity.getName());
        restrictions.setText("None"); // TODO need to complete this once mechanics are in
    }

    // This method will take a list of all nodes, calculate their position, and then draw them with appropriate colouring.
    public void drawSim(GraphicsContext gc, ResizableCanvas canvas, RouteNetwork routeNetwork) {
        try {
            // Clear canvas.
            gc.setFill(Color.valueOf("#F4BD98"));
            gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
            CityNode centerTarget = CityNode.getCenterTarget();

            if (centerTarget == null) return; // If no target is selected, don't draw anything.

            for (CityNode node : CityNode.getCityNodes()) // For each CityNode we have.
            {

                if (node.getName().equals(centerTarget.getName())) continue; // Don't handle the center node here.
                if(!node.isARouteTarget()) continue; // If no flights end at this node from source.

                gc.setFill(node.getPaint());
                gc.fillOval(node.getxLoc() - node.getSize() / 2, node.getyLoc() - node.getSize() / 2, node.getSize(), node.getSize());
            }

            // Draw the center node.
            gc.setFill(centerTarget.getPaint());
            gc.fillOval(centerTarget.getxLoc() - centerTarget.getSize() / 2, centerTarget.getyLoc() - centerTarget.getSize() / 2, centerTarget.getSize(), centerTarget.getSize());
        }catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
