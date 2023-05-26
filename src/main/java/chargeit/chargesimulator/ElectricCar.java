package chargeit.chargesimulator;

import java.util.Random;

public class ElectricCar {
    private final String id;
    private boolean charging;
    private int percent;
    private static final int MAX_PERCENT = 100;

    public ElectricCar(String id) {
        this.id = id;
        this.charging = false;
        this.percent = (new Random().nextInt(100));
    }

    public String getId() {
        return id;
    }

    public double getPercent() {
        return percent;
    }

    public double getMaxPercent() {
        return MAX_PERCENT;
    }

    public void addOnePercentToBattery() {
        percent = percent + 1;
    }

    public boolean isCharging() {
        return charging;
    }

    public void startCharging() {
        this.charging = true;
    }

    public void stopCharging() {
        this.charging = false;
    }
}