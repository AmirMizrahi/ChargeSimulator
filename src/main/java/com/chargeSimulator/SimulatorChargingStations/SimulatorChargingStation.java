package com.chargeSimulator.SimulatorChargingStations;

public class SimulatorChargingStation {
    private int initialPercentage;
    private int finalPercentage;
    private boolean isCharging;
   private String id;
   private String name;


    public SimulatorChargingStation(String id, String name)
    {
        this.id = id;
        this.isCharging = false;
        this.initialPercentage = 0;
        this.name = name;
    }

    public int getInitialPercentage() {
        return initialPercentage;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public String getId() {
        return id;
    }

    public int getFinalPercentage() {
        return finalPercentage;
    }

    public String getName() {
        return name;
    }

    public void setInitialPercentage(int initialPercentage) {
        this.setFinalPercentage(initialPercentage);
        this.initialPercentage = initialPercentage;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public void setFinalPercentage(int finalPercentage) {
        this.finalPercentage = finalPercentage;
    }

    public void setName(String name) {
        this.name = name;
    }
}