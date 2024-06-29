package com.ehizman.auth_app.verification.service;

import com.ehizman.auth_app.verification.model.CreateFactorRequest;
import com.twilio.rest.verify.v2.service.entity.Challenge;
import com.twilio.rest.verify.v2.service.entity.Factor;

import java.util.Map;

public interface VerificationService {
    Map<String, Object> createFactor(CreateFactorRequest createFactorRequest);
    Factor.FactorStatuses verifyFactor(String userId, String factorSid, String authPayload);
    Challenge.ChallengeStatuses verifyToken(String userId, String factorSid, String authPayload);
}
