package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "/gerentes")
@Theme("valo")
public class CRUDGerente extends UI {
    @Autowired
    private UsuarioService usuarioService;

    TextField nombre = new TextField("Nombre:");
    TextField email = new TextField("Email:");
    PasswordField contrasena = new PasswordField("Contraseña:");

    Button agregar = new Button("Salvar");
    Button cancelar = new Button("Cancelar");

    private VerticalLayout verticalLayout = new VerticalLayout();

    @Override
    protected void init(VaadinRequest request) {
        if (usuarioService.listarUsuarios().isEmpty())
            getUI().getPage().setLocation("/");
        else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado())
            getUI().getPage().setLocation("/");
        else {
            configurarPagina();

            nombre.setValue("");
            email.setValue("");
            contrasena.setValue("");
        }
    }

    private void configurarPagina() {
        Page.getCurrent().setTitle("Practica #14 - OCJ - CRUD");

        agregar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        agregar.setIcon(FontAwesome.SAVE);

        agregar.addClickListener((evento) -> {
            try {
                usuarioService.crearUsuario(usuarioService.contarUsuario(), nombre.getValue(), email.getValue(), contrasena.getValue());
            } catch (Exception exp) {
                exp.printStackTrace();
            }

            nombre.setValue("");
            email.setValue("");
            contrasena.setValue("");
        });

        cancelar.addClickListener((evento) -> {
            nombre.setValue("");
            email.setValue("");
            contrasena.setValue("");
        });

        Label header = new Label("CRUD de Gerentes");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();

        HorizontalLayout botoneslayout = new HorizontalLayout(agregar, cancelar);
        botoneslayout.setSpacing(true);

        nombre.setCaption("Nombre: ");
        email.setCaption("Email: ");
        contrasena.setCaption("Contraseña: ");

        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(verticalLayout);
        verticalLayout.addComponents(header, nombre, email, contrasena, botoneslayout);
    }
}