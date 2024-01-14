package CashGroz;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
        // Arrange
        String username = "existingUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        // Mock the UserRepository to return the user when findByUsername is called
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        UserDetails userDetails = userDetail.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsException() {
        // Arrange
        String username = "nonExistentUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act/Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetail.loadUserByUsername(username));
    }

    @Test
    void registerUser_UserDoesNotExist_ReturnsUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("newUser");
        userDto.setPassword("password");

        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role()));

        // Act
        User registeredUser = userDetail.registerUser(userDto);

        // Assert
        assertNull(registeredUser);
    }

    @Test
    void registerUser_UserExists_ReturnsNull() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("existingUser");
        userDto.setPassword("password");

        when(userRepository.findByUsername("existingUser")).thenReturn(new User());

        // Act
        User registeredUser = userDetail.registerUser(userDto);

        // Assert
        assertNull(registeredUser);
    }

    @Test
    void loadUserByUsername_EmptyUsername_ThrowsException() {
        // Act/Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetail.loadUserByUsername(""));
    }

    @Test
    void registerUser_NullDto_ThrowsException() {
        // Act/Assert
        assertThrows(IllegalArgumentException.class, () -> userDetail.registerUser(null));
    }

    @Test
    void registerUser_NullUsername_ThrowsException() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPassword("password");
    
        // Act/Assert
        assertThrows(IllegalArgumentException.class, () -> userDetail.registerUser(userDto));
    }

    @Test
    void registerUser_NullPassword_ThrowsException() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("newUser");

        // Act/Assert
        assertThrows(IllegalArgumentException.class, () -> userDetail.registerUser(userDto));
    }
    
}

