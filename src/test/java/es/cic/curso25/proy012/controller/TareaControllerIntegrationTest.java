package es.cic.curso25.proy012.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.cic.curso25.proy012.modelo.Estado;
import es.cic.curso25.proy012.modelo.Evolucion;
import es.cic.curso25.proy012.modelo.Tarea;
import es.cic.curso25.proy012.repository.EstadoRepository;
import es.cic.curso25.proy012.repository.TareaRepository;
import es.cic.curso25.proy012.service.TareaService;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TareaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TareaService tareaService;

    @Autowired
    TareaRepository tareaRepository;

    @Autowired
    EstadoRepository estadoRepository;

    private Estado estadoInicial;

    @BeforeEach
    void preparacion() {
        // Creamos un estado base para usar en las tareas
        estadoInicial = new Estado();
        estadoInicial.setEvolucion(Evolucion.EN_CURSO);
        // Guardamos para que tenga ID asignado
        estadoInicial = tareaService.saveEstado(estadoInicial);

        // Creamos una tarea base con ese estado
        Tarea tarea = new Tarea();
        tarea.setDescripcion("Tarea de prueba");
        tarea.setEstado(estadoInicial);

        tareaService.create(tarea);
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/tareas")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testGet() throws Exception {
        List<Tarea> tareas = tareaService.getAll();
        Long id = tareas.get(0).getId();

        mockMvc.perform(get("/api/tareas/" + id)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion", is("Tarea de prueba")));
    }

    @Test
    void testGetEstado() throws Exception {
        List<Tarea> tareas = tareaService.getAll();
        Long id = tareas.get(0).getId();

        mockMvc.perform(get("/api/tareas/" + id + "/estado")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.evolucion", is("EN_CURSO")));
    }


    @Test
    void testUpdateEstado() throws Exception {
        Tarea tarea = tareaService.getAll().get(0);
        Long id = tarea.getId();

        Estado nuevoEstado = new Estado();
        nuevoEstado.setEvolucion(Evolucion.FINALIZADO);
        nuevoEstado = tareaService.saveEstado(nuevoEstado); // Guarda para que tenga ID

        Long estadoId = nuevoEstado.getId();

        mockMvc.perform(patch("/api/tareas/" + id + "/estado")
                .contentType("application/json")
                .param("estadoId", String.valueOf(estadoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado.id", is(estadoId.intValue())));
    }

    @Test
    void testDelete() throws Exception {
        Tarea tarea = new Tarea();
        tarea.setDescripcion("Tarea a eliminar");
        tarea.setEstado(estadoInicial);
        Tarea tareaGuardada = tareaService.create(tarea);

        mockMvc.perform(delete("/api/tareas/" + tareaGuardada.getId())
                .contentType("application/json"))
                .andExpect(status().isOk()); // El controller devuelve void pero 200 OK

        // Ahora debe lanzar excepción porque ya no existe
        assertThrows(RuntimeException.class, () -> tareaService.get(tareaGuardada.getId()));
    }
}
