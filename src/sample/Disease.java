package sample;

public class Disease {
    private String name;
    private double growthRate;
    private double recoveryRate;
    private double q = 0.10; // Rate at which a0 decreases to k*a0.

    // Static constructor
    public Disease(String nameI, double growthRateI, double recoveryRateI) {
        this.name = nameI;
        this.growthRate = growthRateI;
        this.recoveryRate = recoveryRateI;
    }


    // A more thorough equation for calculating case incidence in a population.
    public double getCaseIncidenceEqn2(int time)
    {
        double equateAt; // Current time of the integral loop.
        double caseIncidence = 0; // Number of active cases.
        double stepSize = 0.001; // Amount to increase equateAt by, per loop.  Decrease for more accuracy.
        double a0 = this.getInitialGrowthRate(); // Initial infection rate.
        double k = 0.00; // k*a0 is the final infection rate; k is a scalar (0 value is good -> no infections).
        double beta = this.getRecoveryRate(); // Recovery rate.

        // This loop is equivalent to an approx. of an integral on [0, time] range.
        for(equateAt = 0; equateAt < time; equateAt += stepSize)
        {
            caseIncidence += a0*((1-k)*Math.exp(-q*equateAt) + k)*Math.exp(a0*((1-k)*Math.exp(-q*equateAt) + k) * equateAt - beta*(time - equateAt));
        }

        // Theta component:
        if(caseIncidence <= 1) // 1 is the epsilon value, which is a threshold implying eradication of the disease.
            caseIncidence = 0;

        return caseIncidence; // Cast to int, as it is a number of people.
    }


    public double getMeanGrowthRate()
    {
        double a0 = this.getInitialGrowthRate();
        double k = 0; // kappa
        double time;
        double Tb = getTbTime();
        double stepSize = 0.001;
        double currentAofT = 0;
        double divideBy = 0;

        for(time = 0; time < Tb; time+=stepSize)
        {
            currentAofT += a0*((1-k)*Math.exp(-1*q*time) + k);
            divideBy++;
        }

        return currentAofT/divideBy;
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
    public double getInitialGrowthRate() {
        return growthRate;
    }

    public double getRecoveryRate() {
        return recoveryRate;
    }
}