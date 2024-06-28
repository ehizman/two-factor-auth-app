package com.ehizman.auth_app.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class TwilioClient {
    private final String TWILIO_ACCOUNT_SID;
    private final String TWILIO_AUTH_TOKEN;
    private final String VERIFICATION_SID;

    private Logger log = LoggerFactory.getLogger(TwilioClient.class);


    public TwilioClient(Environment env) {
        this.TWILIO_ACCOUNT_SID = env.getProperty("TWILIO_ACCOUNT_SID");
        this.TWILIO_AUTH_TOKEN = env.getProperty("TWILIO_AUTH_TOKEN");
        this.VERIFICATION_SID = env.getProperty("VERIFY_SERVICE_SID");
    }

    @PostConstruct
    public void initialize() {
        if (this.TWILIO_ACCOUNT_SID != null && this.TWILIO_AUTH_TOKEN != null && this.VERIFICATION_SID != null) {
            log.info("Twilio Client initialized successfully");
            Twilio.init(this.TWILIO_ACCOUNT_SID, this.TWILIO_AUTH_TOKEN);
        } else {
            throw new NullPointerException("Error starting application \n\nMissing credentials TWILIO_ACCOUNT_SID and/or TWILIO_AUTH_TOKEN");
        }
    }
}


