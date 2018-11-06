package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@UIScope
@SpringUI
public class PantallaEvento extends FormLayout {

    @Autowired
    EventoService eventoService;

    DateField inicio = new PopupDateField("Fecha de inicio");
    DateField fin = new PopupDateField("Fecha fin");

    TextField titulo = new TextField("Titulo");
    TextArea descripcion = new TextArea("Descripcion");

    Button agregar = new Button("Agregar");
    Button cancelar = new Button("Cancelar");

    public PantallaEvento(Date startDate, Date endDate) {
        inicio.setValue(startDate);
        fin.setValue(endDate);
        setup();
    }

    public PantallaEvento() {
        inicio.setValue(new Date());
        fin.setValue(new Date());
        setup();
    }

    private void setup() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);
        inicio.setResolution(Resolution.MINUTE);
        fin.setResolution(Resolution.MINUTE);
        agregar.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        agregar.addClickListener((evento) -> {
                Evento e = new Evento(titulo.getValue(), descripcion.getValue(), false, inicio.getValue(), fin.getValue());
                try {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    eventoService.crearEvento(e.getCaption(), e.getDescription(), false, sdf1.parse(e.getStart().toString()), sdf1.parse(e.getEnd().toString()));

                } catch (Exception exp){

                }
                Principal.calendario.addEvent(e);

                ((Window)getParent()).close();
        });

        cancelar.addClickListener(e -> {
                ((Window)getParent()).close();
        });

        HorizontalLayout buttons = new HorizontalLayout(agregar, cancelar);
        buttons.setSpacing(true);

        inicio.setCaption("Inicio:");
        fin.setCaption("Fin:");
        titulo.setCaption("Titulo:");
        descripcion.setCaption("Descripcion:");

        addComponents(titulo, descripcion, inicio, fin, buttons);
    }

    public void setDates(Date startDate, Date endDate) {
        inicio.setValue(startDate);
        fin.setValue(endDate);
    }
}

