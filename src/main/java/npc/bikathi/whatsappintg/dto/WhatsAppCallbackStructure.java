package npc.bikathi.whatsappintg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhatsAppCallbackStructure {
    private String object;
    private List<Entry> entry;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Entry {
        private String id;
        private List<Change> changes;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Change {
            private Value value;
            private String field;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Value {
                private String messagingProduct;
                private Metadata metadata;
                private List<Contact> contacts;
                private List<Message> messages;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Metadata {
                    private String displayPhoneNumber;
                    private String phoneNumberId;
                }

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Contact {
                    private Profile profile;
                    private String waId;

                    @Data
                    @AllArgsConstructor
                    @NoArgsConstructor
                    public static class Profile {
                        private String name;
                    }
                }

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Message {
                    private Context context;
                    private String from;
                    private String id;
                    private long timestamp;
                    private Text text;
                    private String type;

                    @Data
                    @AllArgsConstructor
                    @NoArgsConstructor
                    public static class Context {
                        private String from;
                        private String id;
                    }

                    @Data
                    @AllArgsConstructor
                    @NoArgsConstructor
                    public static class Text {
                        private String body;
                    }
                }
            }
        }
    }
}
