package com.ccunarro.hostfully.services;

import com.ccunarro.hostfully.data.user.User;
import com.ccunarro.hostfully.data.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loggedInUser(Authentication auth) {
        UserDetails details = (UserDetails) auth.getDetails();
        String username = details.getUsername();
        return userRepository.findByUsername(username);
    }
}
