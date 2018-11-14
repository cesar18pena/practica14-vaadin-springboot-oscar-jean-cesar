package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Data.EventoRepository;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.calendar.CalendarItemTheme;

import javax.persistence.PersistenceException;
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

    public Evento encontrarEventoPorID(long id){
        return eventoRepository.getOne(id);
    }

    @Transactional
    public Evento crearEvento(long id, Date fecha, String titulo, CalendarItemTheme color) {
        return eventoRepository.save(new Evento(id, fecha, titulo, color));
    }

    public void editarEvento(long EventoID) throws Exception {
        try {
            Evento evento = encontrarEventoPorID(EventoID);
            eventoRepository.save(evento);
        } catch (PersistenceException e) {
            throw new PersistenceException("Hubo un error al editar el evento.");
        } catch (NullPointerException e) {
            throw new NullPointerException("Al editar el evento hubo un error de datos nulos.");
        } catch (Exception e) {
            throw new Exception("Hubo un error general al editar un evento.");
        }
    }

    @Transactional
    public boolean borrarEvento(Evento evento) {
        eventoRepository.delete(evento);
        return true;
    }
}
