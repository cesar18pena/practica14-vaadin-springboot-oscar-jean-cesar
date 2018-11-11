package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringComponent
@UIScope
public class PantallaEvento extends VerticalLayout {
    DatePicker inicio = new DatePicker();
    DatePicker fin = new DatePicker();
    boolean editando = false;

    public PantallaEvento(@Autowired EventoService eventoService) {
        H3 header = new H3("Agregar Evento");
        inicio.setLabel("Selecciona un dia");
        inicio.setPlaceholder("Selecciona una fecha");
        inicio.setValue(LocalDate.now());

        fin.setLabel("Selecciona un dia");
        fin.setPlaceholder("Selecciona una fecha");
        fin.setValue(LocalDate.now());

        TextField titulo = new TextField("Titulo");

        TextArea descripcion = new TextArea("Descripcion");

        Button agregar = new Button("Agregar");
        Button cancelar = new Button("Cancelar");

        HorizontalLayout buttons = new HorizontalLayout(agregar, cancelar);
        buttons.setSpacing(true);

        if (!editando) {
            add(header, titulo, descripcion, inicio, fin, buttons);
        } else {
            add(header, titulo, descripcion, buttons);
        }

        agregar.addClickListener((evento) -> {
            Evento e = new Evento(
                    titulo.getValue(),
                    descripcion.getValue(),
                    false,
                    inicio.getValue().atStartOfDay(ZoneId.of("America/Los_Angeles")),
                    fin.getValue().atStartOfDay(ZoneId.of("America/Los_Angeles"))
            );

            try {
                eventoService.crearEvento(
                        e.getCaption(),
                        e.getDescription(),
                        false,
                        e.getStart(),
                        e.getEnd()
                );

            } catch (Exception exp) {

            }

            Principal.calendario.setItems(e);
        });
    }

    public void setDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        inicio.setValue(startDate.toLocalDate());
        fin.setValue(endDate.toLocalDate());
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }
}
