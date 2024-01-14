package CashGroz.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import CashGroz.dto.UserDto;
import CashGroz.models.Role;
import CashGroz.models.User;
import CashGroz.repositories.RoleRepository;
import CashGroz.repositories.UserRepository;

@Service
public class UserDetail implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with username: %s does not exist", username));
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public User registerUser(UserDto userDto) {
        // check if user exists
        User existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser != null) {
            return existingUser;
        }
        // creating user object
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);

        return null;
    }
}
