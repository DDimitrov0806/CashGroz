package CashGroz.services;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with username: %s does not exist", username));
        }
        Set<GrantedAuthority> authorities = user.getRoles().stream()
            .map((role) -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    public void registerUser(UserDto userDto) throws InvalidParameterException{
        // check if user exists
        User existingUser = userRepository.findByUsername(userDto.getUsername());
        if(existingUser != null) {
            throw new InvalidParameterException("Username already exists!");
        }
        // creating user object
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
    }
}
