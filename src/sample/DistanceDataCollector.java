package sample;

import javafx.scene.paint.Color;
import sample.Route.MultiPath;
import sample.Route.Path;
import sample.RouteNetwork.RouteNetwork;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DistanceDataCollector {

    // Global vars.
    private int seed = 0;
    private RouteNetwork network;
    private Disease disease;

    // Constructor.
    public DistanceDataCollector(RouteNetwork network, Disease disease) {
        try {
            this.network = network;
            this.disease = disease;

            // TODO This is where the flight API would be called, and flights calculated and filtered.
            // For now, we will randomly generate the data from normal distributions.

            Random rand = new Random(seed);
            int numCities = 1200; // This is the number of active international airports.
            int numFlights = (int)(rand.nextGaussian()*15000 + 100000); // Average number of flights per day

            // Make cities
            for(int i = 0; i < numCities; i++) {
                CityNode cityNode = new CityNode(randNameGenerator(rand), (int) (rand.nextGaussian()*10000000 + 40000000), disease);
            }

            CityNode.setCenterTarget(CityNode.get(numCities/2));
            CityNode.getCenterTarget().setPaint(Color.RED);

            // Generate paths.
            // Half are paths, half are multipaths.
            for(int i = 0; i < numFlights/2; i++)
            {
                int indexT = -1;
                int indexS = -1;
                while(indexT < 0 || indexT > numCities - 1 || indexS < 0 || indexS > numCities - 1) {
                    indexT = (int) (rand.nextGaussian()*350 + 600);
                    indexS = (int) (rand.nextGaussian()*350 + 600);
                }
                CityNode sourceCity = CityNode.get(indexS);
                CityNode targetCity = CityNode.get(indexT);

                Path path = new Path(sourceCity, targetCity, rand.nextGaussian()*50 + 120, 1, disease);
                network.addRoute(path);
                targetCity.setIsARouteTarget(true);
                System.out.println("Added route to network.  Remaining: " + (numFlights - i));
            }

            // Generate multipaths.
            for(int i = 0; i < numFlights/2;)
            {
                int numPaths = rand.nextInt(2) + 2; // Rand uniform distribution between 2-3 flights.
                List<Path> pathList = new ArrayList<>();
                List<CityNode> cityNodes = new ArrayList<>();

                // Get source city.
                int indexS = -1;
                while(indexS < 0 || indexS > numCities - 1) {
                    indexS = (int) (rand.nextGaussian()*350 + 600);
                }
                CityNode prevCity = CityNode.get(indexS);
                cityNodes.add(prevCity);
                double transport = rand.nextGaussian()*50 + 120;

                // Generate each path.
                for(int j = 0; j < numPaths; j++)
                {
                    CityNode nextCity;
                    do {
                        // Get next city.
                        int indexT = -1;
                        while (indexT < 0 || indexT > numCities - 1) {
                            indexT = (int) (rand.nextGaussian()*350 + 600);
                        }
                        nextCity = CityNode.get(indexT);
                    } while(cityNodes.contains(nextCity));

                    cityNodes.add(nextCity);

                    Path path = new Path(prevCity, nextCity, transport/numPaths,1, disease);
                    prevCity = nextCity;
                    pathList.add(path);
                }

                MultiPath multiPath = new MultiPath(pathList, disease);
                network.addRoute(multiPath);
                i += numPaths;
                System.out.println("Added route to network.  Remaining: " + (numFlights/2 - i));
            }
        }catch (Exception ex)
        {
            System.out.println("Distance data generator:" + ex);
        }
    }

    // Generates a random three letter code, in same format as airport codes.
    private String randNameGenerator(Random rand)
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String name = "";
        name += alphabet.toCharArray()[rand.nextInt(26-1)];
        name += alphabet.toCharArray()[rand.nextInt(26-1)];
        name += alphabet.toCharArray()[rand.nextInt(26-1)];
        return name.toUpperCase();
    }

}
