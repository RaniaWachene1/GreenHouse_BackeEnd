package com.greenhouse.gh_backend.payload.response;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    @Getter
    private Long idUser;
    @Getter
    private String email;
    @Getter
    private String firstName;
    @Getter
    private String lastName;

    @Getter
    private String companyName;
    @Getter
    private String sector;
    @Getter
    private String industry;
    @Getter
    private List<String> roles;
    @Getter
    private Float revenue;
    @Getter
    private String headquarters;
    @Getter
    private String currency;
    @Getter
    private boolean profileComplete;




    public JwtResponse(String token, Long idUser, String email, String firstName, String lastName, String companyName, String sector, String industry, List<String> roles, Float revenue, String headquarters, String currency, boolean profileComplete) {
        this.token = token;
        this.idUser = idUser;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.sector = sector;
        this.industry = industry;
        this.roles = roles;
        this.revenue = revenue;
        this.headquarters = headquarters;
        this.currency = currency;
        this.profileComplete = profileComplete;
    }

    public JwtResponse(String newAccessToken, String refreshToken) {
        // This constructor is used for refresh tokens
        this.token = newAccessToken;
        // Optionally handle the refreshToken if needed
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setProfileComplete(boolean profileComplete) {
        this.profileComplete = profileComplete;
    }

}