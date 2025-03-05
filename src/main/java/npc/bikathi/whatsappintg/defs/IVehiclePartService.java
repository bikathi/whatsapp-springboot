package npc.bikathi.whatsappintg.defs;

import jakarta.validation.constraints.NotNull;
import npc.bikathi.whatsappintg.entity.VehiclePart;

import java.util.Optional;

public interface IVehiclePartService {
    void deleteVehiclePartByExternId(@NotNull String externId);
    VehiclePart saveVehiclePart(@NotNull VehiclePart vehiclePart);
    Optional<VehiclePart> getVehiclePart(@NotNull String externPartId);
}
