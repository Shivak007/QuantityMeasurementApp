package com.app.quantitymeasurement.config.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

/**
 * Verifies Google ID tokens by calling Google's tokeninfo endpoint.
 * This avoids the need for Google OAuth2 client libraries while remaining production-safe.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleTokenVerifier {

    @Value("${app.google.client-id}")
    private String googleClientId;

    private static final String GOOGLE_TOKEN_INFO_URL =
            "https://oauth2.googleapis.com/tokeninfo?id_token=";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Validates a Google ID token and returns the user's profile info.
     * @param idToken the raw Google ID token from the frontend
     * @return GoogleUserInfo if valid
     * @throws IllegalArgumentException if token is invalid or aud mismatch
     */
    public GoogleUserInfo verify(String idToken) {
        try {
            String url = GOOGLE_TOKEN_INFO_URL + idToken;
            String json = restTemplate.getForObject(url, String.class);
            JsonNode node = objectMapper.readTree(json);

            if (node.has("error_description")) {
                throw new IllegalArgumentException("Google token invalid: " + node.get("error_description").asText());
            }

            String aud = node.get("aud").asText();
            if (!aud.equals(googleClientId)) {
                throw new IllegalArgumentException("Google token audience mismatch");
            }

            return GoogleUserInfo.builder()
                    .sub(node.get("sub").asText())
                    .email(node.get("email").asText())
                    .name(node.has("name") ? node.get("name").asText() : node.get("email").asText())
                    .emailVerified(node.has("email_verified") && node.get("email_verified").asBoolean())
                    .build();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Google token verification failed", e);
            throw new IllegalArgumentException("Could not verify Google token: " + e.getMessage());
        }
    }
}
