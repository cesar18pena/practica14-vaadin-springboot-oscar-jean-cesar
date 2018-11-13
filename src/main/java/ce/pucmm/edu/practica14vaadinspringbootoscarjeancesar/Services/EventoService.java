package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Data.EventoRepository;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.calendar.CalendarItemTheme;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public List<Evento> encontrarEventoPorFecha(Date fecha) {
        return eventoRepository.findAllByFecha(fecha);
    }

    public List<Evento> encontrarEventosEnUnRango(Date fechaInicio, Date fechaFin) {
        return eventoRepository.findByDatesBetween(fechaInicio, fechaFin);
    }

    @Transactional
    public Evento crearEvento(Date fecha, String titulo, CalendarItemTheme color) {
        return eventoRepository.save(new Evento(fecha, titulo, color));
    }

    @Transactional
    public boolean borrarEvento(Evento evento) {
        eventoRepository.delete(evento);
        return true;
    }
}
