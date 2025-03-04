package npc.bikathi.whatsappintg.service;

import lombok.RequiredArgsConstructor;
import npc.bikathi.whatsappintg.defs.IVehiclePartService;
import npc.bikathi.whatsappintg.repository.IVehiclePartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehiclePartService implements IVehiclePartService {
    private final IVehiclePartRepository vehiclePartRepository;

    @Override
    public void deleteVehiclePartByExternId(String externId) {
        vehiclePartRepository.deleteByExternId(externId);
    }
}
