package com.chargeSimulator.ChargingStations;

import com.chargeSimulator.location.GeoLocation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ChargingStationRepository extends MongoRepository<ChargingStation, String> {
    Optional<ChargingStation> findByLocation(GeoLocation location);
    List<ChargingStation> findByOwnerId(ObjectId ownerId);
    Optional<ChargingStation> findById(ObjectId id);
    void deleteByLocation(GeoLocation location);
    void deleteAllByOwnerId(ObjectId id);
}
