package npc.bikathi.whatsappintg.defs;

import jakarta.validation.constraints.NotNull;

public interface IVehiclePartService {
    void deleteVehiclePartByExternId(@NotNull String externId);
}
