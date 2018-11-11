package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;

@SpringUI(path = "/")
@Theme("valo")
public class PantallaLogin extends UI {
    @Autowired
    private UsuarioService usuarioService;

    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private VerticalLayout verticalLayout = new VerticalLayout();
    private Button botonAccion = new Button();

    @Override
    protected void init(VaadinRequest request) {
        if (!usuarioService.listarUsuarios().isEmpty() && usuarioService.listarUsuarios().get(0).isEstaLogueado())
            getUI().getPage().setLocation("/calendario");
        else {
            configurarEstilo();
            agregarFormulario();
            agregarHeader();
        }
    }

    private void configurarEstilo() {
        Page.getCurrent().setTitle("Login");

        horizontalLayout = new HorizontalLayout();

        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);
        horizontalLayout.setSizeFull();
        horizontalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        setContent(verticalLayout);
    }

    private void agregarFormulario() {
        TextField email = new TextField("Email");
        PasswordField contrasena = new PasswordField("Contrasena");
        TextField nombres = new TextField("Nombre");

        botonAccion = usuarioService.listarUsuarios().isEmpty() ? new Button("Registrate !") : new Button("Entra !");

        botonAccion.addStyleName(ValoTheme.BUTTON_PRIMARY);
        botonAccion.setIcon(VaadinIcons.SIGN_IN);

        if (usuarioService.listarUsuarios().isEmpty()) {
            horizontalLayout.addComponents(nombres, email, contrasena);
        } else {
            horizontalLayout.addComponents(email, contrasena);
        }

        botonAccion.addClickListener((evento) -> {
            if (usuarioService.listarUsuarios().isEmpty()) {
                try {
                    usuarioService.crearUsuario(usuarioService.listarUsuarios().size() + 1, nombres.getValue(), email.getValue(), contrasena.getValue());
                    getUI().getPage().setLocation("/");
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
                        getUI().getPage().setLocation("/calendario");
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    getUI().getPage().setLocation("/");
            }
        });

        botonAccion.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    public void agregarHeader() {
        Label titulo = new Label("Práctica #14 - OCJ");
        titulo.addStyleName(ValoTheme.LABEL_H1);
        titulo.setSizeUndefined();

        Label subtitulo = usuarioService.listarUsuarios().isEmpty() ? new Label("¡Registre una cuenta para entrar!") : new Label("¡Por favor logueate!");
        subtitulo.addStyleName(ValoTheme.LABEL_H2);
        subtitulo.setSizeUndefined();

        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        verticalLayout.addComponents(titulo, subtitulo, horizontalLayout, botonAccion);
    }
}
