package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Evento;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.EventoService;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@SpringUI(path = "/calendario")
@Theme("valo")
public class Principal extends UI {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    public EventoService eventoService;

    @Autowired
    private PantallaEvento pantallaEvento;
    private PantallaEmail pantallaEmail;

    private VerticalLayout verticalLayout = new VerticalLayout();
    public static Calendar calendario = new Calendar();

    @Autowired
    public Principal(PantallaEvento pantallaEvento) {
        this.pantallaEvento = pantallaEvento;
    }

    @Override
    protected void init(VaadinRequest request) {
        if (usuarioService.listarUsuarios().isEmpty())
            getUI().getPage().setLocation("/");
        else if (!usuarioService.listarUsuarios().get(0).isEstaLogueado())
            getUI().getPage().setLocation("/");
        else {
            configurarPagina();
            agregarHeader();
            formAgregar();
            agregarCalendario();

            pantallaEvento = new PantallaEvento();
            pantallaEmail = new PantallaEmail(usuarioService.listarUsuarios().get(0).getEmail());
        }
    }

    private void configurarPagina() {
        Page.getCurrent().setTitle("Practica #14 - OCJ");

        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(verticalLayout);
    }

    private void formAgregar() {
        HorizontalLayout layoutBotones = new HorizontalLayout();
        layoutBotones.setSpacing(true);

        Button agregar = new Button("Agregar evento");
        Button enviarEmail = new Button("Enviar email");
        Button verUsuario = new Button("Información de usuario");
        Button CRUD = new Button("CRUD de Gerentes");
        Button salir = new Button("Salir");

        agregar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        agregar.setIcon(FontAwesome.PLUS);

        enviarEmail.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        enviarEmail.setIcon(FontAwesome.SEND);

        verUsuario.addStyleName(ValoTheme.BUTTON_PRIMARY);
        verUsuario.setIcon(FontAwesome.USER);

        CRUD.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        CRUD.setIcon(FontAwesome.USERS);

        salir.addStyleName(ValoTheme.BUTTON_DANGER);
        salir.setIcon(FontAwesome.SIGN_OUT);

        if (!eventoService.listarEventos().isEmpty()) {
            configuraBotonPantalla(agregar, "Agregar nuevo evento (" + eventoService.listarEventos().size() + ")", pantallaEvento);
        } else {
            configuraBotonPantalla(agregar, "Agregar nuevo evento", pantallaEvento);
        }

        configuraBotonPantalla(enviarEmail, "Enviar email", pantallaEmail);

        layoutBotones.addComponents(agregar, enviarEmail, verUsuario, CRUD ,salir);

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

        verUsuario.addClickListener((evento) -> {
            getUI().getPage().setLocation("/usuario");
        });

        CRUD.addClickListener((evento) -> {
            getUI().getPage().setLocation("/gerentes");
        });

        verticalLayout.addComponent(layoutBotones);

        agregar.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private void agregarHeader() {
        Label header = new Label("Práctica #14 - OCJ");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        verticalLayout.addComponent(header);
    }

    private void agregarCalendario() {
        Button agregar = new Button("Agregar");
        agregar.setIcon(FontAwesome.PLUS);
        agregar.setStyleName(ValoTheme.BUTTON_PRIMARY);

        calendario.setFirstDayOfWeek(1);
        calendario.setTimeFormat(Calendar.TimeFormat.Format12H);

        calendario.setWeeklyCaptionFormat("yyyy-MM-dd");
        calendario.setFirstVisibleDayOfWeek(1);
        calendario.setLastVisibleDayOfWeek(7);
        calendario.setSizeFull();
        calendario.setHeight("90%");

        calendario.setHandler(new BasicEventMoveHandler() {
            private java.util.Calendar calendarioAux;

            public void eventMove(CalendarComponentEvents.MoveEvent event) {
                calendarioAux = event.getComponent().getInternalCalendar();
                super.eventMove(event);
            }

            protected void setDates(EditableCalendarEvent event, Date start, Date end) {
                Evento e = (Evento) event;
                e.setStart(start);
                e.setEnd(end);
                eventoService.crearEvento(e.getCaption(), e.getDescription(), e.isAllDay(), e.getStart(), e.getEnd());
            }
        });

        calendario.setHandler(new BasicEventResizeHandler() {
            protected void setDates(EditableCalendarEvent evento, Date start, Date end) {
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
        calendario.setStartDate(new GregorianCalendar().getTime());
        GregorianCalendar finCalendario = new GregorianCalendar();
        finCalendario.set(java.util.Calendar.DATE, 1);
        finCalendario.roll(java.util.Calendar.DATE, -1);
        calendario.setEndDate(finCalendario.getTime());
        calendario.setTimeFormat(Calendar.TimeFormat.Format12H);
        calendario.setFirstVisibleDayOfWeek(1);
        calendario.setLastVisibleDayOfWeek(7);
        calendario.setFirstVisibleHourOfDay(6);
        calendario.setLastVisibleHourOfDay(20);
        calendario.setSizeFull();

        verticalLayout.addComponent(calendario);
        verticalLayout.setExpandRatio(calendario, 1.0f);

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
