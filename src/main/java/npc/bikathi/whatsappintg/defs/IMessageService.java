package npc.bikathi.whatsappintg.defs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import npc.bikathi.whatsappintg.entity.VehiclePart;

import java.util.List;

public interface IMessageService {
    void sendMessageWithAttachment(
        @NotEmpty List<String> toPhoneNumbers,
        @NotEmpty List<String> mediaIds,
        @NotNull String message,
        @NotNull VehiclePart vehiclePart
    );
    void sendMessageWithoutAttachment(
        @NotEmpty List<String> toPhoneNumbers,
        @NotNull String message,
        @NotNull VehiclePart vehiclePart
    );
}
