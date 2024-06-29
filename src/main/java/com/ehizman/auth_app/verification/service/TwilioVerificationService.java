package com.ehizman.auth_app.verification.service;

import com.ehizman.auth_app.verification.model.CreateFactorRequest;
import com.twilio.rest.verify.v2.service.entity.Challenge;
import com.twilio.rest.verify.v2.service.entity.Factor;
import com.twilio.rest.verify.v2.service.entity.NewFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TwilioVerificationService implements VerificationService {
    private Logger log = LoggerFactory.getLogger(TwilioVerificationService.class);
    private Environment env;
    private final String VERIFY_SERVICE_SID;

    public TwilioVerificationService(Environment env) {
        this.env = env;
        VERIFY_SERVICE_SID = env.getProperty("VERIFY_SERVICE_SID");
    }
    public Map<String, Object> createFactor(CreateFactorRequest createFactorRequest) {
        log.info("UserId --> {}", createFactorRequest.getUserId());
        log.info("User First Name --> {}", createFactorRequest.getUserName());
        NewFactor factor = NewFactor.creator(VERIFY_SERVICE_SID, createFactorRequest.getUserId(), createFactorRequest.getUserName(), NewFactor.FactorTypes.TOTP).create();
        log.info("API Response --> {}", factor);
        return factor.getBinding();
    }
    public Factor.FactorStatuses verifyFactor(String userId, String factorSid, String authPayload) {
        log.info("UserId --> {}", userId);
        log.info("Factor Sid--> {}", factorSid);
        log.info("Auth Payload --> {}", authPayload);
        Factor factor = Factor.updater(VERIFY_SERVICE_SID, userId, factorSid).setAuthPayload(authPayload).update();
        log.info("API Response --> {}", factor);
        return factor.getStatus();
    }
    public Challenge.ChallengeStatuses verifyToken(String userId, String factorSid, String authPayload) {
        log.info("User Id --> {}", userId);
        log.info("Factor Id --> {}", factorSid);
        log.info("Auth Payload --> {}", authPayload);
        Challenge challenge = Challenge.creator(VERIFY_SERVICE_SID, userId, factorSid).setAuthPayload(authPayload).create();
        return challenge.getStatus();
    }
}

