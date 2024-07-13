package com.greenhouse.gh_backend.security.services;

import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Transactional
    public UserDetails loadUserByDetails(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

        return UserDetailsImpl.build(user);
    }

    public User getUserFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserDetailsImpl) {
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
            return userDetailsImpl.getUser();
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}