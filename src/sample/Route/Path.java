package sample.Route;

import sample.CityNode;
import sample.Disease;
import sample.Route.Route;

public class Path extends Route {

    // Global vars
    private CityNode sourceCity; // Start city.
    private CityNode targetCity; // End city.
    private double transportDistance; // Transport distance between cities.
    private double NEAPValue; // NEAP.
    private double effectiveDistance; // Effective distance between cities.
    private Boolean openPath = true; // True if city can get transport distance updates.
    private Disease disease;

    // Constructor
    public Path(CityNode sourceCity, CityNode targetCity, double initialTransportDistance, Disease disease) {
        this.disease = disease;
        this.sourceCity = sourceCity;
        this.targetCity = targetCity;
        this.transportDistance = initialTransportDistance;
    }



    // Calculates Effective Distance.
    private void calcEffDis() {
        // Get the flux fraction
        double fluxFraction = transportDistance / sourceCity.getCityPopulation();
        if (fluxFraction == 0 || fluxFraction < 0 || Double.isInfinite(fluxFraction) || Double.isNaN(fluxFraction)) {
            effectiveDistance = -1; // < 0 implies infinite distance.
        }else {
            // Calculate effdis from city I to city J.
            effectiveDistance = 1 - Math.log(fluxFraction); // Math.log is in base e. Eqn: dm,n = 1 − ln Pm,n.
        }
    }

    // Calculates NEAP
    private void calcNEAP(){
        calcEffDis(); // Get any updated values.

        // Calc NEAP numerically.
        double equatAt;
        double stepSize = 0.1; // Smaller this value, the more accurate.
        double Tb = disease.getTbTime(); // Get the time taken to complete a wave and have 0 case incidence.
        double lambda = disease.getGrowthRate(); // Spreading rate.
        double NEAP = 0;

        // Sum function values for the integral.
        for(equatAt = 0; equatAt <= Tb; equatAt += stepSize)
        {
            double propT = Math.exp(1 - effectiveDistance + lambda*equatAt - (1/lambda)*Math.exp(1 - effectiveDistance + lambda*equatAt));
            if(equatAt < 0) propT = 0; // Apply Heavyside function.
            NEAP += propT;
        }
        NEAPValue = NEAP;
    }

    // TODO Get Gumbel prediction.
    @Override
    public int getGumbelPrediction()
    {
        return -1;
    }

    // Returns the effective distance
    @Override
    public double getEffDis()
    {
        calcEffDis();
        return effectiveDistance;
    }

    @Override
    public double getNEAP() {
        calcNEAP();
        return NEAPValue;
    }

    @Override
    public CityNode getSourceCity() {
        return sourceCity;
    }

    @Override
    public CityNode getTargetCity() {
        return targetCity;
    }

    public double getTransportDistance() {
        return transportDistance;
    }

    public void addTransportDistance(double transportDistance) {
        this.transportDistance += transportDistance;
    }
}
