package npc.bikathi.whatsappintg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GptResponse {
    private boolean isRelevant;
    private String partCost;
    private String thoughtProcess;
}
