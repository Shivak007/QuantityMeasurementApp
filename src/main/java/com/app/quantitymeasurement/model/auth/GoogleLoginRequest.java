package com.app.quantitymeasurement.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {
    /** Google ID token obtained from frontend Google Sign-In */
    @NotBlank private String idToken;
}
