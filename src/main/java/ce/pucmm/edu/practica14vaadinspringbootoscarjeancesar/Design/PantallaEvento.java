package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SpringComponent
@UIScope
public class PantallaEvento extends VerticalLayout {
    DatePicker inicio = new DatePicker();
    DatePicker fin = new DatePicker();
    boolean editando = false;

    public PantallaEvento(@Autowired EventoService eventoService) {
        FormLayout formLayout = new FormLayout();

        H3 header = new H3("Agregar Evento");

        inicio.setLabel("Selecciona el dia de inicio");
        inicio.setPlaceholder("Selecciona una fecha");
        inicio.setValue(LocalDate.now());

        fin.setLabel("Selecciona el dia de fin");
        fin.setPlaceholder("Selecciona una fecha");
        fin.setValue(LocalDate.now());

        TextField titulo = new TextField("Titulo");

        TextArea descripcion = new TextArea("Descripcion");

        Button agregar = new Button("Agregar");
        agregar.setIcon(new Icon(VaadinIcon.DATABASE));
        agregar.getElement().setAttribute("theme", "primary");

        Button cancelar = new Button("Cancelar");
        cancelar.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        cancelar.getElement().setAttribute("theme", "error");

        HorizontalLayout botones = new HorizontalLayout(agregar, cancelar);
        botones.setSpacing(true);
        setAlignItems(Alignment.CENTER);

        if (!editando) {
            formLayout.add(titulo, descripcion, inicio, fin);
        } else {
            formLayout.add(titulo, descripcion);
        }

        add(header, formLayout, botones);

        agregar.addClickListener((evento) -> {
            Evento e = new Evento(
                    titulo.getValue(),
                    descripcion.getValue(),
                    false,
                    inicio.getValue().atStartOfDay(ZoneId.systemDefault()),
                    fin.getValue().atStartOfDay(ZoneId.systemDefault())
            );

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

            try {
                eventoService.crearEvento(
                        e.getCaption(),
                        e.getDescription(),
                        false,
                        e.getStart(),
                        e.getEnd()
                );

                titulo.setValue("");
                descripcion.setValue("");
                inicio.setValue(LocalDate.now());
                fin.setValue(LocalDate.now());
            } catch (Exception exp) {
                exp.printStackTrace();
            }

            Principal.calendario.refresh();
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
