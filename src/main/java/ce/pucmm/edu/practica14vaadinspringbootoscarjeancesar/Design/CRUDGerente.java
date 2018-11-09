package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.annotations.Theme;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.Binder;
import java.util.Date;
import java.util.List;

@SpringUI(path = "/gerentes")
@Theme("valo")
public class CRUDGerente extends UI {
    @Autowired
    private UsuarioService usuarioService;

    TextField nombre = new TextField("Nombre:");
    TextField email = new TextField("Email:");
    PasswordField contrasena = new PasswordField("Contraseña:");

    Grid tabla;

    Button agregar = new Button("Salvar");
    Button cancelar = new Button("Cancelar");

    boolean editando = false;

    Integer usuarioSeleccionadoID;

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout paginaEntera = new HorizontalLayout();

    PantallaAccionesGerente pantallaAccionesGerente = new PantallaAccionesGerente();

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
                if(editando){
                    usuarioService.crearUsuario(usuarioSeleccionadoID, nombre.getValue(), email.getValue(), contrasena.getValue());
                }
                else {
                    usuarioService.crearUsuario(usuarioService.contarUsuario(), nombre.getValue(), email.getValue(), contrasena.getValue());
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }

            nombre.setValue("");
            email.setValue("");
            contrasena.setValue("");
            Page.getCurrent().reload();
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

        List<Usuario> listaDeUsuarios = usuarioService.listarUsuarios();

        tabla = new Grid();
        tabla.addColumn("Nombre").setHeaderCaption("Nombre");
        tabla.addColumn("Email").setHeaderCaption("Email");

        for (Usuario usuario : listaDeUsuarios) {
            tabla.addRow(usuario.getNombre(), usuario.getEmail());
        }
        ;

        tabla.addSelectionListener((SelectionEvent.SelectionListener) event -> {
            if (!event.getSelected().isEmpty()) {
                abrirPantalla("Elige una opción", pantallaAccionesGerente);
                pantallaAccionesGerente.eliminar.addClickListener((evento) -> {
                    Integer usuarioID = (Integer) event.getSelected().toArray()[0];
                    usuarioService.eliminarUsuario(usuarioID);
                    Page.getCurrent().reload();
                });
                pantallaAccionesGerente.modificar.addClickListener((evento) -> {
                    Integer usuarioID = (Integer) event.getSelected().toArray()[0];
                    Usuario usuario = usuarioService.buscarUsuario(usuarioID);
                    nombre.setValue(usuario.getNombre());
                    email.setValue(usuario.getEmail());
                    contrasena.setValue(usuario.getContrasena());
                    editando = true;
                    usuarioSeleccionadoID = usuarioID;
                });
            }
        });


        tabla.setHeight("70%");

        verticalLayout = new VerticalLayout();
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        verticalLayout.setSpacing(true);
        setContent(verticalLayout);

        verticalLayout.addComponents(header, nombre, email, contrasena, botoneslayout, tabla);
    }

    private void abrirPantalla(String title, FormLayout form) {
        Window vistaPantalla = new Window(title);

        vistaPantalla.center();
        vistaPantalla.setResizable(false);
        vistaPantalla.setModal(true);
        vistaPantalla.setClosable(true);
        vistaPantalla.setDraggable(false);
        vistaPantalla.setContent(form);

        addWindow(vistaPantalla);
    }

    private void configuraBotonPantalla(Button boton, String titulo, FormLayout formulario) {
        boton.addClickListener((e) -> {
            abrirPantalla(titulo, formulario);
        });
    }
}