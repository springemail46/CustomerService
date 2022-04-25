package com.spring.auth.entity;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID=392992920293883L;

    private final String jwtToken;

    public AuthenticationResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
