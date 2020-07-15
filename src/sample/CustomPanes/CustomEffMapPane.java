package sample.CustomPanes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sample.CityNode;
import sample.ResizableCanvas;
import sample.RouteNetwork.RouteNetwork;

public class CustomEffMapPane extends Pane {
    // Global vars.
    private ResizableCanvas canvas = new ResizableCanvas();

    // Constructor
    public CustomEffMapPane()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Canvas, everything needed to run the animation.

        this.getChildren().add(canvas);

        canvas.widthProperty().bind(
                this.widthProperty());
        canvas.heightProperty().bind(
                this.heightProperty());

        this.setStyle("-fx-padding: 10;" +
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

        // Set a Time line to draw the animation in this thread
        Timeline timelineGraphics = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> drawSim(canvas.getGraphicsContext2D(), canvas)
                ),
                new KeyFrame(Duration.millis(10))
        );
        timelineGraphics.setCycleCount(Timeline.INDEFINITE);
        timelineGraphics.play();

        // Set resizing scripts which keep the center target centered during window resizing
        canvas.widthProperty().addListener(evt -> {
            drawSim(gc, canvas);
            CityNode.offsetAllCoordBy((CityNode.getCenterTarget().getxLoc() - canvas.prefWidth(0)/2)*-1,(CityNode.getCenterTarget().getyLoc() - canvas.prefHeight(0)/2)*-1,true);
        });
        canvas.heightProperty().addListener(evt -> {
            drawSim(gc, canvas);
            CityNode.offsetAllCoordBy((CityNode.getCenterTarget().getxLoc() - canvas.prefWidth(0)/2)*-1,(CityNode.getCenterTarget().getyLoc() - canvas.prefHeight(0)/2)*-1,true);
        });
    }

    // Methods
    // This method will take a list of all nodes, calculate their position, and then draw them with appropriate colouring.
    public void drawSim(GraphicsContext gc, ResizableCanvas canvas) {
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




    // Getters and Setters.
    public ResizableCanvas getCanvas() {
        return canvas;
    }
}
