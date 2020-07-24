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
            // For now, we will randomly generate the data from normal distributions.`

            Random rand = new Random(seed);
            int numCities = 1200; // This is the number of active international airports.
            int numFlights = (int)(rand.nextGaussian()*15000 + 100000); // Average number of flights per day

            // Make cities
            for(int i = 0; i < numCities - 1; i++) {
                CityNode cityNode = new CityNode(randNameGenerator(rand, false), (int) (rand.nextGaussian()*800000 + 1000000), disease);
            }
            CityNode cityNode = new CityNode(randNameGenerator(rand, true), (int) (rand.nextGaussian()*800000 + 1000000), disease);

            CityNode.setCenterTarget(CityNode.get(numCities/2));
            CityNode.getCenterTarget().setPaint(Color.RED);

            // Generate all single paths from flight data.
            for(int i = 0; i < numFlights; i++)
            {
                int indexT = -1;
                int indexS = -1;
                while(indexT < 0 || indexT > numCities - 1 || indexS < 0 || indexS > numCities - 1) {
                    indexT = (int) (rand.nextGaussian()*30 + 600);
                    indexS = (int) (rand.nextGaussian()*30 + 600);
                }
                CityNode sourceCity = CityNode.get(indexS);
                CityNode targetCity = CityNode.get(indexT);

                Path path = new Path(sourceCity, targetCity, rand.nextGaussian()*50 + 200, disease);
                if(sourceCity.getName().equals(CityNode.getCenterTarget().getName())) targetCity.setIsARouteTarget(true);

                network.addRoute(path);
                System.out.println("Added route to network.  Remaining: " + (numFlights - i));
            }

        }catch (Exception ex)
        {
            System.out.println("Distance data generator:" + ex);
        }
    }

    // Generates a unique random three letter code, in same format as airport codes.
    private List<String> nameListForNameGenerator = new ArrayList<>();
    private String randNameGenerator(Random rand, Boolean last)
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String name;

        do {
            name = "";
            name += alphabet.toCharArray()[rand.nextInt(26 - 1)];
            name += alphabet.toCharArray()[rand.nextInt(26 - 1)];
            name += alphabet.toCharArray()[rand.nextInt(26 - 1)];
        }while(nameListForNameGenerator.contains(name));

        nameListForNameGenerator.add(name);
        if(last)
            nameListForNameGenerator.clear(); // Wipe from memory.
        return name.toUpperCase();
    }

}
