package npc.bikathi.whatsappintg.service;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import npc.bikathi.whatsappintg.defs.IBroadcastEntryService;
import npc.bikathi.whatsappintg.entity.BroadcastEntry;
import npc.bikathi.whatsappintg.repository.IBroadcastEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BroadcastEntryService implements IBroadcastEntryService {
    private final IBroadcastEntryRepository broadcastEntryRepository;

    @Override
    public List<BroadcastEntry> saveBroadcastEntries(List<BroadcastEntry> newEntries) {
        return broadcastEntryRepository.saveAll(newEntries);
    }

    @Override
    public void deleteBroadcastEntries(@NotEmpty List<BroadcastEntry> entries) {
        broadcastEntryRepository.deleteAll(entries);
    }

    @Override
    public Optional<BroadcastEntry> getBroadCastEntry(@NotNull String id) {
        return broadcastEntryRepository.findById(id);
    }
}
