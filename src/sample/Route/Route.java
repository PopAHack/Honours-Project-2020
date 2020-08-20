package sample.Route;

import sample.CityNode;

public abstract class Route {
    public abstract double getNEAP();
    public abstract double getEffDis();
    public abstract double getTOAPrediction();
    public abstract CityNode getSourceCity();
    public abstract CityNode getTargetCity();
}
