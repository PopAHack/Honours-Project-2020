package sample.Route;

import sample.CityNode;
import sample.Disease;
import sample.Route.Path;
import sample.Route.Route;

import java.util.List;

public class MultiPath extends Route {

    // Global vars
    private CityNode sourceCity; // Start city.
    private CityNode targetCity; // End city.
    private List<Path> pathList; // List of paths making up this multipath.
    private double dayOfArrival = 0;
    private Disease disease;
    private double transportDistance; // The number of passengers going from source to target cities.

    // Constructor
    public MultiPath(List<Path> pathList, Disease disease, double transportDistance)
    {
        this.disease = disease;
        this.sourceCity = pathList.get(0).getSourceCity(); // Source of first path.
        this.targetCity = pathList.get(pathList.size()-1).getTargetCity(); // Target of last path.
        this.pathList = pathList;
        this.dayOfArrival = pathList.get(pathList.size()-1).getDayOfArrival(); // Get last paths day of arrival.
        this.transportDistance = transportDistance;
    }

    public String getUniqueMultiPathName()
    {
        String name = "";
        for(Path path : pathList)
            name.concat(path.getSourceCity().getName() + path.getTargetCity().getName());
        return name;
    }

    public List<Path> getPathList() {
        return pathList;
    }

    public void setPathList(List<Path> pathList) {
        this.pathList = pathList;
    }

    @Override
    public double getEffDis()
    {
        double effectiveDistance;

        // Get the flux fraction
        double fluxFraction = transportDistance / sourceCity.getCityPopulation();
        if (fluxFraction == 0 || fluxFraction < 0 || Double.isInfinite(fluxFraction) || Double.isNaN(fluxFraction)) {
            effectiveDistance = -1; // < 0 implies infinite distance.
        }else {
            // Calculate effdis from city I to city J.
            effectiveDistance = 1 - Math.log(fluxFraction); // Math.log is in base e. Eqn: dm,n = 1 − ln Pm,n.
        }

        return effectiveDistance;
    }

    @Override
    public double getDayOfArrival()
    {
        return dayOfArrival;
    }

    @Override
    public double getNEAP() {
        double NEAP = 0;
        for (Path path : pathList) {
            if(NEAP == 0)NEAP = path.getNEAP();
            else NEAP *= path.getNEAP();
        }
        return NEAP;
    }

    @Override
    public CityNode getSourceCity() {
        return sourceCity;
    }

    @Override
    public CityNode getTargetCity() {
        return targetCity;
    }



}
