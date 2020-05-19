package sample;

import java.util.List;

public class MultiPath extends Route {

    // Global vars
    private CityNode sourceCity; // Start city.
    private CityNode targetCity; // End city.
    private List<Path> pathList; // List of paths making up this multipath.
    private double dayOfArrival = 0;
    private Disease disease;

    // Constructor
    public MultiPath(List<Path> pathList, Disease disease)
    {
        this.disease = disease;
        this.sourceCity = pathList.get(0).getSourceCity(); // Source of first path.
        this.targetCity = pathList.get(pathList.size()-1).getTargetCity(); // Target of last path.
        this.pathList = pathList;
        this.dayOfArrival = pathList.get(pathList.size()-1).getDayOfArrival(); // Get last paths day of arrival.

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
    public double getMinEffDis()
    {
        double sumEffDis = 0;
        for(Path path : pathList)
        {
            sumEffDis += path.getMinEffDis();
        }
        return sumEffDis;
    }

    @Override
    public double getDayOfArrival()
    {
        return dayOfArrival;
    }

    @Override
    public double getNEAP() {
        double NEAP = 0;
        for (Path path : pathList)
            NEAP += path.getNEAP();
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
