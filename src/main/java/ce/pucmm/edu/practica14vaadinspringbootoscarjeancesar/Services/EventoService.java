package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Data.EventoRepository;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Evento> encontrarEventoPorFecha(Date start, Date end) {
        return eventoRepository.findAllByStartAndEnd(start, end);
    }

    public List<Evento> encontrarEventosEnUnRango(Date startDate, Date endDate) {
        return eventoRepository.findByDatesBetween(startDate, endDate);
    }

    @Transactional
    public Evento crearEvento(String caption, String description, boolean isAllDay, Date start, Date end) {
        return eventoRepository.save(new Evento(caption, description, isAllDay, start, end));
    }

    @Transactional
    public boolean borrarEvento(Evento customEvent) {
        eventoRepository.delete(customEvent);
        return true;
    }

}
