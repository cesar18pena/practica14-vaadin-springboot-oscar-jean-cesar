package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@UIScope
@SpringUI
public class PantallaEvento extends FormLayout {

    @Autowired
    EventoService eventoService;

    DateField inicio = new DateField("Fecha de inicio");
    DateField fin = new DateField("Fecha fin");

    TextField titulo = new TextField("Titulo");
    TextArea descripcion = new TextArea("Descripcion");

    Button agregar = new Button("Agregar");
    Button cancelar = new Button("Cancelar");
    boolean editando = false;

    public PantallaEvento(LocalDate startDate, LocalDate endDate) {
        inicio.setValue(startDate);
        fin.setValue(endDate);
        setup();
    }

    public PantallaEvento() {
        inicio.setValue(LocalDate.now());
        fin.setValue(LocalDate.now());
        setup();
    }

    private void setup() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        inicio.setResolution(DateResolution.DAY);
        fin.setResolution(DateResolution.DAY);
        agregar.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        agregar.addClickListener((evento) -> {
            Evento e = new Evento(titulo.getValue(), descripcion.getValue(), false, inicio.getValue().atStartOfDay(ZoneId.of("America/La Paz")), fin.getValue().atStartOfDay(ZoneId.of("America/La Paz")));

            try {
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

                eventoService.crearEvento(
                        e.getCaption(),
                        e.getDescription(),
                        false,
                        e.getStart(),
                        e.getEnd()
//                        sdf1.parse(e.getStart().toString()),
//                        sdf1.parse(e.getEnd().toString())
                );

            } catch (Exception exp) {

            }

//            Principal.calendario.add(e);

            Principal.dataProvider.refreshAll();
            ((Window) getParent()).close();
        });

        cancelar.addClickListener(e -> {
            ((Window) getParent()).close();
        });

        HorizontalLayout buttons = new HorizontalLayout(agregar, cancelar);
        buttons.setSpacing(true);

        inicio.setCaption("Inicio:");

        fin.setCaption("Fin:");
        titulo.setCaption("Titulo:");
        descripcion.setCaption("Descripcion:");

        if (!editando) {
            addComponents(titulo, descripcion, inicio, fin, buttons);
        } else {
            addComponents(titulo, descripcion, buttons);
        }
    }

    public void setDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        inicio.setValue(startDate.toLocalDate());
        fin.setValue(endDate.toLocalDate());
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }
}
