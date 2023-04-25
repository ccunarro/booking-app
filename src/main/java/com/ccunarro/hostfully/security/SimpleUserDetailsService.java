package com.ccunarro.hostfully.security;

import com.ccunarro.hostfully.data.user.User;
import com.ccunarro.hostfully.data.user.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public SimpleUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SimpleUserDetails loadUserByUsername(String name) throws UsernameNotFoundException, DataAccessException {
        User user = userRepository.findByUsername(name);
        return new SimpleUserDetails(user);
    }

}
