package com.greenhouse.gh_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;
    private String firstName;
    private String lastName;
    private String companyName;
    private String sector;
    private String industry;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String resetPasswordToken;
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetPasswordTokenExpiry;
    private boolean verified;
    private String verificationToken;

    public User() {}

    public User(String firstName, String lastName, String email, String encodedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = encodedPassword;
        this.verified = false; // Default to false until verified
    }
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
