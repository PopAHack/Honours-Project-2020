package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import sample.Route.Route;
import sample.RouteNetwork.RouteNetwork;
import sample.RouteNetwork.RouteTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CityNode {

    // Static properties
    private static List<CityNode> cityNodeList = new ArrayList<>();
    private static CityNode centerTarget; // The node that the eff dis is in ref to.
    private static CityNode selectedTarget; // The currently selected node.
    private static double vectorLengthScalar = 25; // Recommended: 25
    private static double vectorLengthAdder = -5; // Recommended: -5
    private static Boolean initialised = false;
    private static double mouseInitialX;
    private static double mouseInitialY;
    private static Boolean locMovement = false; // This tells us if an update to any node has occurred and that it needs to be redrawn.
    private static CityNode currentlySelectedNode;
    private static Random rand = new Random();

    // Global vars.
    private String name;
    private double cityPopulation;
    private double xLoc = 0;
    private double yLoc = 0;
    private double size = 10;
    private Paint paint = Color.BLACK;
    private Disease disease;
    private Boolean isARouteTarget = false;
    private List<Double> CIEqn = new ArrayList<>();

    // Constructor.
    public CityNode(String name, int cityPopulation, Disease disease)
    {
        this.name = name;

        this.cityPopulation = cityPopulation;
        cityNodeList.add(this);
        this.disease = disease;
    }

    // Plots the case incidence eqn.
    public void plotCIGraph()
    {
        List<Route> routeList = RouteTreeNode.getRouteList(CityNode.getCenterTarget().getName() + this.getName());
        List<Route> routeListUnsorted = new ArrayList<>();
        List<Route> routeListSorted = new ArrayList<>();

        if(routeList == null) return; // No paths to this target, so no infections.

        for(Route route : routeList)
            routeListUnsorted.add(route);

        int size = routeListUnsorted.size();
        Route smallest;

        // Arrange in ascending order.
        for(int i = 0; i < size; i++)
        {
            smallest = routeListUnsorted.get(0);
            for(Route route : routeListUnsorted)
            {
                if(route.getTOAPrediction() < smallest.getTOAPrediction())
                {
                    smallest = route;
                }
            }
            routeListSorted.add(smallest);
            routeListUnsorted.remove(smallest);
        }

        List<Double> points = new ArrayList<>();

        // For each day that the disease is active.
        for(int time = 0; time < routeListSorted.get(routeListSorted.size()-1).getTOAPrediction() + disease.getTbTime(); time++)
        {
            double caseIncidence = 0;
            for(Route route : routeListSorted)
            {
                if(time < route.getTOAPrediction()) // If the disease has not yet arrived.
                    continue;
                caseIncidence += disease.getCaseIncidenceEqn2((int)(time - route.getTOAPrediction()));
            }
            points.add(caseIncidence);
        }
        CIEqn = points;
    }

    public double[] getArrivalTimes()
    {
        List<Route> routeList = RouteTreeNode.getRouteList(CityNode.getCenterTarget().getName() + this.getName());
        List<Route> routeListUnsorted = new ArrayList<>();
        List<Route> routeListSorted = new ArrayList<>();

        if(routeList == null) return null; // No paths to this target, so no infections.

        for(Route route : routeList)
            routeListUnsorted.add(route);

        int size = routeListUnsorted.size();
        Route smallest;

        // Arrange in ascending order.
        for(int i = 0; i < size; i++)
        {
            smallest = routeListUnsorted.get(0);
            for(Route route : routeListUnsorted)
            {
                if(route.getTOAPrediction() < smallest.getTOAPrediction())
                {
                    smallest = route;
                }
            }
            routeListSorted.add(smallest);
            routeListUnsorted.remove(smallest);
        }

        double[] returnList = new double[routeListSorted.size()];
        for(int i = 0; i < returnList.length; i++)
            returnList[i] = routeListSorted.get(i).getTOAPrediction();
        return returnList;
    }

    // Getters and Setters

    // Get the cities name.
    public String getName() {
        return name;
    }

    public double getxLoc() {
        return xLoc;
    }

    public Boolean isARouteTarget() {
        return isARouteTarget;
    }

    public void setIsARouteTarget(Boolean ARouteTarget) {
        isARouteTarget = ARouteTarget;
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

    public double getSize() {
        return size;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public List<Double> getCIEqn() {
        return CIEqn;
    }

    // Static methods
    public static CityNode getCurrentlySelectedNode() {
        return currentlySelectedNode;
    }

    public static void setCurrentlySelectedNode(CityNode currentlySelectedNode) {
        CityNode.currentlySelectedNode = currentlySelectedNode;
    }

    public static CityNode get(int index) {
        return cityNodeList.get(index);
    }

    public static List<CityNode> getCityNodes() {
        return cityNodeList;
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

    public static CityNode getCenterTarget() {
        return (centerTarget == null) ?  null :  centerTarget;
    }

    public static void setCenterTarget(CityNode node){
        centerTarget = node;
    }

    // Moves every object in the list by the same amount in x,y space.
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
            if(node.getName().equals(centerTarget.getName())) continue;
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

    // Call this method after all city nodes have been created and their initial coordinates can be calculated.
    // This method initialises the city node coordinates, spreading them out randomly and drawing the distance from
    // the center with respect to the effective distance.  Distance is exaggerated by static variables.
    public static void initCoords(Canvas canvas, RouteNetwork network)
    {
        double centerTargetX = canvas.prefWidth(0)/2;
        double centerTargetY = canvas.prefHeight(0)/2;

        for(CityNode node : cityNodeList)
        {
            if(node.name.equals(getCenterTarget().getName())) continue;
            if(!node.isARouteTarget) continue;
            double r = (network.getMinEffDis(CityNode.getCenterTarget(), node) + vectorLengthAdder)*vectorLengthScalar; // Known variable.  Final length of vector.
            double theta = rand.nextDouble()*360; // Known variable.  Angle of vector.
            double x; // Unknown variable. x location.
            double y; // Unknown variable. y Location.

            // If r < 0, then there is infinite distance, so we won't draw it, as there is no contact between them.
            if(r < 0) continue;

            // Conversion from polar to Cartesian coordinates.
            y = r*Math.cos(theta);
            x = r*Math.sin(theta);

//            // Adjust positives and negatives according to the angle.
//            if(theta < 90) {
//                y *= -1;
//                x *= 1;
//            }else if(theta < 180) {
//                y *= -1;
//                x *= -1;
//            }else if(theta < 270) {
//                y *= 1;
//                x *= -1;
//            }else if(theta <= 360) {
//                y *= 1;
//                x *= 1;
//            }

            // Slide over, so it orbits the center node.
            x += centerTargetX;
            y += centerTargetY;

            // Set coords.
            node.setxLoc(x);
            node.setyLoc(y);
        }
        centerTarget.setxLoc(centerTargetX);
        centerTarget.setyLoc(centerTargetY);

        initialised = true;
        locMovement = true;
    }

    // Finds a city node by its name attribute.
    // Also looks at the center target.
    // Returns null if city not found.
    // Assumes unique names.
    // @param name: The name of the city node to return.
    public static CityNode findByName(String name)
    {
        for(int i =0; i < cityNodeList.size(); i++) {
            if (cityNodeList.get(i).getName().equalsIgnoreCase(name))
                return cityNodeList.get(i);
        }
        if(centerTarget.getName().equals(name))return centerTarget;
        return null;
    }

    // Returns a list of city nodes containing the text in their name property.
    //@param text: The text to look for city nodes containing (ignoreCase) in their name properties.
    public static List<String> findLike(String text)
    {
        List<String> returnList = new ArrayList<>();
        if(text==null) return null;
        for(int i = 0; i < cityNodeList.size(); i++)
        {
            if(cityNodeList.get(i).getName().equals(centerTarget.getName())) continue; // Skip the target city.
            if(cityNodeList.get(i).getName().toLowerCase().contains(text.toLowerCase())) // Ignore case.
            {
                if(cityNodeList.get(i).isARouteTarget()) {
                    List<Route> routeList = RouteTreeNode.getRouteList(CityNode.getCenterTarget().getName() + cityNodeList.get(i).getName());

                    Route routeSmallest = null;
                    for(Route route : routeList)
                        if(routeSmallest == null) routeSmallest = route;
                        else if(routeSmallest.getEffDis()>route.getEffDis())
                            routeSmallest = route;

                    returnList.add(cityNodeList.get(i).getName() + " Eff_Dis: " + routeSmallest.getEffDis());
                }
            }
        }
        return returnList;
    }
}
