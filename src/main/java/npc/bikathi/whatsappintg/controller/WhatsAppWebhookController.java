package npc.bikathi.whatsappintg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npc.bikathi.whatsappintg.config.whatsapp.WhatsAppConfig;
import npc.bikathi.whatsappintg.dto.WhatsAppCallbackStructure;
import npc.bikathi.whatsappintg.entity.BroadcastEntry;
import npc.bikathi.whatsappintg.entity.GptResponse;
import npc.bikathi.whatsappintg.entity.VehiclePart;
import npc.bikathi.whatsappintg.service.BroadcastEntryService;
import npc.bikathi.whatsappintg.service.GptPromptService;
import npc.bikathi.whatsappintg.util.CommunicationsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WhatsAppWebhookController {
    private final WhatsAppConfig whatsAppConfig;
    private final BroadcastEntryService broadcastEntryService;
    private final CommunicationsUtil communicationsUtil;
    private final GptPromptService gptPromptService;

    @PostMapping(value = "/webhooks")
    public void callbackWebhook(HttpServletRequest request) {
        try {
            WhatsAppCallbackStructure jsonBody = new ObjectMapper().readValue(request.getInputStream(), WhatsAppCallbackStructure.class);

            // get to the message object in the callback so that we can analyse the context
            final WhatsAppCallbackStructure.Entry mainEntry = jsonBody.getEntry().get(0);
            WhatsAppCallbackStructure.Entry.Change change = !Objects.isNull(mainEntry) ? mainEntry.getChanges().get(0) : null;
            WhatsAppCallbackStructure.Entry.Change.Value.Message message = !Objects.isNull(change) ? change.getValue().getMessages().get(0) : null;

            if(message != null  && message.getContext() != null) {
                // the context id will help us get the id of the broadcast sent to the user
                final String contextId = message.getContext().getId();
                BroadcastEntry broadcastEntry = broadcastEntryService
                    .getBroadCastEntry(contextId)
                .orElseThrow(() -> new EntityNotFoundException("Broadcast context message not found!"));

                // get the part associated with the broadcast message
                VehiclePart part = broadcastEntry.getVehiclePart();

                // verify with GPT that the message matches
                GptResponse gptResponse = gptPromptService.promptGpt(broadcastEntry.getBroadcastMessage(), message.getText().getBody());
                if(gptResponse.isRelevant()) {
                    // return to PartSultan the response message along with the id of the vehicle part
                    communicationsUtil.returnResponse(
                        part.getExternPartId(),
                        message.getFrom(),
                        message.getText().getBody(),
                        gptResponse.getPartCost()
                    );
                } else {
                    // TODO: Do something else here if GPT determines that the user's response is irrelevant for now, we're printing what GPT thought to come to this conclusion
                    log.info("User's response is irrelevant! GPT's thought process: {}", gptResponse.getThoughtProcess());
                }

            // throw an exception when there is no message entry or the context is not attached
            } else throw new RuntimeException("No message info or context info found in callback!");

        } catch (Exception e) {
            log.error("Failed to read callback data! Cause: {}", e.getMessage());
        }
    }

    @GetMapping(value = "/webhooks")
    public ResponseEntity<Integer> verifyWebhook(
        @RequestParam(name = "hub.mode", required = false) String mode,
        @RequestParam(name = "hub.verify_token", required = false) String token,
        @RequestParam(name = "hub.challenge", required = false) Integer challenge
    ) {
        final String WEBHOOK_VERIFY_TOKEN = whatsAppConfig.getCallbackValidationToken();
        if ("subscribe".equals(mode) && WEBHOOK_VERIFY_TOKEN.equals(token)) {
            log.info("Webhook verified successfully!");
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
