package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CityNode {

    // Static properties
    private static List<CityNode> cityNodeList = new ArrayList<>();
    private static CityNode centerTarget; // The node that the eff dis is in ref to
    private static CityNode selectedTarget; // The currently selected node
    private static double vectorLengthScalar = 40;
    private static double vectorLengthAdder = -7;
    private static Boolean initialised = false;
    private static double mouseInitialX;
    private static double mouseInitialY;
    private static Boolean locMovement = false; // This tells us if an update to any node has occurred and that it needs to be redrawn.

    // Global vars.
    private String name;
    private int indexInMatrix;
    private double cityPopulation;
    private double xLoc = 0;
    private double yLoc = 0;
    private double size = 10;
    private Paint paint = Color.BLACK;
    private double r2;
    private double theta2;

    // Constructor.
    public CityNode(String name, int index, int cityPopulation)
    {
        this.name = name;
        indexInMatrix = index;
        this.cityPopulation = cityPopulation;
        cityNodeList.add(this);

    }

    // Getters and Setters

    // Get the cities name.
    public String getName() {
        return name;
    }

    public double getxLoc() {
        return xLoc;
    }

    public void setxLoc(double xLoc) {
        this.xLoc = xLoc;
        locMovement=true;
    }

    public double getyLoc() {
        return yLoc;
    }

    public void setyLoc(double yLoc) {
        this.yLoc = yLoc;
        locMovement=true;
    }

    public double getCityPopulation() {
        return cityPopulation;
    }

    public void setCityPopulation(double cityPopulation) {
        this.cityPopulation = cityPopulation;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getIndexInMatrix()
    {
        return indexInMatrix;
    }

    // Static methods
    public static CityNode get(int index) {
        return cityNodeList.get(index);
    }

    public static void set(int index, CityNode node) {
        CityNode.cityNodeList.set(index, node);
    }

    public static double getVectorLengthAdder() {
        return vectorLengthAdder;
    }

    public static void setVectorLengthAdder(double vectorLengthAdder) {
        CityNode.vectorLengthAdder = vectorLengthAdder;
    }

    public static Boolean getLocMovement() {
        return locMovement;
    }

    public static void setLocMovement(Boolean locMovement) {
        CityNode.locMovement = locMovement;
    }

    public static double getMouseInitialX() {
        return mouseInitialX;
    }

    public static void setMouseInitialX(double mouseInitialX) {
        CityNode.mouseInitialX = mouseInitialX;
    }

    public static double getMouseInitialY() {
        return mouseInitialY;
    }

    public static void setMouseInitialY(double mouseInitialY) {
        CityNode.mouseInitialY = mouseInitialY;
    }

    public static int numOfNodes() {
        return cityNodeList.size();
    }

    public static CityNode getCenterTarget() {
        return (centerTarget == null) ?  null :  centerTarget;
    }

    public static CityNode getSelectedTarget() {
        return (selectedTarget == null) ? null : selectedTarget;
    }

    public static void setSelectedTarget(CityNode node) {
        selectedTarget = node;
    }

    public static void setCenterTarget(CityNode node){
        centerTarget = node;
    }

    public static void setVectorLengthScalar(double vectorLengthScalar) {
        CityNode.vectorLengthScalar = vectorLengthScalar;
    }

    public static Boolean getInitialised() {
        return initialised;
    }

    public static void setInitialised(Boolean initialised) {
        CityNode.initialised = initialised;
    }

    // Moves every object in the list by the same amount in x,y space
    // If moveCenter is true, it will also move the center node
    // @param x: The distance to move all in the x direction.
    // @param y: The distance to move all in the y direction.
    // @param moveCenter: Should the center city move as well?
    public static void offsetAllCoordBy(double x, double y, Boolean moveCenter)
    {
        if(!initialised)
        {
            System.out.println("Error, CityNodes not initialised.");
            return;
        }
        for(CityNode node : cityNodeList)
        {
            node.setxLoc(node.getxLoc() + x);
            node.setyLoc(node.getyLoc() + y);
        }
        if(moveCenter)
        {
            centerTarget.setxLoc(centerTarget.getxLoc() + x);
            centerTarget.setyLoc(centerTarget.getyLoc() + y);
        }
        locMovement=true;
    }

    // Call this method after all city nodes have been created and their initial coordinates can be calculated
    // This method initialises the city node coordinates, spreading them out randomly and drawing the distance from
    // the center with respect to the effective distance.  Distance is exaggerated by static variables.
    public static void initCoords(Canvas canvas)
    {
        Random rand = new Random();

        double centerTargetX = canvas.prefWidth(0)/2;
        double centerTargetY = canvas.prefHeight(0)/2;

        for(CityNode node : cityNodeList)
        {
            if(node.indexInMatrix == getCenterTarget().indexInMatrix) continue;
            double r = (DistanceDataCollector.getEffDisMatrix().get(CityNode.getCenterTarget().getIndexInMatrix(), node.indexInMatrix) + vectorLengthAdder)*vectorLengthScalar; // Known variable.  Final length of vector.
            double theta = rand.nextDouble()*360; // Known variable.  Angle of vector.
            double x; // Unknown variable. x location.
            double y; // Unknown variable. y Location.

            // If r < 0, then there is infinite distance, so we won't draw it, as there is no contact between them.
            if(r < 0) continue;

            // Conversion from polar to Cartesian coordinates.
            y = Math.sqrt(((r*r)*(Math.tan(theta)*Math.tan(theta))) / (1 + (Math.tan(theta)*Math.tan(theta))));
            x = Math.sqrt((r*r) - (y*y));

            // Adjust positives and negatives according to the angle
            if(theta < 90)
            {
                y *= -1;
                x *= 1;
            }else if(theta < 180)
            {
                y *= -1;
                x *= -1;
            }else if(theta < 270)
            {
                y *= 1;
                x *= -1;
            }else if(theta <= 360)
            {
                y *= 1;
                x *= 1;
            }

            // Slide over, so it orbits the center node.
            x += centerTargetX;
            y += centerTargetY;

            // Set coords.
            node.setxLoc(x);
            node.setyLoc(y);

            // Implies infinite eff distance
            if(node.getxLoc() ==0 && node.getyLoc()==0)
                DistanceDataCollector.getEffDisMatrix().set(CityNode.getCenterTarget().getIndexInMatrix(), node.indexInMatrix, -1);

        }
        centerTarget.setxLoc(centerTargetX);
        centerTarget.setyLoc(centerTargetY);

        initialised = true;
        locMovement = true;
    }

    // Finds a city node by its name attribute
    // Also looks at the center target
    // Returns null if city not found
    // Assumes unique names
    // @param name: The name of the city node to return
    public static CityNode findByName(String name)
    {
        for(int i =0; i < cityNodeList.size(); i++) {
            if (cityNodeList.get(i).getName().equals(name))
                return cityNodeList.get(i);
        }
        if(centerTarget.getName().equals(name))return centerTarget;
        return null;
    }

    //Returns a list of city nodes containing the text in their name property.
    //@param text: The text to look for city nodes containing in their name properties.
    public static List<String> findLike(String text)
    {
        List<String> returnList = new ArrayList<>();
        if(text==null) return null;
        for(int i = 0; i < cityNodeList.size(); i++)
        {
            if(cityNodeList.get(i).getName().compareToIgnoreCase(text) == 0)
            {
                returnList.add(cityNodeList.get(i).getName());
            }
        }
        return returnList;
    }
}
