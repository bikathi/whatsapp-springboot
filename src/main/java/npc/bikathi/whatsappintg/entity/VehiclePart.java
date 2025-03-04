package npc.bikathi.whatsappintg.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "vehicle_part")
@NoArgsConstructor
@AllArgsConstructor
public class VehiclePart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_part_id")
    private Long id;

    @Column(name = "extern_id", nullable = false)
    private String externId; // the id from part sultan database

    @OneToMany(mappedBy = "vehiclePart")
    private BroadcastEntry broadcastEntry;
}
