package npc.bikathi.whatsappintg.service;

import com.whatsapp.api.domain.media.FileType;
import com.whatsapp.api.domain.media.UploadResponse;
import com.whatsapp.api.impl.WhatsappBusinessCloudApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npc.bikathi.whatsappintg.config.PropertiesConfig;
import npc.bikathi.whatsappintg.defs.IMediaHandlingService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsAppMediaStorageService implements IMediaHandlingService {
    private final WhatsappBusinessCloudApi whatsappBusinessCloudApi;
    private final PropertiesConfig propertiesConfig;

    @Override
    public String uploadFile(byte[] fileContent, Object fileType, String fileName) {
        UploadResponse response = whatsappBusinessCloudApi.uploadMedia(
            propertiesConfig.getSenderPhoneId(),
            fileName,
            (FileType) fileType,
            fileContent
        );

        return response.id();
    }
}
