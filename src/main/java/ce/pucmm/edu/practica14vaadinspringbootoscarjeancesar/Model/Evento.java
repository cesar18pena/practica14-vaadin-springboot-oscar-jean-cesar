package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model;

import org.vaadin.calendar.CalendarItemTheme;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Evento implements Serializable {
    @GeneratedValue
    @Id
    private Long id;
    private Date fecha;
    private String titulo;
    private CalendarItemTheme color;

    public Evento() {
    }

    public Evento(Date fecha, String titulo, CalendarItemTheme color) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public CalendarItemTheme getColor() {
        return color;
    }

    public void setColor(CalendarItemTheme color) {
        this.color = color;
    }
}