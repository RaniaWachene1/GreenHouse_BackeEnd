package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceIMP implements IUser {
    @Autowired
    private UserRepository userRepository;


    @Override
    public User addUser(User user) {
        try {

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add user. Error: " + e.getMessage());
        }
    }


    @Override
    public User retrieveById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void updateUser(Long userId, User updateUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));


        if (updateUser.getEmail() != null) {
            existingUser.setEmail(updateUser.getEmail());
        }
        if (updateUser.getFirstName() != null) {
            existingUser.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null) {
            existingUser.setLastName(updateUser.getLastName());
        }

        userRepository.save(existingUser);
    }



    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }



    public void generatePasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // Token expiry in 1 hour
        userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user != null && user.getResetPasswordTokenExpiry().after(new Date())) {
            user.setPassword(newPassword);
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiry(null);
            userRepository.save(user);
            // Password reset successful, notify user
        } else {
            // Invalid or expired token, notify user
        }
    }




    @Override
    public boolean verifyEmail(String verificationToken) {
        User user = userRepository.findByVerificationToken(verificationToken);

        if (user != null) {
            // Mark the user's email as verified
            user.setVerified(true);
            // Clear the verification token
            user.setVerificationToken(null);
            // Save the updated user
            userRepository.save(user);
            return true; // Email verified successfully
        } else {
            return false; // Invalid verification token
        }
    }




















}