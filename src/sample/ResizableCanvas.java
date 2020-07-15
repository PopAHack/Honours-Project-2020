package sample;

import javafx.scene.canvas.Canvas;

// This is a custom class used for resizing the canvas on window size change.
public class ResizableCanvas extends Canvas {

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
