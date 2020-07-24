package sample.RouteNetwork;

import javafx.scene.layout.Pane;
import sample.CityNode;
import sample.Route.Path;
import sample.Route.Route;

import java.util.List;

public class Vertix {
    // Global Vars
    private double distance;
    private Vertix prevVertix;
    private CityNode city;
    private List<Route> routeList; // Routes leaving this city.
    private Path pathToThisCity;

    // Constructor
    public Vertix(double distance, Vertix prevVertix, CityNode cityNode, List<Route> routeList, Path path)
    {
        this.distance = distance;
        this.prevVertix = prevVertix;
        this.city = cityNode;
        this.routeList = routeList;
        this.pathToThisCity = path;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Vertix getPrevVertix() {
        return prevVertix;
    }

    public void setPrevVertix(Vertix prevVertix) {
        this.prevVertix = prevVertix;
    }

    public CityNode getCity() {
        return city;
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public Path getPathToThisCity() {
        return pathToThisCity;
    }

    public void setPathToThisCity(Path pathToThisCity) {
        this.pathToThisCity = pathToThisCity;
    }
}
