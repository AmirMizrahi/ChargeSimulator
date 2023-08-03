package com.chargeSimulator.SimulatorChargingStations;

import com.chargeSimulator.ChargingStations.ChargingStation;
import com.chargeSimulator.ChargingStations.ChargingStationRepository;
import com.chargeSimulator.SimulatorConfig;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/simulator")
public class SimulatorChargingStationController {
    @Autowired
    private ChargingStationRepository m_chargingStationsRepository;
    private Map<String , SimulatorChargingStation> chargingStations = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(SimulatorChargingStationController.class);

    @PostConstruct
    public void init() {
        // Perform any initialization tasks here
        // This method will be called once the ChargingStationController is constructed by Spring

        printTitle();

        //Get all charging stations from DB.
        List<ChargingStation> chargingStationsFromDB = m_chargingStationsRepository.findAll();

        // Create SimulatorChargingStation instances from existing ChargingStation entities
        for (ChargingStation chargingStation : chargingStationsFromDB) {
            SimulatorChargingStation simulatorChargingStation = new SimulatorChargingStation(chargingStation.getId().toString());
            chargingStations.put(simulatorChargingStation.getId(), simulatorChargingStation);

            logger.info("Charging station created with ID: {}", simulatorChargingStation.getId());

        }

        logger.info("SimulatorChargingStationController initialized.");
    }

    private static void printTitle() {
        // ANSI escape code for green color
        String greenColor = "\u001B[32m";

        // ANSI escape code to reset color to default
        String resetColor = "\u001B[0m";

        System.out.println();
        System.out.println();
        System.out.println(greenColor + "      ██████     ██  ███    ███  ██       ██  ██             ██      ██████████     ██████      ████████ " + resetColor);
        System.out.println(greenColor + "     ██          ██  ████  ████  ██       ██  ██            ████         ██       ██       ██   ██     ██" + resetColor);
        System.out.println(greenColor + "      █████      ██  ██ ████ ██  ██       ██  ██           ██  ██        ██      ██         ██  ████████ " + resetColor);
        System.out.println(greenColor + "            ██   ██  ██  ██  ██  ██       ██  ██          ████████       ██       ██       ██   ██   ██  " + resetColor);
        System.out.println(greenColor + "      ██████     ██  ██      ██   ████████    █████████  ██      ██      ██         ██████      ██    ██ " + resetColor);
        System.out.println(greenColor + "      V2.0" + resetColor);
        System.out.println();
        System.out.println();
    }

    @PostMapping("/createChargingStation")
    public ResponseEntity<String> createChargingStation(@RequestParam String chargingStationId) {
        HttpStatus httpStatus = HttpStatus.OK;
        JsonObject jsonObject = new JsonObject();

        chargingStations.put(chargingStationId, new SimulatorChargingStation(chargingStationId));

        logger.info("Charging station created with ID: {}", chargingStationId);

        return ResponseEntity.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
    }

    @PutMapping("/charge")
    public ResponseEntity<String> charge(@RequestParam("chargingStationId") String chargingStationId) {
        HttpStatus httpStatus = HttpStatus.OK;
        JsonObject jsonObject = new JsonObject();

        //TODO E - check id exists in chargingStations.???

        Thread chargingThread = new Thread(() -> {
            SimulatorChargingStation simulatorChargingStation = chargingStations.get(chargingStationId);
            simulatorChargingStation.setCharging(true);
            int initialPercentage = getRandomInitialPercentage();
            simulatorChargingStation.setInitialPercentage(initialPercentage);
            chargingStations.put(chargingStationId, simulatorChargingStation);

            //charge.
            logger.info("Charging started for charging station with ID: {}", chargingStationId);
            int targetPercentage = 100;

            while ((simulatorChargingStation.getFinalPercentage() < targetPercentage) && (simulatorChargingStation.isCharging())) {
                try {
                    // Wait for the specified time (secForOnePercent) before increasing the charging percentage.
                    Thread.sleep(SimulatorConfig.SEC_FOR_ONE_PERCENT * 1000);

                    // Increase the charging percentage by 1 for every SEC_FOR_ONE_PERCENT seconds.
                    simulatorChargingStation.setFinalPercentage(simulatorChargingStation.getFinalPercentage() + 1);

                    chargingStations.put(chargingStationId, simulatorChargingStation);

                    logger.info("Charging progress for charging station with ID {}: {}%", chargingStationId, simulatorChargingStation.getFinalPercentage());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Charging process completed.
            simulatorChargingStation.setCharging(false);
            chargingStations.put(chargingStationId, simulatorChargingStation);
            logger.info("Charging completed for charging station with ID: {}", chargingStationId);
        });

        chargingThread.start();

        return ResponseEntity.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
    }

    @PutMapping("/unCharge")
    public ResponseEntity<String> unCharge(@RequestParam("chargingStationId") String chargingStationId) {
        HttpStatus httpStatus = HttpStatus.OK;
        JsonObject jsonObject = new JsonObject();

        SimulatorChargingStation simulatorChargingStation = chargingStations.get(chargingStationId);
        simulatorChargingStation.setCharging(false);
        jsonObject.addProperty("percentage", simulatorChargingStation.getFinalPercentage() - simulatorChargingStation.getInitialPercentage());
        chargingStations.put(simulatorChargingStation.getId(), simulatorChargingStation);

        logger.info("Charging stopped for charging station with ID: {}", chargingStationId);

        return ResponseEntity.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
    }

    // Helper method to generate a random initial percentage for charging.
    private int getRandomInitialPercentage() {
        Random random = new Random();
        return random.nextInt(SimulatorConfig.HIGH_PERCENT - SimulatorConfig.LOW_PERCENT + 1) + SimulatorConfig.LOW_PERCENT;
    }

    @GetMapping("/allChargingStations")
    public ResponseEntity<List<ChargingStationInfo>> getAllChargingStations() {
        List<ChargingStationInfo> chargingStationInfos = new ArrayList<>();

        for (SimulatorChargingStation simulatorChargingStation : chargingStations.values()) {
            ChargingStationInfo chargingStationInfo = new ChargingStationInfo();
            chargingStationInfo.setChargingStationId(simulatorChargingStation.getId());
            chargingStationInfo.setFinalPercentage(simulatorChargingStation.getFinalPercentage());
            chargingStationInfo.setCharging(simulatorChargingStation.isCharging());
            chargingStationInfos.add(chargingStationInfo);
        }

        return ResponseEntity.ok(chargingStationInfos);
    }

    // Helper class for charging station information
    class ChargingStationInfo {
        private String chargingStationId;
        private int finalPercentage;
        private boolean charging;

        // Getters and Setters...

        public String getChargingStationId() {
            return chargingStationId;
        }

        public void setChargingStationId(String chargingStationId) {
            this.chargingStationId = chargingStationId;
        }

        public int getFinalPercentage() {
            return finalPercentage;
        }

        public void setFinalPercentage(int finalPercentage) {
            this.finalPercentage = finalPercentage;
        }

        public boolean isCharging() {
            return charging;
        }

        public void setCharging(boolean charging) {
            this.charging = charging;
        }
    }
}
