package npc.bikathi.whatsappintg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "vehicle_part")
@NoArgsConstructor
@AllArgsConstructor
public class VehiclePart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_part_id")
    private Long id;

    @Column(name = "extern_part_id", nullable = false)
    private String externPartId; // the id from part sultan database

    @OneToMany(mappedBy = "vehiclePart")
    private List<BroadcastEntry> broadcastEntry = new ArrayList<>();

    public void addBroadcastEntry(@NotNull List<BroadcastEntry> broadcastEntry) {
        this.broadcastEntry.addAll(broadcastEntry);
    }
}
