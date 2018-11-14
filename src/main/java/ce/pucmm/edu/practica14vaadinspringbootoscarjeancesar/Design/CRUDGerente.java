package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
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

import javax.persistence.PersistenceException;

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
        agregar.setIcon(new Icon(VaadinIcon.DATABASE));
        agregar.getElement().setAttribute("theme", "primary");

        Button cancelar = new Button("Cancelar");
        cancelar.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        cancelar.getElement().setAttribute("theme", "error");

        PantallaAccionesGerente pantallaAccionesGerente = new PantallaAccionesGerente();

        if (usuarioService.listarUsuarios().isEmpty())
            getUI().get().navigate("");
        else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado())
            getUI().get().navigate("");
        else {
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


            H4 titulo = new H4("Práctica #14 - OCJ");
            H6 subtitulo = new H6("CRUD de Gerentes");

            HorizontalLayout botones = new HorizontalLayout();

            Button calendario = new Button("Volver al Calendario");
            calendario.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_LEFT_O));

            Button salir = new Button("Salir");
            salir.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            salir.getElement().setAttribute("theme", "error");

            botones.add(calendario, salir);

            salir.addClickListener((evento) -> {
                try {
                    Usuario usuarioaux = usuarioService.listarUsuarios().get(0);
                    usuarioaux.setEstaLogueado(false);
                    usuarioService.editarUsuario(usuarioaux);
                } catch (PersistenceException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getUI().get().navigate("");
            });

            calendario.addClickListener((evento) -> getUI().get().navigate("calendario"));

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
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });

            setAlignItems(Alignment.CENTER);
            FormLayout form = new FormLayout(nombre, email, contrasena);

            add(titulo, subtitulo, botones, form, botoneslayout, tabla);

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
