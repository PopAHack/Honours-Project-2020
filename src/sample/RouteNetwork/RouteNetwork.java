package sample.RouteNetwork;

import sample.CityNode;
import sample.Route.MultiPath;
import sample.Route.Path;
import sample.Route.Route;

import java.util.ArrayList;
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
            for (Route path : routeList) {
                if (path instanceof Path) {
                    if((path.getSourceCity().getName() + path.getTargetCity().getName()).equals(route.getSourceCity().getName() + route.getTargetCity().getName())) {
                        // This takes the existing route, adds the additional transport distance to it, and then discards the new route.
                        ((Path) path).addTransportDistance(((Path) route).getTransportDistance());
                        return; // Done.
                    }
                }
            }
            // This adds the new route if it doesn't already exist.
            routeList.add(route);

        } else if (route instanceof MultiPath) {
            for (Route multiRoute : routeList) {
                if (multiRoute instanceof MultiPath) {
                    if (((MultiPath) multiRoute).getUniqueMultiPathName().equals(((MultiPath) route).getUniqueMultiPathName())) {
                        System.out.println("Error in route add method.  Multipath already exists.");
                        return;
                    }
                }
            }
            routeList.add(route);
        }
    }

    // Handles the search for each route leaving the source city.
    public List<Route> getRoutesFrom(CityNode sourceCity)
    {
        List<Route> routeList = new ArrayList<>();
        return RouteTreeNode.getTargetsFromSource(sourceCity);
    }


    // Apply a variation of Dijkstra's algorithm.
    public void generateMultipathsForTarget(CityNode target, RouteNetwork routeNetwork) {
        // If we have already calculated the paths between these cities, return.
        List<Route> sampleList = RouteTreeNode.getRouteList(CityNode.getCenterTarget().getName() + target.getName());
        for (Route route : sampleList)
            if (route instanceof MultiPath)
                return;

        List<Vertex> vertices = new ArrayList<>();
        List<Vertex> verticesPerm = new ArrayList<>();
        Vertex targetVertex = null;
        Vertex targetVertexPerm = null;
        Vertex sourceVertexPerm = null;
        int numCities = 1200;
        int numPaths = 6;

        // Init vertices.
        List<CityNode> cityNodeList = CityNode.getCityNodes();
        for (int i = 0; i < numCities - 1; i++) {
            Vertex vertex = new Vertex(100000, null, cityNodeList.get(i), routeNetwork.getRoutesFrom(cityNodeList.get(i)), null);
            Vertex vertexPerm = vertex.copy();
            vertices.add(vertex);
            verticesPerm.add(vertexPerm);

            // Keep the target vertex for later ref.
            if (cityNodeList.get(i).getName().equals(target.getName())) {
                targetVertex = vertex;
                targetVertexPerm = targetVertex.copy();
            }
        }

        // Set source distance to 0.
        Vertex sourceVertex = new Vertex(0, null, CityNode.getCenterTarget(), routeNetwork.getRoutesFrom(CityNode.getCenterTarget()), null);
        vertices.add(sourceVertex);
        verticesPerm.add(sourceVertex.copy());
        sourceVertexPerm = sourceVertex.copy();

        for (int k = 0; k < numPaths; k++) {
            if(k != 0)
            {
                // Re init the vertices list with fresh copies of our vertices.
                vertices.clear();
                for(int i = 0; i < numCities - 1; i++)
                    vertices.add(verticesPerm.get(i).copy());
                targetVertex = targetVertexPerm.copy();
                vertices.add(sourceVertexPerm.copy());
            }

            while (vertices.size() != 0) {
                Vertex u = null;
                for (Vertex vertex : vertices) {
                    if (u == null)
                        u = vertex;
                    else if (vertex.getDistance() < u.getDistance())
                        u = vertex;
                }
                vertices.remove(u);

                if (u.getCity().getName().equals(target.getName())) // Break if we have found the target.
                    break;

                if (u.getRouteList() != null && u.getRouteList().size() != 0) { // If it is not an end node.
                    for (Route route : u.getRouteList()) {
                        if (route instanceof Path) {
                            double alt = u.getDistance() + route.getEffDis();
                            Vertex v = null;
                            for (Vertex v1 : vertices) {
                                if (v1.getCity().getName().equals(route.getTargetCity().getName()))
                                    v = v1;
                            }
                            if (v != null && alt < v.getDistance()) {
                                v.setDistance(alt);
                                v.setPrevVertex(u);
                                v.setPathToThisCity((Path) route);
                            }
                        }
                    }
                }
            }

            // Now trace back the shortest path.
            List<Vertex> S = new ArrayList<>();
            List<Path> pathList = new ArrayList<>();
            Vertex u = targetVertex;
            if (u.getPrevVertex() != null || u.getCity().getName().equals(CityNode.getCenterTarget().getName())) {
                while (u != null) {
                    S.add(0, u);
                    if (u.getPathToThisCity() != null) {
                        pathList.add(0, u.getPathToThisCity());

                        // Find this path in the perm list, and delete it from future multipath searches.
                        for(Vertex v : verticesPerm)
                            if(v.getCity().getName().equals(u.getPathToThisCity().getSourceCity().getName())) {
                                v.getRouteList().remove(u.getPathToThisCity());
                                break;
                            }
                    }
                    u = u.getPrevVertex();
                }
            }

            // Add the discovered multipath to the network, if it has more than one path.
            if(pathList.size() > 1) {
                MultiPath multiPath = new MultiPath(pathList);
                routeNetwork.addRoute(multiPath);
            }

            System.out.println("Found a shortest path.  Contains: " + pathList.size() + " route(s).");
            for (Path path : pathList)
                System.out.print(path.getSourceCity().getName() + " " + path.getTargetCity().getName() + " ");
            System.out.println("");
        }
        System.out.println("");
    }

//    // Find paths from source to target using a variation of local search.
//    public void generateMultipathsForTarget(CityNode target, RouteNetwork routeNetwork) {
//
//        // Here, if we have already calculated the multipaths, simply return and end.
//        List<Route> routeListExisting = getRoutesFrom(target);
//        if(routeListExisting != null)
//            for (Route route : routeListExisting)
//                if (route instanceof MultiPath)
//                    return;
//
//        // Variables.
//        int numTries = 10000; // A heuristic solution.
//        int numMultipaths = 10; // We assume there are less than 10 paths to the target.
//        int numHops = 5; // Assume maximum number of hops is numHops+1.
//        List<MultiPath> multiPathsList = new ArrayList<>();
//        Random rand = new Random();
//
//        for(int i = 0; i < numTries; i++) { // For each multipath.
//            if (multiPathsList.size() == numMultipaths) break;
//
//            int numHopsTrue = rand.nextInt(numHops + 1) + 1;
//
//            CityNode prevCity = CityNode.getCenterTarget();
//            List<Path> pathList = new ArrayList<>();
//
//            List<CityNode> cityNodeList = new ArrayList<>();
//            cityNodeList.add(CityNode.getCenterTarget());
//
//            // We are going through each hop, randomly selecting the next Path, while not circling or returning to past cities.
//            for (int j = 0; j < numHopsTrue-1; j++) {
//                List<Route> routeList = routeNetwork.getRoutesFrom(prevCity);
//                if (routeList == null || routeList.size() == 0) break;
//                Path path = null;
//                int randomIndex = rand.nextInt(routeList.size() - 1);
//                if (routeList.size() == 1 && routeList.get(0) instanceof Path)
//                    path = (Path) routeList.get(0);
//                else if(routeList.get(randomIndex) instanceof Path)
//                    path = (Path) routeList.get(randomIndex);
//                while (pathList.contains(path) && cityNodeList.contains(path.getTargetCity())) {
//                    randomIndex = rand.nextInt(routeList.size() - 1);
//                    path = (Path) routeList.get(randomIndex);
//                }
//                pathList.add(path);
//                cityNodeList.add(path.getTargetCity());
//                prevCity = path.getTargetCity();
//            }
//
//            // On last hop, look for our target city.  If not found in routelist, discard and try again.
//            List<Route> routeList = routeNetwork.getRoutesFrom(prevCity);
//            if (routeList == null || routeList.size() == 0) continue;
//            for (Route route : routeList) {
//                if (route instanceof Path && route.getTargetCity().getName().equals(target.getName())) {
//                    pathList.add((Path) route);
//                    MultiPath multiPath = new MultiPath(pathList);
//
//                    // Here, check if we have already found this multipath.
//                    for(MultiPath multiPath1 : multiPathsList)
//                        if(!multiPath1.getUniqueMultiPathName().equals(multiPath.getUniqueMultiPathName())) {
//                            multiPathsList.add(multiPath);
//                            routeNetwork.addRoute(multiPath);
//                        }
//                }
//            }
//        }
//        System.out.println("Found " + multiPathsList.size() + " path(s).");
//    }

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
    }

    // Returns the PDP between many routes to target city at a particular time.
    public double getPDP(CityNode sourceCity, CityNode targetCity, int time) {
        double PDP = 0;
        List<Route> routeList = RouteTreeNode.getRouteList(sourceCity.getName() + targetCity.getName());
        if (routeList == null) return -1; // Error handling, no routes between cities.
        try {
            // Calculate PDP:
            for (Route route : routeList) { // For each path.
                double predictedDay = route.getTOAPrediction(); // Get the expected time to arrival.
                if (time == (int) predictedDay) { // If the current time is equal to the estimated day of arrival.
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
