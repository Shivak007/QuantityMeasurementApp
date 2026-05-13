package com.app.quantitymeasurement.config.google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class GoogleUserInfo {
    private String sub;
    private String email;
    private String name;
    private boolean emailVerified;
}
