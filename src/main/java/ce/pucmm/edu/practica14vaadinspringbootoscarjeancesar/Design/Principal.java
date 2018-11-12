package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import org.vaadin.calendar.CalendarComponent;
import org.vaadin.calendar.data.AbstractCalendarDataProvider;

import javax.persistence.PersistenceException;
import java.time.ZonedDateTime;
import java.util.*;

@Route("calendario")
@SpringComponent
@UIScope
public class Principal extends VerticalLayout {
    public static CalendarComponent<Evento> calendario = new CalendarComponent<Evento>()
            .withItemDateGenerator(e -> new Date())
            .withItemLabelGenerator(e -> e.getCaption());

    @Autowired
    public static EventoService eventoService;

    @Autowired
    public Principal(@Autowired final PantallaEvento pantallaEvento,
                     @Autowired UsuarioService usuarioService,
                     @Autowired EventoService eventoService,
                     @Autowired final PantallaEmail pantallaEmail
    ) {
        Principal.eventoService = eventoService;

        if (usuarioService.listarUsuarios().isEmpty()) {
            getUI().get().navigate("");
        } else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado()) {
            getUI().get().navigate("");
        } else {
            setAlignItems(Alignment.CENTER);

            HorizontalLayout layoutBotones = new HorizontalLayout();
            layoutBotones.setSpacing(true);

            Button agregar = new Button("Agregar evento");
            Button enviarEmail = new Button("Enviar email");
            Button verUsuario = new Button("Información de usuario");
            Button CRUD = new Button("CRUD de Gerentes");
            Button salir = new Button("Salir");

            agregar.setIcon(new Icon(VaadinIcon.CALENDAR_CLOCK));
            agregar.getElement().setAttribute("theme", "primary");

            enviarEmail.setIcon(new Icon(VaadinIcon.CALENDAR_ENVELOPE));
            enviarEmail.getElement().setAttribute("theme", "primary");

            verUsuario.setIcon(new Icon(VaadinIcon.CLIPBOARD_USER));

            CRUD.setIcon(new Icon(VaadinIcon.FORM));
            CRUD.getElement().setAttribute("theme", "success");

            salir.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            salir.getElement().setAttribute("theme", "error");

            configuraBotonPantalla(agregar, pantallaEvento);
            configuraBotonPantalla(enviarEmail, pantallaEmail);

            layoutBotones = new HorizontalLayout(agregar, enviarEmail, verUsuario, CRUD, salir);

            salir.addClickListener((evento) -> {
                try {
                    Usuario usuario = usuarioService.listarUsuarios().get(0);
                    usuario.setEstaLogueado(false);
                    usuarioService.editarUsuario(usuario);

                    getUI().get().navigate("");
                } catch (PersistenceException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            verUsuario.addClickListener((evento) -> getUI().get().navigate("usuario"));
            CRUD.addClickListener((evento) -> getUI().get().navigate("gerentes"));

            eventoService.crearEvento("algo", "alguito", false, ZonedDateTime.now(), ZonedDateTime.now());
            eventoService.crearEvento("algo1", "alguito1", false, ZonedDateTime.now(), ZonedDateTime.now());
            eventoService.crearEvento("algo2", "alguito2", false, ZonedDateTime.now(), ZonedDateTime.now());
            eventoService.crearEvento("algo3", "alguito3", false, ZonedDateTime.now(), ZonedDateTime.now());

//            calendario.setItems(new Evento("Evento1", "Este es el evento 1", false, ZonedDateTime.now(), ZonedDateTime.now()));

            calendario.setDataProvider(new CustomDataProvider());
            calendario.addEventClickListener(evt -> Notification.show(evt.getDetail().getCaption()));

            H4 titulo = new H4("Práctica #14 - OCJ");
            H6 subtitulo = new H6("Calendario");

            setAlignItems(Alignment.CENTER);

            add(titulo, subtitulo, layoutBotones, calendario);

//            pantallaEmail = new PantallaEmail(usuarioService.listarUsuarios().get(0).getEmail());
        }

        Button agregar = new Button("Agregar");
        agregar.setIcon(new Icon(VaadinIcon.PLUS));
        agregar.getElement().setAttribute("theme", "primary");
    }

    private void abrirPantalla(VerticalLayout form) {
        Dialog vistaPantalla = new Dialog();
        vistaPantalla.add(form);

        vistaPantalla.open();
    }

    private void configuraBotonPantalla(Button boton, VerticalLayout formulario) {
        boton.addClickListener((e) -> {
            abrirPantalla(formulario);
        });
    }
}

@SpringComponent
@UIScope
class CustomDataProvider extends AbstractCalendarDataProvider<Evento> {
    @Override
    public Collection<Evento> getItems(Date fromDate, Date toDate) {
        List<Evento> eventos = Principal.eventoService.listarEventos();
        return eventos;
    }
}