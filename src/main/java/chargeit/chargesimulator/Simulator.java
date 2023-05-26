package chargeit.chargesimulator;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Simulator {
    // Simulate charging process for a given vehicle
    // 252 sec charging 1% average
    private static final int SEC_FOR_ONE_PERCENT = 1;
    public int HowMuchPercentTheStationCharge = 0;
    public boolean isCharging=false;
    public ElectricCar car;

    public Simulator(ElectricCar _car)
    {
        car = _car;
    }

    public void chargeVehicle(ElectricCar vehicle) {

        System.out.println("Charging vehicle " + vehicle.getId());
        isCharging = true;
        vehicle.startCharging();
        try {
            while (vehicle.getPercent() != vehicle.getMaxPercent() && isCharging==true) {
                TimeUnit.SECONDS.sleep(SEC_FOR_ONE_PERCENT);
                vehicle.addOnePercentToBattery();
                HowMuchPercentTheStationCharge = HowMuchPercentTheStationCharge + 1;
                System.out.println("Your Current Percent is: " + vehicle.getPercent() + "%.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vehicle.stopCharging();
        isCharging=false;
        System.out.println("Vehicle " + vehicle.getId() + " charged complete");
    }

    // Handle communication with the vehicle
    public void communicateWithVehicle(ElectricCar vehicle) {
        System.out.println("Connecting to vehicle " + vehicle.getId());
        if (vehicle.isCharging()) {
            System.out.println("Vehicle is already charging");
            return;
        }
        System.out.println("Starting charging for vehicle " + vehicle.getId());
        chargeVehicle(vehicle);
    }

    public void StopCharging()
    {
        isCharging = false;
        this.car.stopCharging();

    }

}