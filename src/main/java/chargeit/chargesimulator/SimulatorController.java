package chargeit.chargesimulator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulator")
public class SimulatorController {
    private final Simulator chargingStationSimulator;
    public SimulatorController() {
        // Instantiate an instance of ElectricCar
        ElectricCar electricCar = new ElectricCar("ABC123");

        // Pass the ElectricCar instance to the constructor of Simulator
        this.chargingStationSimulator = new Simulator(electricCar);
    }
    @PutMapping("/charge")
    public void charge() {
        ElectricCar carToSimulate = new ElectricCar("ABBC");
        chargingStationSimulator.communicateWithVehicle(carToSimulate);
        int PercentToReturnToServer = chargingStationSimulator.HowMuchPercentTheStationCharge;

        //System.out.print("OK!");
        //return ResponseEntity.ok("Charging started for vehicle " + carToSimulate.getId());
    }

    @PutMapping("/unCharge")
    public int unCharge() {
        // int PercentThatChargeToAskPayFor;
        //chargingStationSimulator.StopCharging();
        //PercentThatChargeToAskPayFor = chargingStationSimulator.HowMuchPercentTheStationCharge;
        //chargingStationSimulator.HowMuchPercentTheStationCharge=0;
        //return PercentThatChargeToAskPayFor;

        chargingStationSimulator.isCharging = false;
        chargingStationSimulator.car.stopCharging();
        return chargingStationSimulator.HowMuchPercentTheStationCharge;


    }



}
