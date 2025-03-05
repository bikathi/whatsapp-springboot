package npc.bikathi.whatsappintg.controller;

import com.whatsapp.api.domain.media.FileType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npc.bikathi.whatsappintg.defs.IBroadcastEntryService;
import npc.bikathi.whatsappintg.defs.IMediaHandlingService;
import npc.bikathi.whatsappintg.defs.IMessageService;
import npc.bikathi.whatsappintg.defs.IVehiclePartService;
import npc.bikathi.whatsappintg.entity.VehiclePart;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/messaging/whatsapp")
@CrossOrigin(origins = {})
public class WhatsAppMessagingController {
    private final IMessageService whatsAppIMessageService;
    private final IVehiclePartService vehiclePartService;
    private final IBroadcastEntryService broadcastEntryService;
    private final IMediaHandlingService whatsAppMediaStorageService;

    @PostMapping(value = "/publish", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> publishMessage(
        @RequestParam(name = "vehiclePartId") String vehiclePartId,
        @RequestParam(name = "toPhoneNumbers") List<String> toPhoneNumbers,
        @RequestParam(name = "message") String message,
        @RequestPart(name = "photos", required = false) List<MultipartFile> photos
    ) {
        try {
            // store the details about the vehicle part
            VehiclePart vehiclePart =  VehiclePart.builder()
                .externPartId(vehiclePartId)
            .build();
            VehiclePart savedVehiclePart = vehiclePartService.saveVehiclePart(vehiclePart);

            // if there are any photos upload them and get back the ids
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
            if (!Objects.isNull(photos)) {
                whatsAppIMessageService.sendMessageWithAttachment(toPhoneNumbers, photoIds, message, savedVehiclePart);
            } else {
                whatsAppIMessageService.sendMessageWithoutAttachment(toPhoneNumbers, message, savedVehiclePart);
            }

            log.info("WhatsApp message published successfully!");
            return ResponseEntity.ok("WhatsApp message published successfully!");
        } catch (Exception e) {
            log.error("Failed to send WhatsApp message! Cause: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/clean-inquiry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteInquiry(@RequestParam(name = "externPartId") String externPartId) {
        try {
            VehiclePart vehiclePart = vehiclePartService.getVehiclePart(externPartId).orElseThrow(() -> new EntityNotFoundException("Vehicle part not found!"));
            broadcastEntryService.deleteBroadcastEntries(vehiclePart.getBroadcastEntry());
            vehiclePartService.deleteVehiclePartByExternId(externPartId);
            return ResponseEntity.ok("Inquiry deleted successfully!");

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
