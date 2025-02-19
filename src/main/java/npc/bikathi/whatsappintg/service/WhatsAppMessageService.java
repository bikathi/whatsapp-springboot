package npc.bikathi.whatsappintg.service;

import com.whatsapp.api.domain.messages.ImageMessage;
import com.whatsapp.api.domain.messages.Message;
import com.whatsapp.api.domain.messages.TextMessage;
import com.whatsapp.api.impl.WhatsappBusinessCloudApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npc.bikathi.whatsappintg.config.PropertiesConfig;
import npc.bikathi.whatsappintg.types.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsAppMessageService implements MessageService {
    private final WhatsappBusinessCloudApi whatsappBusinessCloudApi;
    private final PropertiesConfig propertiesConfig;

    @Override
    public void sendMessageWithAttachment(List<String> toPhoneNumbers, List<String> mediaIds, String messageString) {
        if(mediaIds.size() == 1) {
            // send the single image with the message as the caption
            toPhoneNumbers.parallelStream().forEach(phoneNumber -> {
                var imageMessage = new ImageMessage()//
                    .setId(mediaIds.get(0))// media id (uploaded before)
                .setCaption(messageString);

                var message = Message.MessageBuilder.builder()//
                    .setTo(phoneNumber)//
                .buildImageMessage(imageMessage);

                whatsappBusinessCloudApi.sendMessage(propertiesConfig.getSenderPhoneId(), message);
            });
        } else {
            toPhoneNumbers.parallelStream().forEach(phoneNumber -> {
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
                whatsappBusinessCloudApi.sendMessage(propertiesConfig.getSenderPhoneId(), message);
            });
        }
    }

    @Override
    public void sendMessageWithoutAttachment(List<String> toPhoneNumbers, String messageString) {
        toPhoneNumbers.parallelStream().forEach(phoneNumber -> {
            var message = getTextMessageMessage(messageString, phoneNumber);
            whatsappBusinessCloudApi.sendMessage(propertiesConfig.getSenderPhoneId(), message);
        });
    }

    private static Message getTextMessageMessage(String messageString, String phoneNumber) {
        return Message.MessageBuilder.builder()
            .setTo(phoneNumber)
            .buildTextMessage(
                new TextMessage()
                    .setBody(messageString)
                    .setPreviewUrl(false)
            );
    }
}
