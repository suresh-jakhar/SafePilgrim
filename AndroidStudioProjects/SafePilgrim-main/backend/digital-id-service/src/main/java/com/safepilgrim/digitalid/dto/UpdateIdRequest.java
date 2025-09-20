package com.safepilgrim.digitalid.dto;

import java.util.Map;

public class UpdateIdRequest {
    private String digitalId;
    private Map<String, Object> updates;

    public UpdateIdRequest() {}

    public UpdateIdRequest(String digitalId, Map<String, Object> updates) {
        this.digitalId = digitalId;
        this.updates = updates;
    }

    public String getDigitalId() { return digitalId; }
    public void setDigitalId(String digitalId) { this.digitalId = digitalId; }

    public Map<String, Object> getUpdates() { return updates; }
    public void setUpdates(Map<String, Object> updates) { this.updates = updates; }
}
