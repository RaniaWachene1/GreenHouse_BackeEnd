package com.greenhouse.gh_backend.restController;

import com.greenhouse.gh_backend.entities.Role;
import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.payload.request.LoginRequest;
import com.greenhouse.gh_backend.payload.request.SignupRequest;
import com.greenhouse.gh_backend.payload.response.JwtResponse;
import com.greenhouse.gh_backend.payload.response.MessageResponse;
import com.greenhouse.gh_backend.repositories.UserRepository;
import com.greenhouse.gh_backend.security.jwt.JwtUtils;
import com.greenhouse.gh_backend.security.services.UserDetailsImpl;
import com.greenhouse.gh_backend.services.EmailSenderService;
import com.greenhouse.gh_backend.services.UserServiceIMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Collections;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:4200", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceIMP userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailSenderService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Role role = userDetails.getUser().getRole();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getIdUser(), userDetails.getEmail(), Collections.singletonList(role.name())));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute SignupRequest signUpRequest) throws ParseException {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        String verificationToken = UUID.randomUUID().toString();
        String encodedPassword = encoder.encode(signUpRequest.getPassword());

        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail(), encodedPassword);
        user.setRole(Role.COMPANY);
        user.setVerificationToken(verificationToken);
        userRepository.save(user);

        emailSenderService.sendVerificationEmail(user, verificationToken);

        return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email for verification."));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new MessageResponse("Logged out successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        System.out.println("Received email: " + email);
        User user = userService.findByEmail(email);
        if (user != null) {
            userService.generatePasswordResetToken(user);
            emailService.sendPasswordResetEmail(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        userService.resetPassword(token, encodedPassword);
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String verificationToken) {
        boolean emailVerified = userService.verifyEmail(verificationToken);

        if (emailVerified) {
            return "Email verified successfully!";
        } else {
            return "Invalid verification token!";
        }
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getIdUser();
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String refreshToken = bearerToken.substring(7);
            // Process the token to generate a new one
            // Ensure you handle any exceptions such as an invalid token
        } else {
            return ResponseEntity.badRequest().body("Bearer token is missing or improperly formatted");
        }

        return ResponseEntity.ok().body("New access token");
    }
}
