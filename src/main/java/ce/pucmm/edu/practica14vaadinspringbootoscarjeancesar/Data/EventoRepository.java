package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Data;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findAllByStartAndEnd(Date start, Date end);

    @Query("select evento from Evento evento where evento.start between ?1 and ?2 and evento.end between ?1 and ?2")
    List<Evento> findByDatesBetween(LocalDate startDate, LocalDate endDate);
}
