package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.User;
import com.nozama.aluguel_veiculos.dto.UserRequest;
import com.nozama.aluguel_veiculos.repository.UserRepository;
import com.nozama.aluguel_veiculos.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccessfully() {
        UserRequest request = new UserRequest("murillo", "123456");
        User user = new User();
        user.setUsername("murillo");
        user.setPassword("encoded_123456");

        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode("123456")).thenReturn("encoded_123456");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(request);

        assertEquals("murillo", result.getUsername());
        assertEquals("encoded_123456", result.getPassword());

        System.out.println("Esperado username: murillo | Obtido: " + result.getUsername());
        System.out.println("Esperado password codificada: encoded_123456 | Obtido: " + result.getPassword());
    }

    @Test
    void testCreateUserAlreadyExists() {
        UserRequest request = new UserRequest("murillo", "123456");

        when(userRepository.count()).thenReturn(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.create(request));

        System.out.println("Esperado erro: Já existe um usuário cadastrado | Recebido: " + ex.getMessage());
    }

    @Test
    void testGetUsers() {
        User user1 = new User();
        user1.setUsername("murillo");
        User user2 = new User();
        user2.setUsername("joao");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAll();

        assertEquals(2, result.size());
        System.out.println("📋 Esperado 2 usuários | Obtido: " + result.size());
        result.forEach(u -> System.out.println("👤 Usuário encontrado: " + u.getUsername()));
    }

    @Test
    void testGetUserByEmail() {
        User user = new User();
        user.setUsername("murillo");

        when(userRepository.findByUsername("murillo")).thenReturn(user);

        User result = userService.getByUsername("murillo");

        assertNotNull(result);
        assertEquals("murillo", result.getUsername());

        System.out.println("🔎 Esperado username: murillo | Obtido: " + result.getUsername());
    }
}
