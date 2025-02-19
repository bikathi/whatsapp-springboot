package npc.bikathi.whatsappintg.config;

import com.whatsapp.api.WhatsappApiFactory;
import com.whatsapp.api.impl.WhatsappBusinessCloudApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WhatsAppCloudConfig {
    private final PropertiesConfig propertiesConfig;

    @Bean
    public WhatsappBusinessCloudApi waCloudConfig() {
        WhatsappApiFactory factory = WhatsappApiFactory.newInstance(propertiesConfig.getApiKey());
        return factory.newBusinessCloudApi();
    }
}
