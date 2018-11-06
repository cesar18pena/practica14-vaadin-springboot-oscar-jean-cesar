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

    private VerticalLayout layout;
    private HorizontalLayout horizontalLayout;
    private FormLayout editarInformacion;
    private VerticalLayout verticalLayout;

    private Usuario usuario;

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
        layout.setSpacing(true);
        layout.setMargin(true);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);
        layout.setSizeFull();
        layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        horizontalLayout.setSizeFull();
        horizontalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(layout);
    }

    private void agregarHeader() {
        HorizontalLayout hzl = new HorizontalLayout();

        hzl.setSpacing(true);
        hzl.setMargin(true);

        Label tituloArriba = new Label("Estás viendo el perfil de: " + usuario.getNombre());
        tituloArriba.addStyleName(ValoTheme.LABEL_H3);
        tituloArriba.setSizeUndefined();

        Button salir = new Button("Salir");
        Button calendario = new Button("Calendario");

        salir.addStyleName(ValoTheme.BUTTON_DANGER);
        salir.setIcon(FontAwesome.SIGN_OUT);

        calendario.addStyleName(ValoTheme.BUTTON_PRIMARY);
        calendario.setIcon(FontAwesome.CALENDAR);

        hzl.addComponents(tituloArriba, calendario, salir);

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

        calendario.addClickListener((evento) -> {
            getUI().getPage().setLocation("/calendario");

        });

        layout.addComponent(hzl);
    }

    private void mostrarInformacionUsuario() {
        verticalLayout = new VerticalLayout();

        Label email = new Label("Correo electrónico: " + usuario.getEmail());
        email.addStyleName(ValoTheme.LABEL_H4);

        Label nombre = new Label("Nombre: " + usuario.getNombre());
        nombre.addStyleName(ValoTheme.LABEL_H4);

        verticalLayout.addComponents(email, nombre);
        horizontalLayout.addComponent(verticalLayout);
    }

    private void agregarFormularioInformacion() {
        editarInformacion = new FormLayout();

        Label titulo = new Label("Cambia tu información");
        titulo.addStyleName(ValoTheme.LABEL_H3);

        TextField nuevoEmail = new TextField("Nuevo email:");
        TextField nuevoNombre = new TextField("Nuevo nombre:");
        Button guardar = new Button("Guardar cambios");

        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        guardar.setIcon(FontAwesome.SAVE);

        editarInformacion.addComponents(nuevoNombre, nuevoEmail, guardar);

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

