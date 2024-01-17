package CashGroz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import CashGroz.dto.UserDto;
import CashGroz.models.Role;
import CashGroz.models.User;
import CashGroz.repositories.RoleRepository;
import CashGroz.repositories.UserRepository;
import CashGroz.services.UserDetail;

@SpringBootTest
class UserDetailTest {

    @Autowired
    private UserDetail userDetail;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        String username = "existingUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(username)).thenReturn(user);

        UserDetails userDetails = userDetail.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsException() {
        String username = "nonExistentUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetail.loadUserByUsername(username));
    }

    @Test
    void registerUser_UserDoesNotExist_ReturnsUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("newUser");
        userDto.setPassword("password");

        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role()));

        User registeredUser = userDetail.registerUser(userDto);

        assertNull(registeredUser);
    }

    @Test
    void registerUser_UserExists_ReturnsUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("existingUser");
        userDto.setPassword("password");

        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("existingUser")).thenReturn(user);

        User registeredUser = userDetail.registerUser(userDto);

        assertEquals(registeredUser, user);
    }

    @Test
    void loadUserByUsername_EmptyUsername_ThrowsException() {
        assertThrows(UsernameNotFoundException.class, () -> userDetail.loadUserByUsername(""));
    }

    @Test
    void registerUser_NullDto_ThrowsException() {
        assertThrows(NullPointerException.class, () -> userDetail.registerUser(null));
    }

    @Test
    void registerUser_NullPassword_ThrowsException() {
        UserDto userDto = new UserDto();
        userDto.setUsername("newUser");

        assertThrows(IllegalArgumentException.class, () -> userDetail.registerUser(userDto));
    }

}