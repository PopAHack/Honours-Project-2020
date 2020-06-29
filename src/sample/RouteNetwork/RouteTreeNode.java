package sample.RouteNetwork;

import sample.Route.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteTreeNode {
    // This class is a custom data structure used to store the 100,000 (approx.) routes used in this program with a multi leafed tree.

    // Static var:
    private static RouteTreeNode root = new RouteTreeNode();

    // Global vars:
    private List<Route> routeList;
    private Character character;
    private List<RouteTreeNode> branches = new ArrayList<>();

    // Constructor
    public RouteTreeNode() { }

    public static void addRoute(Route route)
    {
        root.addRoute(route, 0); // Add from root level.
    }

    private void addRoute(Route route, int level) {
        try {
            String name = route.getSourceCity().getName() + route.getTargetCity().getName();

            // Break recursion.
            if (level == 5) { // Last level
                if (this.routeList == null) this.routeList = new ArrayList<>();
                this.routeList.add(route);
                this.character = name.charAt(level);
                branches = null;
                return;
            } else {
                if (character == null) character = name.charAt(level);
            }

            for (RouteTreeNode rtn : branches) {
                if (rtn.getCharacter().equals(name.charAt(level))) {
                    rtn.addRoute(route, ++level);
                    return;
                }
            }

            // If branch doesn't exist
            RouteTreeNode routeTreeNode = new RouteTreeNode();
            branches.add(routeTreeNode);
            routeTreeNode.addRoute(route, ++level);
            return;
        } catch (Exception ex) {
            System.out.println("add Route Error");
            ex.printStackTrace();
        }
    }

    // Recursively goes through the tree to find the route.  Interface static method.
    public static List<Route> getRouteList(String routeConcatName)
    {
        return root.getRouteList(routeConcatName, true);
    }

    // Recursively goes through the tree to find the route.
    private List<Route> getRouteList(String routeConcatName, Boolean recursiveMethod)
    {
        try {
            // If not a leaf node, find next branch
            if (branches != null) {
                for (RouteTreeNode rtn : branches) {
                    if (rtn.getCharacter().equals(routeConcatName.charAt(0))) {
                        return rtn.getRouteList((String) routeConcatName.subSequence(1, routeConcatName.toCharArray().length), true);
                    }
                }
            } else {
                // If a leaf node:
                return routeList;
            }
        }catch (Exception ex)
        {
            System.out.println("getRoute Error");
            ex.printStackTrace();
        }
        return null; // Cannot find a branch leading to the route position.
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
