package npc.bikathi.whatsappintg.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Scope(value = "singleton")
@ConfigurationProperties(prefix = "api.whatsapp")
public class PropertiesConfig {
    private String apiKey;
    private String senderPhoneId;
    private String callbackValidationToken;
}
