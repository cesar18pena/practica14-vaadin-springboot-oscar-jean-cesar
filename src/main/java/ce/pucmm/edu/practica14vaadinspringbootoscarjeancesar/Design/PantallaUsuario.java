package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.server.Page;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;

@Route("usuario")
@SpringComponent
@UIScope
public class PantallaUsuario extends VerticalLayout {

    public PantallaUsuario(@Autowired UsuarioService usuarioService) {
        Usuario usuario;

        HorizontalLayout horizontalLayout;
        HorizontalLayout botones;
        VerticalLayout editarInformacion;

        if (usuarioService.listarUsuarios().isEmpty())
            getUI().get().navigate("");
        else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado())
            getUI().get().navigate("");
        else {
            usuario = usuarioService.listarUsuarios().get(0);

            /* ********* CONFIGURAR ESTILO ********** */
            horizontalLayout = new HorizontalLayout();
            botones = new HorizontalLayout();

            setAlignItems(Alignment.CENTER);

            horizontalLayout.setMargin(true);
            horizontalLayout.setSpacing(true);
            horizontalLayout.setSizeFull();
            horizontalLayout.setAlignItems(Alignment.CENTER);

            /* ********* AGREGAR HEADER ********** */
            H1 titulo = new H1("Ver usuario");

            Button calendario = new Button("Calendario");
            calendario.setIcon(new Icon(VaadinIcon.CALENDAR));

            Button salir = new Button("Salir");
            salir.setIcon(new Icon(VaadinIcon.SIGN_OUT));

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

            /* ********* MOSTRAR INFORMACION DE USUARIO ********** */
            FormLayout fml = new FormLayout();

            H3 titulo1 = new H3("Datos del usuario");
            H6 nombre = new H6("Nombre: " + usuario.getNombre());
            H6 email = new H6("Correo electrónico: " + usuario.getEmail());

            fml.add(titulo1, nombre, email);

            editarInformacion = new VerticalLayout();

            H3 titulo2 = new H3("Editar datos del usuario");

            TextField nuevoEmail = new TextField("Email");
            TextField nuevoNombre = new TextField("Nombre");

            Button guardar = new Button("Guardar cambios");
            guardar.setIcon(new Icon(VaadinIcon.DISC));

            editarInformacion.add(titulo2, nuevoNombre, nuevoEmail, guardar);

            /* ********* AGREGAR FILTRO DE INFORMACION ********** */
            guardar.addClickListener((evento) -> {
                try {
                    if (!nuevoEmail.getValue().equals(""))
                        usuario.setEmail(nuevoEmail.getValue());

                    if (!nuevoNombre.getValue().equals(""))
                        usuario.setNombre(nuevoNombre.getValue());

                    usuarioService.editarUsuario(usuario);
                    getUI().get().getPage().reload();
                } catch (PersistenceException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            horizontalLayout.add(fml, editarInformacion);
            horizontalLayout.setAlignItems(Alignment.CENTER);

            botones.add(calendario, salir);

            add(titulo, botones, horizontalLayout);
        }
    }
}

