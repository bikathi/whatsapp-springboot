package npc.bikathi.whatsappintg.util;

import jakarta.validation.constraints.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CommunicationsUtil {
    @Async(value = "asyncTaskExecutor")
    public final void returnResponse(@NotNull String externPartId, @NotNull String message) {
        // TODO: implement some REST call here
    }
}
