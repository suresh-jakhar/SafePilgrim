package com.safepilgrim.digitalid.controller;

import com.safepilgrim.digitalid.dto.GenerateIdRequest;
import com.safepilgrim.digitalid.dto.GenerateIdResponse;
import com.safepilgrim.digitalid.dto.VerifyIdRequest;
import com.safepilgrim.digitalid.dto.VerifyIdResponse;
import com.safepilgrim.digitalid.dto.UpdateIdRequest;
import com.safepilgrim.digitalid.dto.UpdateIdResponse;
import com.safepilgrim.digitalid.service.DigitalIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class DigitalIdController {

    @Autowired
    private DigitalIdService digitalIdService;

    @PostMapping("/generate")
    public ResponseEntity<GenerateIdResponse> generateDigitalId(@RequestBody GenerateIdRequest request) {
        try {
            GenerateIdResponse response = digitalIdService.generateDigitalId(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyIdResponse> verifyDigitalId(@RequestBody VerifyIdRequest request) {
        try {
            VerifyIdResponse response = digitalIdService.verifyDigitalId(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateIdResponse> updateDigitalId(@RequestBody UpdateIdRequest request) {
        try {
            UpdateIdResponse response = digitalIdService.updateDigitalId(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Digital ID Service is healthy");
    }
}
