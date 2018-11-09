package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.stereotype.Component;

@Component
@UIScope
@SpringUI
public class PantallaAccionesGerente extends FormLayout {

    Button modificar = new Button("Modificar");
    Button eliminar = new Button("Eliminar");
    Button cancelar = new Button("Cancelar");

    public PantallaAccionesGerente() {
        setup();
    }

    private void setup() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        modificar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        modificar.setIcon(FontAwesome.PENCIL);

        eliminar.addStyleName(ValoTheme.BUTTON_DANGER);
        eliminar.setIcon(FontAwesome.TRASH);

        cancelar.addClickListener(e -> {
            ((Window) getParent()).close();
        });

        modificar.addClickListener(e -> {
            ((Window) getParent()).close();
        });

        HorizontalLayout buttons = new HorizontalLayout(modificar, eliminar, cancelar);
        buttons.setSpacing(true);
        addComponent(buttons);
    }
}