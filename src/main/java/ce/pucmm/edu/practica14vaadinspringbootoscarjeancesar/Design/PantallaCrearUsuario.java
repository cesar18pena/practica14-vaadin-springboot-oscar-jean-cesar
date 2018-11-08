package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;


import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.sendgrid.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@UIScope
@SpringUI
public class PantallaCrearUsuario extends FormLayout {

    @Autowired
    private UsuarioService usuarioService;

    TextField nombre = new TextField("Nombre:");
    TextField email = new TextField("Email:");
    PasswordField contrasena = new PasswordField("Contraseña:");

    Button enviar = new Button("Enviar");
    Button cancelar = new Button("Cancelar");

    public PantallaCrearUsuario(String nombreString, String emailString, String contrasenaString) {
        nombre.setValue(nombreString);
        email.setValue(emailString);
        contrasena.setValue(contrasenaString);
        setup();
    }

    public PantallaCrearUsuario() {
        nombre.setValue("");
        email.setValue("");
        contrasena.setValue("");
        setup();
    }

    private void setup() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        enviar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        enviar.setIcon(FontAwesome.PLUS);

        enviar.addClickListener((evento) -> {
            try {

                System.out.println(usuarioService.contarUsuario());

                usuarioService.crearUsuario(usuarioService.contarUsuario(), nombre.getValue(), email.getValue(), contrasena.getValue());
            } catch (Exception exp) {
                exp.printStackTrace();
            }

            ((Window) getParent()).close();
        });

        cancelar.addClickListener((evento) -> {
            ((Window) getParent()).close();
        });

        HorizontalLayout botoneslayout = new HorizontalLayout(enviar, cancelar);
        botoneslayout.setSpacing(true);

        nombre.setCaption("Nombre: ");
        email.setCaption("Email: ");
        contrasena.setCaption("Contraseña: ");

        addComponents(nombre, email, contrasena, botoneslayout);
    }
}