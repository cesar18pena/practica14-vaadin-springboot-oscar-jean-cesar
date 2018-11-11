package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import org.vaadin.calendar.CalendarComponent;
import javax.persistence.PersistenceException;
import java.time.ZonedDateTime;
import java.util.*;

@Route("calendario")
public class Principal extends VerticalLayout {

    @Autowired
    public Principal(@Autowired final PantallaEvento pantallaEvento,
                     @Autowired UsuarioService usuarioService,
                     @Autowired EventoService eventoService,
                     @Autowired final PantallaEmail pantallaEmail
    ) {

        VerticalLayout verticalLayout = new VerticalLayout();

        if (usuarioService.listarUsuarios().isEmpty()) {
            getUI().get().getPage().getHistory().pushState(null, "");
            getUI().get().getPage().reload();
        } else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado()) {
            getUI().get().getPage().getHistory().pushState(null, "");
            getUI().get().getPage().reload();
        } else {
            setAlignItems(Alignment.CENTER);

            HorizontalLayout layoutBotones = new HorizontalLayout();
            layoutBotones.setSpacing(true);

            Button agregar = new Button("Agregar evento");
            Button enviarEmail = new Button("Enviar email");
            Button verUsuario = new Button("Información de usuario");
            Button CRUD = new Button("CRUD de Gerentes");
            Button salir = new Button("Salir");

            agregar.setIcon(new Icon(VaadinIcon.PLUS));
            enviarEmail.setIcon(new Icon(VaadinIcon.ENVELOPE));
            verUsuario.setIcon(new Icon(VaadinIcon.USER));
            CRUD.setIcon(new Icon(VaadinIcon.USERS));
            salir.setIcon(new Icon(VaadinIcon.SIGN_OUT));

            if (!eventoService.listarEventos().isEmpty()) {
                configuraBotonPantalla(agregar, "Agregar nuevo evento (" + eventoService.listarEventos().size() + ")", pantallaEvento);
            } else {
                configuraBotonPantalla(agregar, "Agregar nuevo evento", pantallaEvento);
            }

            configuraBotonPantalla(enviarEmail, "Enviar email", pantallaEmail);

            layoutBotones = new HorizontalLayout(agregar, enviarEmail, verUsuario, CRUD, salir);

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
                getUI().get().getPage().reload();
            });

            verUsuario.addClickListener((evento) -> {
                getUI().get().getPage().getHistory().pushState(null, "usuario");
                getUI().get().getPage().reload();
            });

            CRUD.addClickListener((evento) -> {
                getUI().get().getPage().getHistory().pushState(null, "gerentes");
                getUI().get().getPage().reload();
            });

            eventoService.crearEvento("algo", "alguito", false, ZonedDateTime.now(), ZonedDateTime.now());
            eventoService.crearEvento("algo1", "alguito1", false, ZonedDateTime.now(), ZonedDateTime.now());
            eventoService.crearEvento("algo2", "alguito2", false, ZonedDateTime.now(), ZonedDateTime.now());
            eventoService.crearEvento("algo3", "alguito3", false, ZonedDateTime.now(), ZonedDateTime.now());

            CalendarComponent<Evento> calendario = new CalendarComponent<Evento>()
                    .withItemDateGenerator(e -> new Date())
                    .withItemLabelGenerator(e -> e.getCaption());
            calendario.setItems(new Evento("Evento1", "Este es el evento 1", false, ZonedDateTime.now(), ZonedDateTime.now()));
            calendario.addEventClickListener(evt -> Notification.show(evt.getDetail().getCaption()));

            H1 header = new H1("Práctica #14 - OCJ");
            H2 calSubtitulo = new H2("Calendario");

            add(header, calSubtitulo, layoutBotones, calendario);
            setAlignItems(Alignment.CENTER);

//            pantallaEvento = new PantallaEvento();
//            pantallaEmail = new PantallaEmail(usuarioService.listarUsuarios().get(0).getEmail());
        }

        Button agregar = new Button("Agregar");
        agregar.setIcon(new Icon(VaadinIcon.PLUS));

    }

    private void abrirPantalla(String title, FormLayout form) {
        Dialog vistaPantalla = new Dialog();
        vistaPantalla.add(new Label(title));

        vistaPantalla.open();
    }

    private void configuraBotonPantalla(Button boton, String titulo, FormLayout formulario) {
        boton.addClickListener((e) -> {
            abrirPantalla(titulo, formulario);
        });
    }
}
