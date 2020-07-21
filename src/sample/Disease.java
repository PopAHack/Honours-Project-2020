package sample;

public class Disease {
    private String name;
    private double growthRate;
    private double recoveryRate;
    private double infectiousTime;

    // Static constructor
    public Disease(String nameI, double growthRateI, double recoveryRateI, double infectiousTimeI) {
        this.name = nameI;
        this.growthRate = growthRateI;
        this.recoveryRate = recoveryRateI;
        this.infectiousTime = infectiousTimeI;
    }


    // A more thorough equation for calculating case incidence in a population.
    public double getCaseIncidenceEqn2(int time)
    {
        double equateAt; // Current time of the integral loop.
        double caseIncidence = 0; // Number of active cases.
        double stepSize = 0.1; // Amount to increase equateAt by, per loop.  Decrease for more accuracy.
        double a0 = this.getGrowthRate(); // Initial infection rate.
        double k = 0.00; // k*a0 is the final infection rate; k is a scalar (0 value is good -> no infections).
        double q = 0.10; // Rate at which a0 decreases to k*a0.
        double beta = this.getRecoveryRate(); // Recovery rate.
        double aOfT = a0*((1-k)*Math.exp(-1*q*time) + k);

        // This loop is equivalent to an approx. of an integral on [0, time] range.
        for(equateAt = 0; equateAt < time; equateAt += stepSize)
        {
            caseIncidence += aOfT*Math.exp(aOfT*(time - beta*(equateAt - time)));
        }

        // Theta component:
        if(caseIncidence <= 1) // 1 is the epsilon value, which is a threshold implying eradication of the disease.
            caseIncidence = 0;

        return caseIncidence; // Cast to int, as it is a number of people.
    }

    // Runs through the case incidence equation, and finds the time (day) that the case incidence is below a threshold.
    public int getTbTime()
    {
        int time = 1;
        int incrementNum = 1;
        double threshold = 1;
        double currentCI;
        do {
            currentCI = getCaseIncidenceEqn2(time);
            time += incrementNum;
        }
        while(currentCI > threshold);
        return time;
    }



    // Getters and Setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    public double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public double getInfectiousTime() {
        return infectiousTime;
    }

    public void setInfectiousTime(double infectiousTime) {
        this.infectiousTime = infectiousTime;
    }
}