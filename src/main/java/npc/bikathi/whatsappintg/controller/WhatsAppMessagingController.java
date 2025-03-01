package npc.bikathi.whatsappintg.controller;

import com.whatsapp.api.domain.media.FileType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npc.bikathi.whatsappintg.dto.UserResponse;
import npc.bikathi.whatsappintg.types.MediaHandlingService;
import npc.bikathi.whatsappintg.types.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/messaging/whatsapp")
@CrossOrigin(origins = { "https://developers.facebook.com/" })
public class WhatsAppMessagingController {
    private final MessageService whatsAppMessageService;
    private final MediaHandlingService whatsAppMediaStorageService;

    @PostMapping(value = "/publish", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> publishMessage(
        @RequestParam(name = "toPhoneNumbers") List<String> toPhoneNumbers,
        @RequestParam(name = "message") String message,
        @RequestPart(name = "photos", required = false) List<MultipartFile> photos
    ) {
        // if there are any front photos, upload them and get back the ids
        List<String> photoIds = new ArrayList<>();

        if (!Objects.isNull(photos)) {
            // handle photos
            for (MultipartFile photo : photos) {
                try {
                    String photoId = whatsAppMediaStorageService.uploadFile(photo.getBytes(), FileType.PNG, photo.getOriginalFilename());
                    photoIds.add(photoId);
                } catch (IOException e) {
                    log.error("Error uploading file", e);
                }
            }
        }

        // call the appropriate method to send message based on whether there is media
        try {
            if (!Objects.isNull(photos)) {
                whatsAppMessageService.sendMessageWithAttachment(toPhoneNumbers, photoIds, message);
            } else {
                whatsAppMessageService.sendMessageWithoutAttachment(toPhoneNumbers, message);
            }

            log.info("WhatsApp message published successfully!");
            return ResponseEntity.ok("WhatsApp message published successfully!");
        } catch (Exception e) {
            log.error("Failed to send WhatsApp message! Cause: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
