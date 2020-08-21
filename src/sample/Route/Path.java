package sample.Route;

import sample.CityNode;
import sample.Disease;


public class Path extends Route {

    // Global vars
    private CityNode sourceCity; // Start city.
    private CityNode targetCity; // End city.
    private double transportDistance; // Transport distance between cities.
    private double NEAPValue = -1; // NEAP.
    private double effectiveDistance; // Effective distance between cities.
    private Disease disease;
    private double fluxFraction;
    private double TOA_Prediction = -1;

    // Constructor
    public Path(CityNode sourceCity, CityNode targetCity, double initialTransportDistance, Disease disease) {
        this.disease = disease;
        this.sourceCity = sourceCity;
        this.targetCity = targetCity;
        this.transportDistance = initialTransportDistance;
        fluxFraction = transportDistance / sourceCity.getCityPopulation();
        calcEffDis();
    }


    // Calculates Effective Distance.
    private void calcEffDis() {
        // Get the flux fraction
        fluxFraction = transportDistance / sourceCity.getCityPopulation();
        if (fluxFraction == 0 || fluxFraction < 0 || Double.isInfinite(fluxFraction) || Double.isNaN(fluxFraction)) {
            effectiveDistance = -1; // < 0 implies infinite distance.
        }else {
            // Calculate effdis from city I to city J.
            effectiveDistance = 1 - Math.log(fluxFraction); // Math.log is in base e. Eqn: dm,n = 1 − ln Pm,n.
        }
    }

    // Calculates NEAP
    private void calcNEAP(){
        // Calc NEAP numerically.
        double tau;
        double stepSize = 0.01; // Smaller this value, the more accurate.
        double Tb = disease.getTbTime(); // Get the time taken to complete a wave and have 0 case incidence.
        double lambda = disease.getMeanGrowthRate(); // Returns mean growth rate over life time of disease.
        double propT1 = 0;

        // Sum function values for the integral.
        for(tau = 0; tau <= Tb; tau += stepSize)
        {
            propT1 += Math.exp(1 - effectiveDistance + lambda * tau - (1 / lambda) * Math.exp(1 - effectiveDistance + lambda * tau));
        }

        NEAPValue = propT1;
    }

    private void calcTOA() {
        // Calc time numerically using a Gumbel pdf.
        double Tb = disease.getTbTime();// disease.getTbTime(); // Get the time taken to complete a wave and have 0 case incidence.
        double lambda = disease.getMeanGrowthRate();// disease.getMeanGrowthRate(); // Returns mean growth rate over life time of disease.
        double time;
        double stepSize = 0.001;
        double sum1 = 0;
        double sum2 = 0;
        double normFac = 0;

        for (time = 0; time < Tb; time += stepSize) {
            sum1 += (Math.exp(1 - effectiveDistance + lambda*time - (1/lambda)*Math.exp(1 - effectiveDistance + lambda*time)));
        }

        normFac = 1 / (sum1*stepSize);

        for(time = 0; time < Tb; time += stepSize) {
            sum2 += time * (Math.exp(1 - effectiveDistance + lambda*time - (1/lambda)*Math.exp(1 - effectiveDistance + lambda*time)));
        }
        sum2 *= stepSize;

        TOA_Prediction = sum2 * normFac;
        System.out.println(TOA_Prediction);
    }

    @Override
    public double getTOAPrediction() {
        // If we have already calculated it, return the value.
        if (TOA_Prediction != -1)
            return TOA_Prediction;
        else {
            calcTOA();
            return TOA_Prediction;
        }
    }

    // Returns the effective distance
    @Override
    public double getEffDis()
    {
        return effectiveDistance;
    }

    @Override
    public double getNEAP() {
        if(NEAPValue != -1) return NEAPValue;
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
