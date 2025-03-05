package npc.bikathi.whatsappintg.repository;

import jakarta.validation.constraints.NotNull;
import npc.bikathi.whatsappintg.entity.VehiclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVehiclePartRepository extends JpaRepository<VehiclePart, Long> {
    void deleteByExternPartId(@NotNull String externId);
    Optional<VehiclePart> findByExternPartId(@NotNull String externId);
}
