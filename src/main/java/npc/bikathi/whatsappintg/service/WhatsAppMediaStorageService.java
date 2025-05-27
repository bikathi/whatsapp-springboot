package npc.bikathi.whatsappintg.service;

import com.whatsapp.api.domain.media.FileType;
import com.whatsapp.api.domain.media.UploadResponse;
import com.whatsapp.api.impl.WhatsappBusinessCloudApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npc.bikathi.whatsappintg.config.whatsapp.WhatsAppConfig;
import npc.bikathi.whatsappintg.defs.IMediaHandlingService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsAppMediaStorageService implements IMediaHandlingService {
    private final WhatsappBusinessCloudApi whatsappBusinessCloudApi;
    private final WhatsAppConfig whatsAppConfig;

    @Override
    public String uploadFile(byte[] fileContent, Object fileType, String fileName) {
        UploadResponse response = whatsappBusinessCloudApi.uploadMedia(
            whatsAppConfig.getSenderPhoneId(),
            fileName,
            (FileType) fileType,
            fileContent
        );

        return response.id();
    }
}
