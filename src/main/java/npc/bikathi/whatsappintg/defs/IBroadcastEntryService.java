package npc.bikathi.whatsappintg.defs;

import jakarta.validation.constraints.NotNull;
import npc.bikathi.whatsappintg.entity.BroadcastEntry;

import java.util.Optional;

public interface IBroadcastEntryService {
    void saveBroadcastEntry(@NotNull BroadcastEntry newEntry);
    Optional<BroadcastEntry> getBroadCastEntry(@NotNull String id);
}
