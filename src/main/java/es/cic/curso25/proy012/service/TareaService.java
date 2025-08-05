package es.cic.curso25.proy012.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso25.proy012.exception.NotFoundException;
import es.cic.curso25.proy012.modelo.Estado;
import es.cic.curso25.proy012.modelo.Tarea;
import es.cic.curso25.proy012.repository.EstadoRepository;
import es.cic.curso25.proy012.repository.TareaRepository;

@Service
public class TareaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TareaService.class);

    @Autowired
    TareaRepository tareaRepository;

    @Autowired
    EstadoRepository estadoRepository;

    // Obtener una tarea por id
    public Tarea get(Long id) {
        LOGGER.info(String.format("Buscando tarea con id: %d", id));
        Optional<Tarea> tarea = tareaRepository.findById(id);

        if (tarea.isEmpty()) {
            LOGGER.warn(String.format("No existe ninguna tarea con el id %d", id));
            throw new NotFoundException(String.format("No existe ninguna tarea con el id %d", id));
        }

        LOGGER.info(String.format("Tarea encontrada: %s", tarea.get()));
        return tarea.get();
    }

    // Obtener todas las tareas
    public List<Tarea> getAll() {
        LOGGER.info("Obteniendo todas las tareas");
        List<Tarea> tareas = tareaRepository.findAll();
        LOGGER.info(String.format("Total tareas encontradas: %d", tareas.size()));
        return tareas;
    }

    // Obtener estado asociado a tarea
    public Estado getEstado(Long tareaId) {
        LOGGER.info(String.format("Obteniendo estado de la tarea con id: %d", tareaId));
        Tarea tarea = get(tareaId);
        Estado estado = tarea.getEstado();
        LOGGER.info(String.format("Estado obtenido: %s", estado));
        return estado;
    }

    // Actualizar toda la tarea (incluye estado)
    public Tarea update(Long id, Tarea tareaActualizada, Long estadoId) {
        LOGGER.info(String.format("Actualizando tarea id: %d con nuevos datos y estado id: %d", id, estadoId));
        Tarea tarea = get(id);

        tarea.setDescripcion(tareaActualizada.getDescripcion());

        Estado estado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> {
                    LOGGER.error(String.format("No existe estado con id %d", estadoId));
                    return new RuntimeException("No existe estado con id " + estadoId);
                });
        tarea.setEstado(estado);

        Tarea tareaGuardada = tareaRepository.save(tarea);
        LOGGER.info(String.format("Tarea actualizada: %s", tareaGuardada));
        return tareaGuardada;
    }

    // Actualizar solo estado de tarea
    public Tarea updateEstado(Long tareaId, Long estadoId) {
        LOGGER.info(
                String.format("Actualizando solo el estado de la tarea id: %d al estado id: %d", tareaId, estadoId));
        Tarea tarea = get(tareaId);

        Estado estado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> {
                    LOGGER.error(String.format("No existe estado con id %d", estadoId));
                    return new RuntimeException("No existe estado con id " + estadoId);
                });

        tarea.setEstado(estado);
        Tarea tareaGuardada = tareaRepository.save(tarea);
        LOGGER.info(String.format("Estado actualizado en tarea: %s", tareaGuardada));
        return tareaGuardada;
    }

    //Guardar una nueva tarea
    public Tarea create(Tarea tarea) {
        LOGGER.info("Creando una nueva tarea en la BD");
        return tareaRepository.save(tarea);
    }

    // Guardar estado
    public Estado saveEstado(Estado estado) {
        LOGGER.info(String.format("Guardando estado: %s", estado));
        Estado estadoGuardado = estadoRepository.save(estado);
        LOGGER.info(String.format("Estado guardado: %s", estadoGuardado));
        return estadoGuardado;
    }

    // Eliminar tarea
    public void delete(Long id) {
        LOGGER.info(String.format("Eliminando tarea con id: %d", id));
        Tarea tarea = get(id);
        tareaRepository.delete(tarea);
        LOGGER.info(String.format("Tarea eliminada con id: %d", id));
    }

}
