package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

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

@SpringUI(path = "/usuario")
@Theme("valo")
public class PantallaUsuario extends UI {
    @Autowired
    private UsuarioService usuarioService;

    private Usuario usuario;

    private VerticalLayout layout;
    private HorizontalLayout horizontalLayout;
    private FormLayout editarInformacion;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        if (usuarioService.listarUsuarios().isEmpty())
            getUI().getPage().setLocation("/");
        else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado())
            getUI().getPage().setLocation("/");
        else {
            usuario = usuarioService.listarUsuarios().get(0);

            configurarEstilo();
            agregarHeader();
            mostrarInformacionUsuario();
            agregarFormularioInformacion();
            cambiarEstilo();
        }
    }

    private void configurarEstilo() {
        Page.getCurrent().setTitle("Informacion de usuario");

        layout = new VerticalLayout();
        horizontalLayout = new HorizontalLayout();

        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        horizontalLayout.setMargin(true);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setSizeFull();
        horizontalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        setContent(layout);
    }

    private void agregarHeader() {
        HorizontalLayout hzl = new HorizontalLayout();
        VerticalLayout vtl = new VerticalLayout();

        vtl.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        hzl.setSpacing(true);
        hzl.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Label titulo = new Label("Práctica #14 - OCJ");
        titulo.addStyleName(ValoTheme.LABEL_H1);
        titulo.setSizeUndefined();

        Button calendario = new Button("Calendario");
        Button salir = new Button("Salir");

        calendario.addStyleName(ValoTheme.BUTTON_PRIMARY);
        calendario.setIcon(FontAwesome.CALENDAR);

        salir.addStyleName(ValoTheme.BUTTON_DANGER);
        salir.setIcon(FontAwesome.SIGN_OUT);

        hzl.addComponents(calendario, salir);
        vtl.addComponents(titulo, hzl);

        salir.addClickListener((evento) -> {
            try {
                Usuario usuario = usuarioService.listarUsuarios().get(0);
                usuario.setEstaLogueado(false);
                usuarioService.editarUsuario(usuario);
            } catch (PersistenceException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getUI().getPage().setLocation("/");
        });

        calendario.addClickListener((evento) -> getUI().getPage().setLocation("/calendario"));

        layout.addComponent(vtl);
    }

    private void mostrarInformacionUsuario() {
        FormLayout fml = new FormLayout();

        Label titulo = new Label("Datos del usuario");
        titulo.addStyleName(ValoTheme.LABEL_H3);
        titulo.setSizeUndefined();

        Label nombre = new Label("Nombre: " + usuario.getNombre());
        nombre.addStyleName(ValoTheme.LABEL_SUCCESS);

        Label email = new Label("Correo electrónico: " + usuario.getEmail());
        email.addStyleName(ValoTheme.LABEL_SUCCESS);

        fml.addComponents(titulo, nombre, email);

        horizontalLayout.addComponents(fml);
    }

    private void agregarFormularioInformacion() {
        editarInformacion = new FormLayout();

        Label titulo = new Label("Editar datos del usuario");
        titulo.addStyleName(ValoTheme.LABEL_H3);

        TextField nuevoEmail = new TextField("Email");
        TextField nuevoNombre = new TextField("Nombre");

        Button guardar = new Button("Guardar cambios");
        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        guardar.setIcon(FontAwesome.SAVE);

        editarInformacion.addComponents(titulo, nuevoNombre, nuevoEmail, guardar);

        guardar.addClickListener((evento) -> {
            try {
                if (!nuevoEmail.getValue().equals(""))
                    usuario.setEmail(nuevoEmail.getValue());

                if (!nuevoNombre.getValue().equals(""))
                    usuario.setNombre(nuevoNombre.getValue());

                usuarioService.editarUsuario(usuario);
            } catch (PersistenceException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            getUI().getPage().setLocation("/usuario");
        });

        guardar.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        horizontalLayout.addComponent(editarInformacion);
    }

    private void cambiarEstilo() {
        layout.addComponent(horizontalLayout);
    }
}

