package com.ehizman.auth_app.user_management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser createNewUser(SignupForm signupForm) throws Exception {
        AppUser user = userRepository.findAppUserByUsername(signupForm.getUsername()).orElse(null);
        if (user != null){
            log.warn("A user already exists with username : {}", signupForm.getUsername());
            throw new Exception("A user already exists with username " + signupForm.getUsername());
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(signupForm.getUsername().trim());
        appUser.setPassword(passwordEncoder.encode(signupForm.getPassword().trim()));
        appUser.setUserId(UUID.randomUUID().toString());
        appUser.setI2FAEnabled(false);
        log.info("New User created");
        return userRepository.save(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findAppUserByUsername(username).orElse(null);
        if (user == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new User(user.getUsername(), user.getPassword(),  new ArrayList<>());
    }

    public AppUser findUserByUsername(String username) {
        return userRepository.findAppUserByUsername(username).orElse(null);
    }

    public void save(AppUser user) {
        userRepository.save(user);
    }
}
