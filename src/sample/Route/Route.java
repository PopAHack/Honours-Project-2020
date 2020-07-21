package sample.Route;

import sample.CityNode;
import sample.Disease;

public abstract class Route {
    private Disease disease;
    public abstract double getNEAP();
    public abstract CityNode getSourceCity();
    public abstract CityNode getTargetCity();
    public abstract double getDayOfArrival();
    public abstract double getEffDis();
}
