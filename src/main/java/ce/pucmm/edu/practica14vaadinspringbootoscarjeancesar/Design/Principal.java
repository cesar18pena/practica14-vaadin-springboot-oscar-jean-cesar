package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.handler.BasicItemMoveHandler;
import org.vaadin.addon.calendar.handler.BasicItemResizeHandler;
import org.vaadin.addon.calendar.item.EditableCalendarItem;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.Locale;

@Route("calendario")
public class Principal extends VerticalLayout {
    public static Calendar calendario = new Calendar();
    public static DataProvider<Evento, Void> dataProvider;

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
        }
        else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado()) {
            getUI().get().getPage().getHistory().pushState(null, "");
            getUI().get().getPage().reload();
        }
        else {
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

            FullCalendar calendar = FullCalendarBuilder.create().build();
//
//            Entry entry = new Entry();
//            entry.setTitle("Some event");
//            entry.setStart(LocalDate.now().withDayOfMonth(3).atTime(10, 0));
//            entry.setEnd(entry.getStart().plusHours(2));
//            entry.setColor("#ff3333");
//
//            calendar.addEntry(entry);

            Text header = new Text("Práctica #14 - OCJ");

            add(header, layoutBotones, calendar);
            setFlexGrow(1, calendar);

//            pantallaEvento = new PantallaEvento();
//            pantallaEmail = new PantallaEmail(usuarioService.listarUsuarios().get(0).getEmail());
        }

        Button agregar = new Button("Agregar");
        agregar.setIcon(new Icon(VaadinIcon.PLUS));

//        calendario.setFirstDayOfWeek(1);
        calendario.setTimeFormat(Calendar.TimeFormat.Format12H);

//        calendario.setWeeklyCaptionFormat("yyyy-MM-dd");
        calendario.withVisibleDays(1, 7);
        calendario.setSizeFull();
        calendario.setHeight("90%");

        calendario.setHandler(new BasicItemMoveHandler() {
            private java.util.Calendar calendarioAux;

            public void eventMove(CalendarComponentEvents.ItemMoveEvent event) {
//                calendarioAux = event.getComponent().getInternalCalendar();
                super.itemMove(event);
            }

            protected void setDates(EditableCalendarItem event, ZonedDateTime start, ZonedDateTime end) {
                Evento e = (Evento) event;
                e.setStart(start);
                e.setEnd(end);
                eventoService.crearEvento(e.getCaption(), e.getDescription(), e.isAllDay(), e.getStart(), e.getEnd());
            }
        });

        calendario.setHandler(new BasicItemResizeHandler() {
            protected void setDates(EditableCalendarItem evento, ZonedDateTime start, ZonedDateTime end) {
                Evento e = (Evento) evento;
                e.setStart(start);
                e.setEnd(end);
                eventoService.crearEvento(e.getCaption(), e.getDescription(), e.isAllDay(), e.getStart(), e.getEnd());
            }
        });

        calendario.setHandler((CalendarComponentEvents.RangeSelectEvent evento) -> {
            pantallaEvento.setDates(evento.getStart(), evento.getEnd());
            abrirPantalla("Agregar nuevo evento", pantallaEvento);
        });

        calendario.setLocale(Locale.US);
        calendario.setStartDate(new GregorianCalendar().toZonedDateTime());
        GregorianCalendar finCalendario = new GregorianCalendar();
        finCalendario.set(java.util.Calendar.DATE, 1);
        finCalendario.roll(java.util.Calendar.DATE, -1);
        calendario.setEndDate(finCalendario.toZonedDateTime());
        calendario.setTimeFormat(Calendar.TimeFormat.Format12H);
        calendario.withVisibleDays(1, 7);
        calendario.withVisibleHours(6, 20);
        calendario.setSizeFull();

//        verticalLayout.addComponent(calendario);
//        verticalLayout.setExpandRatio(calendario, 1.0f);

        dataProvider = DataProvider.fromCallbacks(
                query -> eventoService.listarEventos().stream(),
                query -> Math.toIntExact(eventoService.listarEventos().size())
        );
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
