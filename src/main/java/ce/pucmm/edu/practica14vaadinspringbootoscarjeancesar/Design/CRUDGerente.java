package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route("gerentes")
@SpringComponent
@UIScope
public class CRUDGerente extends VerticalLayout {

    boolean editando = false;
    Integer usuarioSeleccionadoID;
    DataProvider<Usuario, Void> dataProvider;

    public CRUDGerente(@Autowired UsuarioService usuarioService) {
        TextField nombre = new TextField("Nombre:");
        TextField email = new TextField("Email:");
        PasswordField contrasena = new PasswordField("Contraseña:");

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return usuarioService.listarUsuariosPaginados(offset, limit).stream();
                },
                query -> Math.toIntExact(usuarioService.contarUsuario() - 1)
        );

        Binder<Usuario> binder = new Binder<>();
        Grid<Usuario> tabla = new Grid<>();

        Button agregar = new Button("Salvar");
        Button cancelar = new Button("Cancelar");

        PantallaAccionesGerente pantallaAccionesGerente = new PantallaAccionesGerente();

        if (usuarioService.listarUsuarios().isEmpty())
            getUI().get().navigate("");
        else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado())
            getUI().get().navigate("");
        else {
            agregar.setIcon(new Icon(VaadinIcon.DISC));
            agregar.getElement().setAttribute("theme", "primary");

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

                dataProvider.refreshAll();
            });

            cancelar.addClickListener((evento) -> {
                nombre.setValue("");
                email.setValue("");
                contrasena.setValue("");
            });

            H1 header = new H1("CRUD de Gerentes");

            HorizontalLayout botoneslayout = new HorizontalLayout(agregar, cancelar);
            botoneslayout.setSpacing(true);

            nombre.setTitle("Nombre: ");
            email.setTitle("Email: ");
            contrasena.setTitle("Contraseña: ");

            tabla.setDataProvider(dataProvider);
            tabla.addColumn(Usuario::getNombre).setHeader("Nombre");
            tabla.addColumn(Usuario::getEmail).setHeader("Email");

            tabla.addSelectionListener(event -> {
                if (event.getFirstSelectedItem().isPresent()) {
                    abrirPantalla(pantallaAccionesGerente);
                    pantallaAccionesGerente.eliminar.addClickListener((evento) -> {
                        Usuario usuario = event.getFirstSelectedItem().get();
                        usuarioService.eliminarUsuario((int) usuario.getId());
                        binder.readBean(usuario);

                        dataProvider.refreshAll();
                    });

                    pantallaAccionesGerente.modificar.addClickListener((evento) -> {
                        Usuario usuario = event.getFirstSelectedItem().get();

                        nombre.setValue(usuario.getNombre());
                        email.setValue(usuario.getEmail());
                        contrasena.setValue(usuario.getContrasena());
                        editando = true;
                        usuarioSeleccionadoID = (int) usuario.getId();
                        try {
                            binder.writeBean(usuario);
                        } catch(ValidationException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            setAlignItems(Alignment.CENTER);
            add(header, nombre, email, contrasena, botoneslayout, tabla);
            tabla.setWidth("75%");

            nombre.setValue("");
            email.setValue("");
            contrasena.setValue("");
        }
    }

    private void abrirPantalla(VerticalLayout form) {
        Dialog vistaPantalla = new Dialog();
        vistaPantalla.add(form);

        vistaPantalla.open();
    }
}
