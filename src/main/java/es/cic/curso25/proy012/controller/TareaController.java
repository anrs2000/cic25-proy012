package es.cic.curso25.proy012.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import es.cic.curso25.proy012.modelo.Estado;
import es.cic.curso25.proy012.modelo.Tarea;
import es.cic.curso25.proy012.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TareaController.class);

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public List<Tarea> getAll() {
        LOGGER.info("Obteniendo todas las tareas desde la ruta /api/tareas");
        List<Tarea> tareas = tareaService.getAll();
        LOGGER.info(String.format("Total tareas obtenidas: %d", tareas.size()));
        return tareas;
    }

    @GetMapping("/{id}")
    public Tarea get(@PathVariable Long id) {
        LOGGER.info(String.format("Obteniendo tarea con id %d desde la ruta /api/tareas/%d", id, id));
        Tarea tarea = tareaService.get(id);
        LOGGER.info(String.format("Tarea obtenida: %s", tarea.toString()));
        return tarea;
    }

    @GetMapping("/{id}/estado")
    public Estado getEstado(@PathVariable Long id) {
        LOGGER.info(String.format("Obteniendo estado de la tarea con id %d desde la ruta /api/tareas/%d/estado", id, id));
        Estado estado = tareaService.getEstado(id);
        LOGGER.info(String.format("Estado obtenido: %s", estado.toString()));
        return estado;
    }

    @PostMapping
    public Tarea create(@RequestBody Tarea tarea) {
        LOGGER.info(String.format("Creando nueva tarea desde la ruta /api/tareas con datos: %s", tarea.toString()));
        return tareaService.create(tarea);
    }

    @PutMapping("/{id}/{estadoId}")
    public Tarea update(@PathVariable Long id,@PathVariable Long estadoId,
                        @RequestBody Tarea tareaActualizada) {
        LOGGER.info(String.format("Actualizando tarea con id %d y estadoId %d desde la ruta /api/tareas/%d", id, estadoId, id));
        Tarea tarea = tareaService.update(id, tareaActualizada, estadoId);
        LOGGER.info(String.format("Tarea actualizada: %s", tarea.toString()));
        return tarea;
    }

    @PatchMapping("/{id}/estado")
    public Tarea updateEstado(@PathVariable Long id,
                              @RequestParam Long estadoId) {
        LOGGER.info(String.format("Actualizando solo estado a %d de la tarea con id %d desde la ruta /api/tareas/%d/estado", estadoId, id, id));
        Tarea tarea = tareaService.updateEstado(id, estadoId);
        LOGGER.info(String.format("Estado actualizado en tarea: %s", tarea.toString()));
        return tarea;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        LOGGER.info(String.format("Obteniendo tarea con id %d para proceder a eliminarla desde la ruta /api/tareas/%d", id, id));
        tareaService.delete(id);
        LOGGER.info(String.format("Tarea con id %d eliminada correctamente", id));
    }
}
