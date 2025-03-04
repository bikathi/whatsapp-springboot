package npc.bikathi.whatsappintg.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import npc.bikathi.whatsappintg.defs.IBroadcastEntryService;
import npc.bikathi.whatsappintg.entity.BroadcastEntry;
import npc.bikathi.whatsappintg.repository.IBroadcastEntryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BroadcastEntryService implements IBroadcastEntryService {
    private final IBroadcastEntryRepository broadcastEntryRepository;

    @Override
    public void saveBroadcastEntry(@NotNull BroadcastEntry newEntry) {
        broadcastEntryRepository.save(newEntry);
    }

    @Override
    public Optional<BroadcastEntry> getBroadCastEntry(@NotNull String id) {
        return broadcastEntryRepository.findById(id);
    }
}
