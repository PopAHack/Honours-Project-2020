package sample.RouteNetwork;

import sample.CityNode;
import sample.Route.Path;
import sample.Route.Route;
import java.util.List;

// This class is used only in the Dijkstras shortest path algorithm, implemented in the RouteNetwork class.

public class Vertex {
    // Global Vars
    private double distance;
    private Vertex prevVertex;
    private CityNode city;
    private List<Route> routeList; // Routes leaving this city.
    private Path pathToThisCity;

    // Constructor
    public Vertex(double distance, Vertex prevVertix, CityNode cityNode, List<Route> routeList, Path path)
    {
        this.distance = distance;
        this.prevVertex = prevVertix;
        this.city = cityNode;
        this.routeList = routeList;
        this.pathToThisCity = path;
    }

    public Vertex copy()
    {
        Vertex vertex = new Vertex(distance, prevVertex, this.city, routeList, pathToThisCity);
        return vertex;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Vertex getPrevVertex() {
        return prevVertex;
    }

    public void setPrevVertex(Vertex prevVertex) {
        this.prevVertex = prevVertex;
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
