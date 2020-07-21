package sample.Route;

import sample.CityNode;
import sample.Disease;

public abstract class Route {
    public abstract double getNEAP();
    public abstract double getEffDis();
    public abstract int getGumbelPrediction();
    public abstract CityNode getSourceCity();
    public abstract CityNode getTargetCity();
}
