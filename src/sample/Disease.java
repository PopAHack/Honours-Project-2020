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