package sample.RouteNetwork;

import sample.CityNode;
import sample.Route.MultiPath;
import sample.Route.Path;
import sample.Route.Route;
import java.util.ArrayList;
import java.util.List;

public class RouteNetwork {

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
                    if ((path.getSourceCity().getName() + path.getTargetCity().getName()).equals(route.getSourceCity().getName() + route.getTargetCity().getName())) {
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
    public List<Route> getRoutesFrom(CityNode sourceCity) {
        return RouteTreeNode.getTargetsFromSource(sourceCity);
    }

    // Apply a variation of Dijkstra's algorithm.
    public void generateMultipathsForTarget(CityNode target, RouteNetwork routeNetwork) {

        // If we have already calculated the paths between these cities, return.
        List<Route> existingRoutesForTarget = RouteTreeNode.getRouteList(CityNode.getCenterTarget().getName() + target.getName());
        if (existingRoutesForTarget.size() > 1)
            return;

        List<Vertex> vertices = new ArrayList<>();
        List<Vertex> verticesPerm = new ArrayList<>();

        Vertex targetVertex = null;

        int numCities = 1200;
        int numPaths = 8;

        // Init vertices.
        List<CityNode> cityNodeList = CityNode.getCityNodes();
        for (int i = 0; i < numCities; i++) {
            if (i == numCities / 2) continue; // Skip the source city.
            Vertex vertex = new Vertex(Double.POSITIVE_INFINITY, null, cityNodeList.get(i), routeNetwork.getRoutesFrom(cityNodeList.get(i)), null);
            vertices.add(vertex);
            verticesPerm.add(vertex.copy());

            // Keep the target vertex for later ref.
            if (cityNodeList.get(i).getName().equals(target.getName())) {
                targetVertex = vertex;
            }
        }

        // Set source distance to 0.
        Vertex sourceVertex = new Vertex(0, null, CityNode.getCenterTarget(), routeNetwork.getRoutesFrom(CityNode.getCenterTarget()), null);
        vertices.add(0, sourceVertex);
        verticesPerm.add(0, sourceVertex.copy());

        for (int k = 0; k < numPaths; k++) {
            if (k != 0) {
                // Redo initiating the vertices list with fresh copies of our vertices.
                vertices = new ArrayList<>();
                for (int i = 0; i < numCities; i++) {
                    vertices.add(verticesPerm.get(i).copy());

                    // Keep the target vertex for later ref.
                    if (vertices.get(i).getCity().getName().equals(target.getName())) {
                        targetVertex = vertices.get(i);
                    }
                }
            }

            while (vertices.size() != 0) {
                Vertex u = null;
                // Get vertex with minimum distance.
                for (Vertex vertex : vertices) {
                    if (u == null) {
                        u = vertex;
                    } else if (vertex.getDistance() < u.getDistance())
                        u = vertex;
                }
                vertices.remove(u);

                if (u.getCity().getName().equals(target.getName())) // Break if we have found the target.
                    break;

                // Find all neighbours of u, still in vertices.
                if (u.getRouteList() == null) continue; // If this is an end node, stop.
                for (Route route : u.getRouteList()) {
                    for (Vertex v : vertices) {
                        if (route instanceof Path && v.getCity().getName().equals(route.getTargetCity().getName())) {
                            double alt = u.getDistance() + route.getEffDis();
                            if (alt < v.getDistance()) {
                                v.setDistance(alt);
                                v.setPrevVertex(u);
                                v.setPathToThisCity((Path) route);
                            }
                        }
                    }
                }
            }

            // Now trace back the shortest path.
            List<Path> pathList = new ArrayList<>();
            Vertex u = targetVertex;
            if (u.getPrevVertex() != null || u.getCity().getName().equals(CityNode.getCenterTarget().getName())) {
                while (u != null) {
                    if (u.getPathToThisCity() != null) {
                        pathList.add(0, u.getPathToThisCity());

                        // Find all routes used
                        for (Vertex v : verticesPerm) {
                            if (v.getCity().getName().equals(u.getPathToThisCity().getSourceCity().getName())) {
                                v.getRouteList().remove(u.getPathToThisCity());
                            }
                        }
                    }
                    u = u.getPrevVertex();
                }
            }

            // Add the discovered multipath to the network, if it has more than one path.
            if (pathList.size() > 1) {
                MultiPath multiPath = new MultiPath(pathList);
                routeNetwork.addRoute(multiPath);
            } else if (pathList.size() == 0) return; // Done here.

            System.out.println("Found a shortest path.  Contains: " + pathList.size() + " route(s).");
            for (Path path : pathList)
                System.out.print(path.getSourceCity().getName() + " " + path.getTargetCity().getName() + " ");
            System.out.println("");
        }
    }


    // Get the smallest effective distance from all routes.
    public double getMinEffDis(CityNode sourceCity, CityNode targetCity) {
        List<Route> routeList = RouteTreeNode.getRouteList(sourceCity.getName() + targetCity.getName());
        if (routeList == null)
            return -1; // No routes between.
        try {
            double minEffDis = -1;
            for (Route route : routeList) {
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
}
