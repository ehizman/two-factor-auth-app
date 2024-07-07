package com.ehizman.auth_app.verification;

import com.ehizman.auth_app.user_management.AppUser;
import com.ehizman.auth_app.user_management.UserService;
import com.twilio.rest.verify.v2.service.entity.Challenge;
import com.twilio.rest.verify.v2.service.entity.Factor;
import com.twilio.rest.verify.v2.service.entity.NewFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VerificationService {
    private Logger log = LoggerFactory.getLogger(VerificationService.class);
    private Environment env;
    private final String VERIFY_SERVICE_SID;
    private final UserService userService;

    public VerificationService(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
        VERIFY_SERVICE_SID = env.getProperty("VERIFY_SERVICE_SID");
    }
    public Map<String, Object> createFactor(String  username) throws Exception {
        log.info("Username --> {}", username);
        AppUser user = userService.findUserByUsername(username);
        if (user == null){
            throw new Exception("user does not exist");
        }
        String userId = user.getUserId();
        log.info("UserId --> {}", userId);
        NewFactor factor = NewFactor.creator(VERIFY_SERVICE_SID, userId, username, NewFactor.FactorTypes.TOTP).create();
        log.info("API Response --> {}", factor);
        user.set_2faFactorSid(factor.getSid());
        userService.save(user);
        return factor.getBinding();
    }
    public boolean verifyFactor(String username, String authCode) {
        AppUser user = userService.findUserByUsername(username);
        Factor factor = Factor.updater(VERIFY_SERVICE_SID, user.getUserId(), user.get_2faFactorSid()).setAuthPayload(authCode).update();
        log.info("API Response --> {}", factor);
        if (factor.getStatus().toString().equalsIgnoreCase("verified")){
            user.setI2FAEnabled(true);
            userService.save(user);
            return true;
        } else{
            return false;
        }
    }
    public boolean verifyToken(String username, String authPayload) {
        AppUser user = userService.findUserByUsername(username);
        Challenge challenge = Challenge.creator(VERIFY_SERVICE_SID, user.getUserId(), user.get_2faFactorSid()).setAuthPayload(authPayload).create();
        return challenge.getStatus().toString().equalsIgnoreCase("approved");
    }
}

