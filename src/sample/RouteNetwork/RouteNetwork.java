package sample.RouteNetwork;

import sample.CityNode;
import sample.Route.MultiPath;
import sample.Route.Path;
import sample.Route.Route;
import sample.RouteNetwork.RouteTreeNode;

import java.util.List;

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

    // Get the smallest effective distance from all routes
    public double getMinEffDis(CityNode sourceCity, CityNode targetCity) {
        List<Route> routeList = RouteTreeNode.getRouteList(sourceCity.getName() + targetCity.getName());
        if (routeList == null) return -1; // No routes between.
        try {
            double minEffDis = routeList.get(0).getEffDis();
            for (Route route : routeList)
                if (minEffDis > route.getEffDis())
                    minEffDis = route.getEffDis();
            return minEffDis;
        } catch (Exception ex) {
            System.out.println("Error in getMaxEffDis()");
            return -1;
        }
    }

    // Get the RMS between many routes to the target city.
    // TODO Not working, needs fixing.
    public double getRMS(CityNode sourceCity, CityNode targetCity, int time) {

        List<Route> routeList = RouteTreeNode.getRouteList(sourceCity.getName() + targetCity.getName());
        if (routeList == null) return -1; // Error handling, no routes between cities.
        try {
            double mean;
            double mean1 = 0;
            double neapSum = 0;
            double RMS;
            double rms1 = 0;

            for (Route route : routeList) {
                mean1 += route.getNEAP() * route.getDayOfArrival();
                neapSum += route.getNEAP();
                rms1 += route.getNEAP() * route.getDayOfArrival() * route.getDayOfArrival();
            }
            mean = mean1 / neapSum;
            neapSum -= mean * mean;

            RMS = Math.sqrt(rms1 / neapSum);
            return RMS;

        } catch (Exception ex) {
            System.out.println("Math error during RMS calc.");
            return -1;
        }
    }

    // Returns the PDP between many routes to target city at a particular time.
    public double getPDP(CityNode sourceCity, CityNode targetCity, int time) {
        double PDP = 0;
        List<Route> routeList = RouteTreeNode.getRouteList(sourceCity.getName() + targetCity.getName());
        if (routeList == null) return -1; // Error handling, no routes between cities.
        try {
            // Calculate PDP:
            for (Route route : routeList) { // For each path.
                int effdisK = (int) route.getEffDis(); // Get the rounded down effective distance of that path.
                if (time == effdisK) { // If the current time is equal to the effective distance value.
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
