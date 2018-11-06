package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Data.UsuarioRepository;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
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

    private VerticalLayout verticalLayout = new VerticalLayout();

    @Override
    protected void init(VaadinRequest request) {
        if (!usuarioService.listarUsuarios().isEmpty() && usuarioService.listarUsuarios().get(0).isEstaLogueado())
            getUI().getPage().setLocation("/calendario");
        else {
            configurarEstilo();
            agregarHeader();
            agregarFormulario();
        }
    }

    public void configurarEstilo() {
        Page.getCurrent().setTitle("Login");

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(verticalLayout);

    }

    private void agregarFormulario() {

        TextField email = new TextField("Email");

        PasswordField contrasena = new PasswordField("Contrasena");

        TextField nombres = new TextField("Nombre");

        Button botonAccion = usuarioService.listarUsuarios().isEmpty() ? new Button("Registrate !") : new Button("Entra !");

        botonAccion.addStyleName(ValoTheme.BUTTON_PRIMARY);
        botonAccion.setIcon(FontAwesome.SIGN_IN);

        if (usuarioService.listarUsuarios().isEmpty())
            verticalLayout.addComponents(nombres, email, contrasena, botonAccion);
        else
            verticalLayout.addComponents(email, contrasena, botonAccion);

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
        Label header = usuarioService.listarUsuarios().isEmpty() ? new Label("Registre una cuenta para entrar !") : new Label("Por favor logueate !");
        header.addStyleName(ValoTheme.LABEL_H3);
        header.setSizeUndefined();
        verticalLayout.addComponent(header);
    }
}
