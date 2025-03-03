package npc.bikathi.whatsappintg.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class WhatsAppWebhookController {
    @PostMapping(value = "/webhook")
    public void callbackWebhook(HttpServletRequest request) {
        try {
            String jsonBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            log.info("Message received from user is: {}", jsonBody);
            // return ResponseEntity.ok(jsonBody);
        } catch (Exception e) {
            log.error("Failed to read callback data! Cause: {}", e.getMessage());
            // return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping(value = "/webhooks")
    public ResponseEntity<Integer> verifyWebhook(
        @RequestParam(name = "hub.mode", required = false) String mode,
        @RequestParam(name = "hub.verify_token", required = false) String token,
        @RequestParam(name = "hub.challenge", required = false) Integer challenge,
        HttpServletRequest request
    ) {
        String fullUrl = request.getRequestURL().toString() + "?" + request.getQueryString();
        log.info("Request URL: {}", fullUrl);

        log.info("Webhook triggered! mode: {}, token: {}, challenge: {}", mode, token, challenge);
        final String WEBHOOK_VERIFY_TOKEN = "ELDSRYQG9QXM15XBK9N7";
        if ("subscribe".equals(mode) && WEBHOOK_VERIFY_TOKEN.equals(token)) {
            log.info("Webhook verified successfully!");
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
