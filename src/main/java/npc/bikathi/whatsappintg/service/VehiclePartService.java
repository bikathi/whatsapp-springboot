package npc.bikathi.whatsappintg.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import npc.bikathi.whatsappintg.defs.IVehiclePartService;
import npc.bikathi.whatsappintg.entity.VehiclePart;
import npc.bikathi.whatsappintg.repository.IVehiclePartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiclePartService implements IVehiclePartService {
    private final IVehiclePartRepository vehiclePartRepository;

    @Override
    public void deleteVehiclePartByExternId(@NotNull String externId) {
        vehiclePartRepository.deleteByExternPartId(externId);
    }

    @Override
    public VehiclePart saveVehiclePart(@NotNull VehiclePart vehiclePart) {
        return vehiclePartRepository.save(vehiclePart);
    }

    @Override
    public Optional<VehiclePart> getVehiclePart(@NotNull String externPartId) {
        return vehiclePartRepository.findByExternPartId(externPartId);
    }
}
