package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.ColorController;
import com.nozama.aluguel_veiculos.domain.Color;
import com.nozama.aluguel_veiculos.dto.ColorRequest;
import com.nozama.aluguel_veiculos.repository.ColorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ColorControllerTest {

    @Mock
    private ColorRepository repository;

    @InjectMocks
    private ColorController colorController;

    private Color color1;
    private Color color2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        color1 = new Color();
        color1.setId(1);
        color1.setNome("Vermelho");

        color2 = new Color();
        color2.setId(2);
        color2.setNome("Azul");
    }

    @Test
    void testCreateColor() {
        ColorRequest request = new ColorRequest("Verde");
        Color created = new Color();
        created.setId(3);
        created.setNome("Verde");

        when(repository.existsByNome("Verde")).thenReturn(false);
        when(repository.save(any(Color.class))).thenReturn(created);

        ResponseEntity<?> response = colorController.create(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Color);
        assertEquals("Verde", ((Color) response.getBody()).getNome());

        System.out.println("Created Color: " + ((Color) response.getBody()).getNome() + " (ID: " + ((Color) response.getBody()).getId() + ")");
    }

    @Test
    void testCreateColorAlreadyExists() {
        ColorRequest request = new ColorRequest("Vermelho");
        when(repository.existsByNome("Vermelho")).thenReturn(true);

        ResponseEntity<?> response = colorController.create(request);

        assertEquals(409, response.getStatusCodeValue());
        System.out.println(response.getBody());
    }

    @Test
    void testGetAllColors() {
        List<Color> colors = Arrays.asList(color1, color2);
        when(repository.findAll()).thenReturn(colors);

        ResponseEntity<List<Color>> response = colorController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

        response.getBody().forEach(c -> System.out.println("Color ID: " + c.getId() + ", Name: " + c.getNome()));
    }

    @Test
    void testGetByIdFound() {
        when(repository.findById(1)).thenReturn(Optional.of(color1));

        ResponseEntity<Color> response = colorController.getById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Vermelho", response.getBody().getNome());

        System.out.println("GetById Color: " + response.getBody().getNome() + " (ID: " + response.getBody().getId() + ")");
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<Color> response = colorController.getById(99);

        assertEquals(404, response.getStatusCodeValue());
        System.out.println("Color not found for ID 99");
    }

    @Test
    void testUpdateColor() {
        Color updatedColor = new Color();
        updatedColor.setId(1);
        updatedColor.setNome("Amarelo");

        when(repository.findById(1)).thenReturn(Optional.of(color1));
        when(repository.save(any(Color.class))).thenReturn(updatedColor);

        ResponseEntity<Color> response = colorController.update(1, updatedColor);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Amarelo", response.getBody().getNome());

        System.out.println("Updated Color: " + response.getBody().getNome() + " (ID: " + response.getBody().getId() + ")");
    }

    @Test
    void testDeleteColor() {
        when(repository.findById(1)).thenReturn(Optional.of(color1));
        doNothing().when(repository).deleteById(1);

        ResponseEntity<Void> response = colorController.delete(1);

        assertEquals(200, response.getStatusCodeValue());
        System.out.println("Deleted Color ID: 1");
    }

    @Test
    void testDeleteColorNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = colorController.delete(99);

        assertEquals(404, response.getStatusCodeValue());
        System.out.println("Color not found for ID 99");
    }
}
