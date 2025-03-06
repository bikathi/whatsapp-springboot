package npc.bikathi.whatsappintg.util;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommunicationsUtil {
    @Async(value = "asyncTaskExecutor")
    public final void returnResponse(@NotNull String externPartId, @NotNull String fromPhoneId, @NotNull String message) {
        // TODO: implement some REST call here
        log.info("Congrats! Message to be sent to Part Sultan! ExternPartId: {}, From phoneId: {}, Message: {}", externPartId, fromPhoneId, message);
    }
}
