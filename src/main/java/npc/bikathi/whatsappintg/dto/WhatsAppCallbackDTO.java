package npc.bikathi.whatsappintg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhatsAppCallbackDTO {
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

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Metadata {
                    private String displayPhoneNumber;
                    private String phoneNumberId;
                }
            }
        }
    }
}
