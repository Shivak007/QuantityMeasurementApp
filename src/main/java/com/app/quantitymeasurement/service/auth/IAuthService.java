package com.app.quantitymeasurement.service.auth;

import com.app.quantitymeasurement.model.auth.*;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse googleLogin(GoogleLoginRequest request);
}
