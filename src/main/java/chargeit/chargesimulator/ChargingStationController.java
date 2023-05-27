package chargeit.chargesimulator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/simulator")
public class ChargingStationController {
    private  ChargingStation chargingStationSimulator;
    private final Map<String , ChargingStation> chargingStations = new HashMap<>();
    @PostMapping("/chargingStations")
    public ResponseEntity<String> receiveChargingStationId(@RequestBody String chargingStationId) {

        chargingStations.put(chargingStationId, new ChargingStation(chargingStationId));
        return ResponseEntity.ok("ChargingStation ID received successfully in Simulator.");
    }
    public ChargingStationController() {

this.chargingStationSimulator = new ChargingStation(("123"));

    }
   // @PutMapping("/charge")
    //public void charge() {
      //  ElectricCar carToSimulate = new ElectricCar("ABBC");
        //chargingStationSimulator.communicateWithVehicle(carToSimulate);
        //int PercentToReturnToServer = chargingStationSimulator.HowMuchPercentTheStationCharge;

        //System.out.print("OK!");
        //return ResponseEntity.ok("Charging started for vehicle " + carToSimulate.getId());
    //}
        @PutMapping("/charge")
        public ResponseEntity<String> charge(@RequestParam("chargingStationId") String chargingStationId) {
            Thread chargingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //chargingStationSimulator = chargingStations.get(chargingStationId);
                    ElectricCar carToSimulate = new ElectricCar("ABBC");
                    chargingStationSimulator.communicateWithVehicle(carToSimulate);
                    int PercentToReturnToServer = chargingStationSimulator.HowMuchPercentTheStationCharge;

                }
            });

            chargingThread.start();

            return ResponseEntity.ok("Charging started.");
        }

    @PutMapping("/unCharge")
    public int unCharge() {
        chargingStationSimulator.isCharging = false;
        return chargingStationSimulator.HowMuchPercentTheStationCharge;
    }
}
