package npc.bikathi.whatsappintg.service;

import com.whatsapp.api.domain.messages.ImageMessage;
import com.whatsapp.api.domain.messages.Message;
import com.whatsapp.api.domain.messages.TextMessage;
import com.whatsapp.api.domain.messages.response.MessageResponse;
import com.whatsapp.api.impl.WhatsappBusinessCloudApi;
import com.whatsapp.api.utils.Formatter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npc.bikathi.whatsappintg.config.PropertiesConfig;
import npc.bikathi.whatsappintg.defs.IBroadcastEntryService;
import npc.bikathi.whatsappintg.defs.IMessageService;
import npc.bikathi.whatsappintg.defs.IVehiclePartService;
import npc.bikathi.whatsappintg.entity.BroadcastEntry;
import npc.bikathi.whatsappintg.entity.VehiclePart;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsAppMessageService implements IMessageService {
    private final WhatsappBusinessCloudApi whatsappBusinessCloudApi;
    private final PropertiesConfig propertiesConfig;
    private final IBroadcastEntryService broadcastEntryService;
    private final IVehiclePartService vehiclePartService;

    @Override
    public void sendMessageWithAttachment(
        @NotEmpty List<String> toPhoneNumbers,
        @NotEmpty List<String> mediaIds,
        @NotNull String messageString,
        @NotNull VehiclePart vehiclePart
    ) {
        List<String> messageIds;
        if(mediaIds.size() == 1) {
            // send the single image with the message as the caption
            messageIds = toPhoneNumbers.parallelStream().map(phoneNumber -> {
                var imageMessage = new ImageMessage()
                    .setId(mediaIds.get(0)) // media id (uploaded before)
                .setCaption(messageString + "\n" + Formatter.bold("Kindly tag this message (swipe right over it) when responding to this inquiry"));

                var message = Message.MessageBuilder.builder()
                    .setTo(phoneNumber)
                .buildImageMessage(imageMessage);

                MessageResponse response = whatsappBusinessCloudApi.sendMessage(propertiesConfig.getSenderPhoneId(), message);
                return response.messages().get(0).id();
            }).toList();
        } else {

            messageIds = toPhoneNumbers.parallelStream().map(phoneNumber -> {
                // send the images first (without a caption), then send the text message last
                for(String id: mediaIds) {
                    var imageMessage = new ImageMessage().setId(id);
                    var message = Message.MessageBuilder.builder()
                        .setTo(phoneNumber)
                    .buildImageMessage(imageMessage);
                    whatsappBusinessCloudApi.sendMessage(propertiesConfig.getSenderPhoneId(), message);
                }

                // then send the message last
                var message = getTextMessageMessage(messageString, phoneNumber);
                MessageResponse response = whatsappBusinessCloudApi.sendMessage(propertiesConfig.getSenderPhoneId(), message);
                return response.messages().get(0).id();
            }).toList();
        }

        // save the broadcast entries
        List<BroadcastEntry> broadcastEntries = messageIds.parallelStream().map(id ->
            BroadcastEntry.builder().id(id).vehiclePart(vehiclePart).build()
        ).toList();
        List<BroadcastEntry> savedBroadcastEntries = broadcastEntryService.saveBroadcastEntries(broadcastEntries);

        // update the vehiclePart with the broadcast entries
        vehiclePart.getBroadcastEntry().addAll(savedBroadcastEntries);
        vehiclePartService.saveVehiclePart(vehiclePart);
    }

    @Override
    public void sendMessageWithoutAttachment(@NotEmpty List<String> toPhoneNumbers, @NotNull String messageString, @NotNull VehiclePart vehiclePart) {
        List<String> messageIds = toPhoneNumbers.parallelStream().map(phoneNumber -> {
            var message = getTextMessageMessage(
                messageString,
                phoneNumber
            );
            MessageResponse response = whatsappBusinessCloudApi.sendMessage(propertiesConfig.getSenderPhoneId(), message);
            return response.messages().get(0).id();
        }).toList();

        // save the broadcast entries
        List<BroadcastEntry> broadcastEntries = messageIds.parallelStream().map(id ->
            BroadcastEntry.builder().id(id).vehiclePart(vehiclePart).build()
        ).toList();
        List<BroadcastEntry> savedBroadcastEntries = broadcastEntryService.saveBroadcastEntries(broadcastEntries);

        // update the vehiclePart with the broadcast entries
        vehiclePart.getBroadcastEntry().addAll(savedBroadcastEntries);
        vehiclePartService.saveVehiclePart(vehiclePart);
    }

    private static Message getTextMessageMessage(String messageString, String phoneNumber) {
        return Message.MessageBuilder.builder()
            .setTo(phoneNumber)
            .buildTextMessage(
                new TextMessage()
                    .setBody(messageString + "\n\n" + Formatter.bold("Kindly tag this message (swipe right over it) when responding to this inquiry"))
                    .setPreviewUrl(false)
            );
    }
}
