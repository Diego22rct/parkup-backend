package pe.edu.upc.ParkUp.ParkUp_platform.notification.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * User preferences for notifications
 * Value object representing which channels a user wants to receive notifications on
 */
@Embeddable
@Getter
public class NotificationPreferences {
    
    private Boolean pushEnabled;
    private Boolean emailEnabled;
    private Boolean smsEnabled;
    
    protected NotificationPreferences() {
        // Default: all channels enabled
        this.pushEnabled = true;
        this.emailEnabled = true;
        this.smsEnabled = false; // SMS typically opt-in
    }
    
    public NotificationPreferences(Boolean pushEnabled, Boolean emailEnabled, Boolean smsEnabled) {
        this.pushEnabled = pushEnabled != null ? pushEnabled : true;
        this.emailEnabled = emailEnabled != null ? emailEnabled : true;
        this.smsEnabled = smsEnabled != null ? smsEnabled : false;
    }
    
    /**
     * Checks if a specific channel is enabled
     */
    public boolean isChannelEnabled(NotificationChannel channel) {
        return switch (channel) {
            case PUSH -> Boolean.TRUE.equals(pushEnabled);
            case EMAIL -> Boolean.TRUE.equals(emailEnabled);
            case SMS -> Boolean.TRUE.equals(smsEnabled);
        };
    }
}
