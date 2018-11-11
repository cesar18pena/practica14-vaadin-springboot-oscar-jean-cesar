package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class PantallaEvento extends FormLayout {
//    public PantallaEvento(@Autowired EventoService eventoService) {
//        DatePicker inicio = new DatePicker();
//        inicio.setLabel("Selecciona un dia");
//        inicio.setPlaceholder("Selecciona una fecha");
//
//        DatePicker fin = new DatePicker();
//        fin.setLabel("Selecciona un dia");
//        fin.setPlaceholder("Selecciona una fecha");
//
//        inicio.setValue(LocalDate.now());
//        fin.setValue(LocalDate.now());
//
//        boolean editando = false;
//
//        TextField titulo = new TextField("Titulo");
//        TextArea descripcion = new TextArea("Descripcion");
//
//        Button agregar = new Button("Agregar");
//        Button cancelar = new Button("Cancelar");
//
//
//
//        setSizeUndefined();
//
////        inicio.setResolution(DateResolution.DAY);
////        fin.setResolution(DateResolution.DAY);
////        agregar.setClickShortcut(ShortcutAction.KeyCode.ENTER);
//
//        agregar.addClickListener((evento) -> {
//            Evento e = new Evento(titulo.getValue(), descripcion.getValue(), false, inicio.getValue().atStartOfDay(ZoneId.of("America/La Paz")), fin.getValue().atStartOfDay(ZoneId.of("America/La Paz")));
//
//            try {
//                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//
//                eventoService.crearEvento(
//                        e.getCaption(),
//                        e.getDescription(),
//                        false,
//                        e.getStart(),
//                        e.getEnd()
////                        sdf1.parse(e.getStart().toString()),
////                        sdf1.parse(e.getEnd().toString())
//                );
//
//            } catch (Exception exp) {
//
//            }
//
////            Principal.calendario.add(e);
//
//            Principal.dataProvider.refreshAll();
//            getUI().get().close();
//        });
//
//        cancelar.addClickListener(e -> {
//            getUI().get().close();
//        });
//
//        HorizontalLayout buttons = new HorizontalLayout(agregar, cancelar);
//        buttons.setSpacing(true);
//
//        inicio.setLabel("Inicio:");
//
//        fin.setLabel("Fin:");
//        titulo.setLabel("Titulo:");
//        descripcion.setLabel("Descripcion:");
//
//        if (!editando) {
//            add(titulo, descripcion, inicio, fin, buttons);
//        } else {
//            add(titulo, descripcion, buttons);
//        }
//    }
//
    public void setDates(ZonedDateTime startDate, ZonedDateTime endDate) {
//        inicio.setValue(startDate.toLocalDate());
//        fin.setValue(endDate.toLocalDate());
    }

    public void setEditando(boolean editando) {
//        this.editando = editando;
    }
}
