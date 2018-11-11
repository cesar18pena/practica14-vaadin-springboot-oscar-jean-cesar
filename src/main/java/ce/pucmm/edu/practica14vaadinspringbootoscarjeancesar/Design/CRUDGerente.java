package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@Route("gerentes")
@Theme("valo")
public class CRUDGerente extends UI {
    @Autowired
    private UsuarioService usuarioService;

    TextField nombre = new TextField("Nombre:");
    TextField email = new TextField("Email:");
    PasswordField contrasena = new PasswordField("Contraseña:");

    DataProvider<Usuario, Void> dataProvider;
    Binder<Usuario> binder;
    Grid<Usuario> tabla;

    Button agregar = new Button("Salvar");
    Button cancelar = new Button("Cancelar");

    boolean editando = false;

    Integer usuarioSeleccionadoID;

    private VerticalLayout verticalLayout = new VerticalLayout();

    PantallaAccionesGerente pantallaAccionesGerente = new PantallaAccionesGerente();

    public CRUDGerente() {
        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return usuarioService.listarUsuariosPaginados(offset, limit).stream();
                },
                query -> Math.toIntExact(usuarioService.contarUsuario() - 1)
        );
    }

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
        agregar.setIcon(VaadinIcons.DISC);

        agregar.addClickListener((evento) -> {
            try {
                if (editando) {
                    usuarioService.crearUsuario(usuarioSeleccionadoID, nombre.getValue(), email.getValue(), contrasena.getValue());
                } else {
                    usuarioService.crearUsuario(usuarioService.contarUsuario(), nombre.getValue(), email.getValue(), contrasena.getValue());
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }

            nombre.setValue("");
            email.setValue("");
            contrasena.setValue("");
//            Page.getCurrent().reload();
            dataProvider.refreshAll();
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

        binder = new Binder<>();
        tabla = new Grid();
        tabla.setDataProvider(dataProvider);
        tabla.addColumn(Usuario::getNombre).setCaption("Nombre");
        tabla.addColumn(Usuario::getEmail).setCaption("Email");

        tabla.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                abrirPantalla("Elige una opción", pantallaAccionesGerente);
                pantallaAccionesGerente.eliminar.addClickListener((evento) -> {
                    Usuario usuario = event.getFirstSelectedItem().get();
                    usuarioService.eliminarUsuario((int)usuario.getId());
                    dataProvider.refreshAll();
                });

                pantallaAccionesGerente.modificar.addClickListener((evento) -> {
                    Usuario usuario = event.getFirstSelectedItem().get();
                    nombre.setValue(usuario.getNombre());
                    email.setValue(usuario.getEmail());
                    contrasena.setValue(usuario.getContrasena());
                    editando = true;
                    usuarioSeleccionadoID = (int)usuario.getId();
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
