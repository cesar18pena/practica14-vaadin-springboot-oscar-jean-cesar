package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;


import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services.UsuarioService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "/gerentes")
@Theme("valo")
public class CRUDGerente extends UI {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    PantallaCrearUsuario pantallaCrearUsuario;

    Button enviar = new Button("Crear");

    private VerticalLayout verticalLayout = new VerticalLayout();

    @Autowired
    public CRUDGerente(PantallaCrearUsuario pantallaCrearUsuario) {
        this.pantallaCrearUsuario = pantallaCrearUsuario;
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

            pantallaCrearUsuario = new PantallaCrearUsuario();

            enviar.addStyleName(ValoTheme.BUTTON_PRIMARY);
            enviar.setIcon(FontAwesome.PLUS);
            configuraBotonPantalla(enviar, "Agregar nuevo gerente", pantallaCrearUsuario);

            verticalLayout.addComponent(enviar);

        }

    }

    private void configurarPagina() {
        Page.getCurrent().setTitle("Practica #14 - OCJ - CRUD");

        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(verticalLayout);
    }

    private void agregarHeader() {
        Label header = new Label("CRUD de Gerentes");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        verticalLayout.addComponent(header);
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