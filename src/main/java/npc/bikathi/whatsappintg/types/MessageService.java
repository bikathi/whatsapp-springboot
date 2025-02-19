package npc.bikathi.whatsappintg.types;

import java.util.List;

public interface MessageService {
    void sendMessageWithAttachment(List<String> toPhoneNumbers, List<String> mediaIds, String message);
    void sendMessageWithoutAttachment(List<String> toPhoneNumbers, String message);
}
