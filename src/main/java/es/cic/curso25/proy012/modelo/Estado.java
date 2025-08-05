package es.cic.curso25.proy012.modelo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Evolucion evolucion;

    @OneToMany(mappedBy = "estado", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Tarea> tareas = new ArrayList<>();

    public void addTarea(Tarea tarea) {
        this.tareas.add(tarea);
        tarea.setEstado(this);
    }

    public void removeTarea(Tarea tarea){
        tareas.remove(tarea);
        tarea.setEstado(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evolucion getEvolucion() {
        return evolucion;
    }

    public void setEvolucion(Evolucion evolucion) {
        this.evolucion = evolucion;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Estado other = (Estado) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Estado [id=" + id + ", evolucion=" + evolucion + ", tareas=" + tareas + "]";
    }

}
