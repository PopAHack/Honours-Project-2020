package sample.Route;

import sample.CityNode;

import java.util.List;

public class MultiPath extends Route {
    // A multipath is a series (typically 4/5) of CityNodes, linked by existing paths.

    // Global vars
    private CityNode sourceCity; // Start city.
    private CityNode targetCity; // End city.
    private List<Path> pathList; // List of paths making up this multipath.

    // Constructor
    public MultiPath(List<Path> pathList)
    {
        this.sourceCity = pathList.get(0).getSourceCity(); // Source of first path.
        this.targetCity = pathList.get(pathList.size()-1).getTargetCity(); // Target of last path.
        this.pathList = pathList;
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
    public double getTOAPrediction()
    {
        double pred = 0;
        for(Path path : pathList)
            pred += path.getTOAPrediction();

        return pred;
    }

    @Override
    public double getEffDis()
    {
        double effdis = 0;
        for(Path path : pathList)
            effdis += path.getEffDis();
        return effdis;
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
