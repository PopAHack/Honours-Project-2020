package sample.RouteNetwork;

import sample.CityNode;
import sample.Disease;
import sample.Route.MultiPath;
import sample.Route.Path;
import sample.Route.Route;
import sample.RouteNetwork.RouteTreeNode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouteNetwork {

    // Global vars.
    //private List<List<Route>> routeMatrix = new ArrayList<>();

    // Constructor
    public RouteNetwork() {
    }

    // This takes a route, adds it to the route matrix if it doesn't already exist, otherwise adds each new paths transport distance to the existing paths.
    public void addRoute(Route route) {

        List<Route> routeList = RouteTreeNode.getRouteList(route.getSourceCity().getName() + route.getTargetCity().getName());

        if (routeList == null) {
            RouteTreeNode.addRoute(route); // If no routes exist yet, add and done.
            return;
        }

        // Add route transport
        if (route instanceof Path) {
            // Check the routes from source to target for the new route.
            for (Route multiRoute : routeList) {
                if (multiRoute instanceof Path) {
                    // This takes the existing route, adds the additional transport distance to it, and then discards the new route.
                    ((Path) multiRoute).addTransportDistance(((Path) route).getTransportDistance());
                    return; // Done.
                }
            }
        } else if (route instanceof MultiPath) {
            for (Route multiRoute : routeList) {
                // If the route already exists
                if (multiRoute instanceof MultiPath && ((MultiPath) multiRoute).getUniqueMultiPathName().equals(((MultiPath) route).getUniqueMultiPathName())
                && ((MultiPath) multiRoute).getPathList().size() == ((MultiPath) route).getPathList().size()) {
                    // Update each paths transport distance in the original multipath.  Discard new route.
                    for (int i = 0; i < ((MultiPath) multiRoute).getPathList().size(); i++) {
                        ((MultiPath) multiRoute).getPathList().get(i).addTransportDistance(((MultiPath) route).getPathList().get(i).getTransportDistance());
                    }
                    return;
                }
            }
        }
    }

    // Handles the search for each route leaving the source city.
    public List<Route> getRoutesFrom(CityNode sourceCity)
    {
        List<Route> routeList = new ArrayList<>();
        return RouteTreeNode.getTargetsFromSource(sourceCity);
    }

    // Find paths from source to target.
    public void generateMultipathsForTarget(CityNode target, RouteNetwork routeNetwork) {

        // Here, if we have already calculated the multipaths, simply return and end.
        List<Route> routeListExisting = getRoutesFrom(target);
        if(routeListExisting != null)
            for (Route route : routeListExisting)
                if (route instanceof MultiPath)
                    return;

        // Variables.
        int numTries = 10000; // A heuristic solution.
        int numMultipaths = 10; // We assume there are 10 paths to target.
        int numHops = 6; // Assume maximum number of hops is 6.
        List<MultiPath> multiPathsList = new ArrayList<>();
        Random rand = new Random(0);

        for(int i = 0; i <numTries; i++) { // For each multipath.
            if(multiPathsList.size() == numMultipaths) break;

            int numHopsTrue = rand.nextInt(numHops) + 1;
            CityNode prevCity = CityNode.getCenterTarget();
            List<Path> pathList = new ArrayList<>();
            List<CityNode> cityNodeList = new ArrayList<>();
            cityNodeList.add(CityNode.getCenterTarget());

            // We are going through each hop, randomly selecting the next Path, while not circling or returning to past cities.
            for (int j = 0; j < numHopsTrue - 1; j++) {
                List<Route> routeList = routeNetwork.getRoutesFrom(prevCity);
                if(routeList == null || routeList.size() == 0)break;
                Path path;
                if(routeList.size() == 1)
                    path = (Path) routeList.get(0);
                else
                    path = (Path) routeList.get(rand.nextInt(routeList.size() - 1));
                while (pathList.contains(path) && cityNodeList.contains(path.getTargetCity()))
                    path = (Path) routeList.get(rand.nextInt(routeList.size() - 1));
                pathList.add(path);
                cityNodeList.add(path.getTargetCity());
                prevCity = pathList.get(pathList.size() - 1).getTargetCity();
            }

            // On last hop, look for our target city.  If not found in routelist, discard and try again.
            List<Route> routeList = routeNetwork.getRoutesFrom(prevCity);
            if(routeList == null || routeList.size() == 0) continue;
            for (Route route : routeList) {
                if (route.getTargetCity().equals(target.getName())) {
                    pathList.add((Path) route);
                    MultiPath multiPath = new MultiPath(pathList);
                    routeNetwork.addRoute(multiPath);
                }
            }
        }
    }

    // Get the smallest effective distance from all routes.
    public double getMinEffDis(CityNode sourceCity, CityNode targetCity) {
        List<Route> routeList = RouteTreeNode.getRouteList(sourceCity.getName() + targetCity.getName());
        if (routeList == null) return -1; // No routes between.
        try {
            double minEffDis = -1;
            for(Route route : routeList) {
                if (minEffDis == -1)
                    minEffDis = route.getEffDis();
                else if (minEffDis > route.getEffDis())
                    minEffDis = route.getEffDis();
            }

            return minEffDis;
        } catch (Exception ex) {
            System.out.println("Error in getMaxEffDis()");
            return -1;
        }
    }

    // Get the RMS between many routes to the target city.
    // TODO Not working, needs fixing.
    public double getRMS(CityNode sourceCity, CityNode targetCity, int time) {
        return -1;
//        List<Route> routeList = RouteTreeNode.getRouteList(sourceCity.getName() + targetCity.getName());
//        if (routeList == null) return -1; // Error handling, no routes between cities.
//        try {
//            double mean;
//            double mean1 = 0;
//            double neapSum = 0;
//            double RMS;
//            double rms1 = 0;
//
//            for (Route route : routeList) {
//                mean1 += route.getNEAP() * route.getDayOfArrival();
//                neapSum += route.getNEAP();
//                rms1 += route.getNEAP() * route.getDayOfArrival() * route.getDayOfArrival();
//            }
//            mean = mean1 / neapSum;
//            neapSum -= mean * mean;
//
//            RMS = Math.sqrt(rms1 / neapSum);
//            return RMS;
//
//        } catch (Exception ex) {
//            System.out.println("Math error during RMS calc.");
//            return -1;
//        }
    }

    // Returns the PDP between many routes to target city at a particular time.
    public double getPDP(CityNode sourceCity, CityNode targetCity, int time) {
        double PDP = 0;
        List<Route> routeList = RouteTreeNode.getRouteList(sourceCity.getName() + targetCity.getName());
        if (routeList == null) return -1; // Error handling, no routes between cities.
        try {
            // Calculate PDP:
            for (Route route : routeList) { // For each path.

                int predictedDay = route.getGumbelPrediction(); // Get the rounded down effective distance of that path.
                if (time == predictedDay) { // If the current time is equal to the effective distance value.
                    PDP = route.getNEAP(); // Then the PDP is equal to the NEAP value at this time.
                }
            }
            return PDP;
        } catch (Exception ex) {
            System.out.println("Math error during PDP calc.");
            return -1;
        }
    }
}
