package npc.bikathi.whatsappintg.repository;

import jakarta.validation.constraints.NotNull;
import npc.bikathi.whatsappintg.entity.VehiclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVehiclePartRepository extends JpaRepository<VehiclePart, Long> {
    void deleteByExternId(@NotNull String externId);
}
