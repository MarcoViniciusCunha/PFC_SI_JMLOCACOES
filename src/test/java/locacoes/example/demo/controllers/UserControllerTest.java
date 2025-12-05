package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.domain.User;
import com.nozama.aluguel_veiculos.dto.UserRequest;
import com.nozama.aluguel_veiculos.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private com.nozama.aluguel_veiculos.controllers.UserController userController;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User("user1@example.com", "123456");
        user2 = new User("user2@example.com", "abcdef");
    }

    @Test
    void testCreateUser_Success() throws Exception {
        UserRequest request = new UserRequest("newuser@example.com", "password");
        User createdUser = new User("newuser@example.com", "password");

        when(userService.create(request)).thenReturn(createdUser);

        ResponseEntity<?> response = userController.createUser(request);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof User);
        assertEquals("newuser@example.com", ((User) response.getBody()).getUsername());

        System.out.println("Created User: " + ((User) response.getBody()).getUsername());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

        response.getBody().forEach(u -> System.out.println("User: " + u.getUsername()));
    }
}
