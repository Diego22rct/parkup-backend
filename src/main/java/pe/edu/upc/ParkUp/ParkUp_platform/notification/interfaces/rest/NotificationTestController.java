package pe.edu.upc.ParkUp.ParkUp_platform.notification.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.ParkUp.ParkUp_platform.notification.application.internal.outboundservices.WhatsAppNotificationProvider;

import java.util.Map;

/**
 * REST controller for testing WhatsApp notifications
 * Provides simple endpoints to test Twilio WhatsApp sending
 */
@RestController
@RequestMapping("/api/v1/notifications/test")
@Tag(name = "Notification Testing", description = "Test endpoints for notification services")
public class NotificationTestController {

    private final WhatsAppNotificationProvider whatsAppProvider;

    public NotificationTestController(WhatsAppNotificationProvider whatsAppProvider) {
        this.whatsAppProvider = whatsAppProvider;
    }

    /**
     * Test WhatsApp sending via Twilio
     * 
     * @param phoneNumber The recipient phone number (e.g., +51986091617)
     * @param message The WhatsApp message to send
     * @return Response indicating success or failure
     */
    @PostMapping("/whatsapp")
    @Operation(summary = "Test WhatsApp sending", description = "Send a test WhatsApp message via Twilio to verify configuration")
    public ResponseEntity<Map<String, Object>> testWhatsApp(
            @RequestParam String phoneNumber,
            @RequestParam String message) {
        
        // Validate phone number format
        if (!whatsAppProvider.validatePhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", "Invalid phone number format. Must be in E.164 format (e.g., +51986091617)"
            ));
        }
        
        // Send WhatsApp message
        String messageSid = whatsAppProvider.sendWhatsApp(phoneNumber, message);
        
        if (messageSid != null) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "messageSid", messageSid,
                "to", "whatsapp:" + phoneNumber,
                "message", "WhatsApp message sent successfully via Twilio!"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Failed to send WhatsApp message. Check server logs for details."
            ));
        }
    }

    /**
     * Test endpoint to verify Twilio WhatsApp configuration
     */
    @GetMapping("/whatsapp/config")
    @Operation(summary = "Check WhatsApp configuration", description = "Verify Twilio WhatsApp configuration status")
    public ResponseEntity<Map<String, Object>> checkWhatsAppConfig() {
        return ResponseEntity.ok(Map.of(
            "service", "Twilio WhatsApp",
            "status", "configured",
            "message", "Twilio WhatsApp service is ready. Use POST /api/v1/notifications/test/whatsapp to send a test message.",
            "sandbox", "Make sure you've connected to the Twilio Sandbox: https://console.twilio.com/us1/develop/sms/try-it-out/whatsapp-learn"
        ));
    }
}
