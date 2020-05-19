package sample;

public abstract class Route {
    private Disease disease;
    public abstract double getNEAP();
    public abstract CityNode getSourceCity();
    public abstract CityNode getTargetCity();
    public abstract double getDayOfArrival();
    public abstract double getMinEffDis();
}
