package npc.bikathi.whatsappintg.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "broadcast_entry")
public class BroadcastEntry {
    @Id
    @Basic
    private String id;

    @ManyToOne
    @JoinColumn(name = "fk_vehicle_part_id", nullable = false)
    private VehiclePart vehiclePart;
}
