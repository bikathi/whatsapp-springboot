package npc.bikathi.whatsappintg.config;

import com.whatsapp.api.WhatsappApiFactory;
import com.whatsapp.api.impl.WhatsappBusinessCloudApi;
import lombok.RequiredArgsConstructor;
import npc.bikathi.whatsappintg.config.whatsapp.WhatsAppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WhatsAppCloudConfig {
    private final WhatsAppConfig whatsAppConfig;

    @Bean
    public WhatsappBusinessCloudApi waCloudConfig() {
        WhatsappApiFactory factory = WhatsappApiFactory.newInstance(whatsAppConfig.getApiKey());
        return factory.newBusinessCloudApi();
    }
}
