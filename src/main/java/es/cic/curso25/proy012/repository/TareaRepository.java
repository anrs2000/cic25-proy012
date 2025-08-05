package es.cic.curso25.proy012.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.cic.curso25.proy012.modelo.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long>{

}
