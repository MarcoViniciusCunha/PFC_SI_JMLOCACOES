package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.AuthController;
import com.nozama.aluguel_veiculos.domain.User;
import com.nozama.aluguel_veiculos.dto.LoginResponse;
import com.nozama.aluguel_veiculos.dto.UserRequest;
import com.nozama.aluguel_veiculos.repository.UserRepository;
import com.nozama.aluguel_veiculos.security.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        UserRequest loginRequest = new UserRequest("user@example.com", "password123");
        User user = new User();
        user.setUsername("user@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("user@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwt.generateToken("user@example.com")).thenReturn("jwtToken123");

        ResponseEntity<?> response = authController.login(loginRequest);

        // imprimir no terminal
        System.out.println("Status: " + response.getStatusCodeValue());
        System.out.println("Body: " + response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof LoginResponse);
        assertEquals("jwtToken123", ((LoginResponse) response.getBody()).token());
    }

    @Test
    void testLoginUserNotFound() {
        UserRequest loginRequest = new UserRequest("unknown@example.com", "password123");

        when(userRepository.findByUsername("unknown@example.com")).thenReturn(null);

        ResponseEntity<?> response = authController.login(loginRequest);

        // imprimir no terminal
        System.out.println("Status: " + response.getStatusCodeValue());
        System.out.println("Body: " + response.getBody());

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Email ou senha inválidos.", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void testLoginInvalidPassword() {
        UserRequest loginRequest = new UserRequest("user@example.com", "wrongPassword");
        User user = new User();
        user.setUsername("user@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("user@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        ResponseEntity<?> response = authController.login(loginRequest);

        // imprimir no terminal
        System.out.println("Status: " + response.getStatusCodeValue());
        System.out.println("Body: " + response.getBody());

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Email ou senha inválidos.", ((Map<?, ?>) response.getBody()).get("message"));
    }
}
