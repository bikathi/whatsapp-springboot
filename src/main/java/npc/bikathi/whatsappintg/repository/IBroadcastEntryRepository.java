package npc.bikathi.whatsappintg.repository;

import npc.bikathi.whatsappintg.entity.BroadcastEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBroadcastEntryRepository extends JpaRepository<BroadcastEntry, String> {

}
