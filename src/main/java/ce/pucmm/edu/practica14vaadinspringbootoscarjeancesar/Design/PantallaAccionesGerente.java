package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class PantallaAccionesGerente extends VerticalLayout {

    Button modificar = new Button("Modificar");
    Button eliminar = new Button("Eliminar");

    public PantallaAccionesGerente() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        modificar.setIcon(new Icon(VaadinIcon.PENCIL));

        eliminar.setIcon(new Icon(VaadinIcon.TRASH));

        HorizontalLayout buttons = new HorizontalLayout(modificar, eliminar);
        buttons.setSpacing(true);
        add(buttons);
    }
}
