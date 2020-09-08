package sample.CustomPanes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sample.CityNode;

import java.util.List;

public class CustomGraphPane extends Pane {
    // Global Vars.
    private ResizableCanvas canvas = new ResizableCanvas();
    private int horizontalInitialDay = 0;
    private int horizontalInitialDayLast = -1;

    // Constructor.
    public CustomGraphPane() {
        // Canvas, everything needed to run the animation.
        GraphicsContext gc = canvas.getGraphicsContext2D();

        this.getChildren().add(canvas);

        canvas.setOnScroll(event -> {
            horizontalInitialDay -= event.getDeltaY()/20;
            if(horizontalInitialDay < 0) horizontalInitialDay = 0;
        });

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

        // Set a Time line to draw the animation in this thread
        Timeline timelineGraphics = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> drawCanvas(canvas.getGraphicsContext2D())
                ),
                new KeyFrame(Duration.millis(50))
        );
        timelineGraphics.setCycleCount(Timeline.INDEFINITE);
        timelineGraphics.play();

        // Set resizing scripts which keep the center target centered during window resizing
        canvas.widthProperty().addListener(evt -> {
            drawCanvas(gc);
            CityNode.offsetAllCoordBy((CityNode.getCenterTarget().getxLoc() - canvas.prefWidth(0) / 2) * -1, (CityNode.getCenterTarget().getyLoc() - canvas.prefHeight(0) / 2) * -1, true);
        });
        canvas.heightProperty().addListener(evt -> {
            drawCanvas(gc);
            CityNode.offsetAllCoordBy((CityNode.getCenterTarget().getxLoc() - canvas.prefWidth(0) / 2) * -1, (CityNode.getCenterTarget().getyLoc() - canvas.prefHeight(0) / 2) * -1, true);
        });
    }

    // Methods.
    private void drawCanvas(GraphicsContext gc) {
        try {
            CityNode targetCity = CityNode.getCurrentlySelectedNode();
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            int axisOffset = 75;

            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
            gc.strokeLine(0 + axisOffset, 0, 0 + axisOffset, canvas.getHeight()); // Vert axis.
            gc.strokeLine(0, canvas.getHeight() - axisOffset, canvas.getWidth(), canvas.getHeight() - axisOffset); // Horiz axis.

            // Set the vertival axis values.
            if(targetCity != null) {
                // Set Vertical axis values.
                int numberOfVertVals = 10;
                double incrementHeight = (canvas.getHeight() - axisOffset) / numberOfVertVals;
                double yLoc = canvas.getHeight() - axisOffset;
                for (int i = 0; i < numberOfVertVals; i++) {
                    yLoc -= incrementHeight;
                    String text = String.valueOf((int)(((targetCity.getCityPopulation() / (double) numberOfVertVals) * (i + 1))));
                    gc.setLineWidth(0.6);
                    gc.strokeText(text, 5, yLoc + gc.getLineWidth() * 18);
                    gc.setLineWidth(3);
                    gc.strokeLine(axisOffset - 5, yLoc, axisOffset + 5, yLoc);
                }
                // Set horizontal axis values.
                int numberOfHorizVals = 25;
                double incrementWidth = (canvas.getWidth() - axisOffset) / numberOfHorizVals;
                double xLoc;
                for(int i = 0; i < numberOfHorizVals; i++)
                {
                    xLoc = axisOffset + incrementWidth*(i+1);
                    gc.setLineWidth(0.6);
                    gc.strokeText(String.valueOf(i+1 + horizontalInitialDay), xLoc - 6, canvas.getHeight() - 6);
                    gc.strokeLine(xLoc, canvas.getHeight() - axisOffset - 5, xLoc, canvas.getHeight() - axisOffset + 5);
                }

                // Now draw the graph.
                gc.setLineWidth(3);
                gc.setStroke(Color.RED);
                List<Double> points = targetCity.getCIEqn();

                for(int i = 1; i < points.size(); i++)
                {
                    if((i - 1 - horizontalInitialDay)*incrementWidth + axisOffset < axisOffset) continue;

                    double yVal1 = canvas.getHeight() - axisOffset - ((points.get(i-1)/targetCity.getCityPopulation()) * (canvas.getHeight() - axisOffset));
                    double yVal2 = canvas.getHeight() - axisOffset - ((points.get(i)/targetCity.getCityPopulation()) * (canvas.getHeight() - axisOffset));

                    gc.strokeLine((i - 1 - horizontalInitialDay)*incrementWidth + axisOffset, yVal1, (i - horizontalInitialDay)*incrementWidth + axisOffset, yVal2);
                }

                double[] arrivalTimesArray = targetCity.getArrivalTimes();
                gc.setStroke(Color.LAWNGREEN);
                gc.setLineWidth(2);
                for(int i = 0; i < arrivalTimesArray.length; i++)
                {
                    if((arrivalTimesArray[i] - horizontalInitialDay)*incrementWidth + axisOffset < axisOffset) continue;
                    gc.strokeText("W" + (i+1), (arrivalTimesArray[i] - horizontalInitialDay)*incrementWidth + axisOffset, canvas.getHeight() - axisOffset + 20);
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
