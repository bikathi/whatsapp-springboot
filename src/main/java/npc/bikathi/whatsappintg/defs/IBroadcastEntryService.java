package npc.bikathi.whatsappintg.defs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import npc.bikathi.whatsappintg.entity.BroadcastEntry;

import java.util.List;
import java.util.Optional;

public interface IBroadcastEntryService {
    List<BroadcastEntry> saveBroadcastEntries(@NotEmpty List<BroadcastEntry> newEntries);
    void deleteBroadcastEntries(@NotEmpty List<BroadcastEntry> entries);
    Optional<BroadcastEntry> getBroadCastEntry(@NotNull String id);
}
