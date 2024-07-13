package com.greenhouse.gh_backend.payload.response;

import lombok.Getter;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    @Getter
    private Long idUser;
    @Getter
    private String email;
    @Getter
    private List<String> roles;

    public JwtResponse(String accessToken, Long idUser, String email, List<String> roles) {
        this.token = accessToken;
        this.idUser = idUser;
        this.email = email;
        this.roles = roles;
    }

    public JwtResponse(String newAccessToken, String refreshToken) {
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}