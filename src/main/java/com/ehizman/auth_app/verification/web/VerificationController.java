package com.ehizman.auth_app.verification.web;

import com.ehizman.auth_app.verification.service.VerificationService;
import com.ehizman.auth_app.verification.model.CreateFactorRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verify")
public class VerificationController {
    private final VerificationService verificationService;
    private final Logger log = LoggerFactory.getLogger(VerificationController.class);

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/create/factor")
    public ResponseEntity<?> createFactor(@Valid @RequestBody CreateFactorRequest createFactorRequest){
        log.info("Create Factor Request --> {}", createFactorRequest);
        Map<String, Object> binding = verificationService.createFactor(createFactorRequest);
        return new ResponseEntity<>(binding, HttpStatus.OK);
    }
}
