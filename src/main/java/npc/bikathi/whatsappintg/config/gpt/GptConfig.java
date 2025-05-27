package npc.bikathi.whatsappintg.config.gpt;

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
@ConfigurationProperties(prefix = "api.chatgpt")
public class GptConfig {
    private String model;
    private String apiKey;
}
