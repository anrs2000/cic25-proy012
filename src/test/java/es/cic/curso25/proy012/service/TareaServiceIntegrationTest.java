package es.cic.curso25.proy012.service;

import es.cic.curso25.proy012.modelo.Estado;
import es.cic.curso25.proy012.modelo.Evolucion;
import es.cic.curso25.proy012.modelo.Tarea;
import es.cic.curso25.proy012.repository.EstadoRepository;
import es.cic.curso25.proy012.repository.TareaRepository;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class TareaServiceIntegrationTest {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private TareaRepository tareaRepository;

    private Estado estado;
    private Tarea tarea;

    @BeforeEach
    void prepararDatos() {
        tareaRepository.deleteAll();
        estadoRepository.deleteAll();

        estado = new Estado();
        estado.setEvolucion(Evolucion.EN_CURSO);
        estado = estadoRepository.save(estado);

        tarea = new Tarea();
        tarea.setDescripcion("Tarea integración");
        tarea.setEstado(estado);
        tarea = tareaService.create(tarea);
    }

    @Test
    void testGet() {
        Tarea resultado = tareaService.get(tarea.getId());

        assertNotNull(resultado);
        assertEquals(tarea.getDescripcion(), resultado.getDescripcion());
    }

    @Test
    void testGetAll() {
        List<Tarea> tareas = tareaService.getAll();

        assertFalse(tareas.isEmpty());
        assertEquals(1, tareas.size());
    }

    @Test
    void testGetEstado() {
        Estado resultado = tareaService.getEstado(tarea.getId());

        assertNotNull(resultado);
        assertEquals(Evolucion.EN_CURSO, resultado.getEvolucion());
    }

    @Test
    void testCreate() {
        Tarea nueva = new Tarea();
        nueva.setDescripcion("Nueva tarea");
        nueva.setEstado(estado);

        Tarea guardada = tareaService.create(nueva);

        assertNotNull(guardada.getId());
        assertEquals("Nueva tarea", guardada.getDescripcion());
    }

     @Test
    void testUpdate() {
        // Crear y guardar estado inicial
        Estado estadoInicial = new Estado();
        estadoInicial.setEvolucion(Evolucion.EN_CURSO);
        estadoInicial = estadoRepository.save(estadoInicial);

        // Crear y guardar tarea original
        Tarea tarea = new Tarea();
        tarea.setDescripcion("Tarea original");
        tarea.setEstado(estadoInicial);
        tarea = tareaRepository.save(tarea);

        // Crear un nuevo estado para actualizar
        Estado nuevoEstado = new Estado();
        nuevoEstado.setEvolucion(Evolucion.FINALIZADO);
        nuevoEstado = estadoRepository.save(nuevoEstado);

        // Crear objeto con nueva descripción
        Tarea actualizada = new Tarea();
        actualizada.setDescripcion("Tarea modificada");

        // Llamar al servicio
        Tarea resultado = tareaService.update(tarea.getId(), actualizada, nuevoEstado.getId());

        // Verificar cambios
        assertEquals("Tarea modificada", resultado.getDescripcion());
        assertEquals(Evolucion.FINALIZADO, resultado.getEstado().getEvolucion());
    }


    @Test
    void testUpdateEstado() {
        Estado nuevoEstado = new Estado();
        nuevoEstado.setEvolucion(Evolucion.PENDIENTE);
        nuevoEstado = estadoRepository.save(nuevoEstado);

        Tarea resultado = tareaService.updateEstado(tarea.getId(), nuevoEstado.getId());

        assertEquals(Evolucion.PENDIENTE, resultado.getEstado().getEvolucion());
    }

    @Test
    void testDelete() {
        tareaService.delete(tarea.getId());

        assertTrue(tareaRepository.findById(tarea.getId()).isEmpty());
    }
}
