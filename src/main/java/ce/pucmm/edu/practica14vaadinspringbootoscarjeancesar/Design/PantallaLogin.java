package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.server.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;


import javax.persistence.PersistenceException;

@Route("")
public class PantallaLogin extends VerticalLayout {
    public PantallaLogin(@Autowired UsuarioService usuarioService) {

        // Agregar formulario
        TextField email = new TextField("Email");
        PasswordField contrasena = new PasswordField("Contrasena");
        TextField nombres = new TextField("Nombre");

        Button botonAccion = usuarioService.listarUsuarios().isEmpty() ? new Button("Registrate !") : new Button("Entra !");
        botonAccion.setIcon(new Icon(VaadinIcon.SIGN_IN));
        botonAccion.getElement().setAttribute("theme", "primary");
        HorizontalLayout horizontalLayout;

        if (usuarioService.listarUsuarios().isEmpty()) {
            horizontalLayout = new HorizontalLayout(nombres, email, contrasena);
        } else {
            horizontalLayout = new HorizontalLayout(email, contrasena);
        }

        botonAccion.addClickListener((evento) -> {
            if (usuarioService.listarUsuarios().isEmpty()) {
                try {
                    usuarioService.crearUsuario(usuarioService.listarUsuarios().size() + 1, nombres.getValue(), email.getValue(), contrasena.getValue());
                    getUI().get().getPage().reload();
                } catch (PersistenceException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (usuarioService.validarUsuario(email.getValue(), contrasena.getValue())) {
                    try {
                        Usuario usuario = usuarioService.listarUsuarios().get(0);
                        usuario.setEstaLogueado(true);
                        usuarioService.editarUsuario(usuario);
                        getUI().get().getPage().getHistory().pushState(null, "calendario");
                        getUI().get().getPage().reload();
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getUI().get().getPage().reload();
                }
            }
        });

        // Agregar header
        Text titulo = new Text("Práctica #14 - OCJ");
        Text subtitulo = usuarioService.listarUsuarios().isEmpty() ? new Text("¡Registre una cuenta para entrar!") : new Text("¡Por favor logueate!");

        add(titulo, subtitulo, horizontalLayout, botonAccion);
        setAlignItems(Alignment.CENTER);
    }
}
