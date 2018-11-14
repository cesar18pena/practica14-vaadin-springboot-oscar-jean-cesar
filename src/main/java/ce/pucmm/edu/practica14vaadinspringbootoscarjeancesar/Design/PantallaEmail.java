package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import com.sendgrid.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.IOException;
import java.time.LocalDate;

@SpringComponent
@UIScope
public class PantallaEmail extends VerticalLayout {
    TextField para = new TextField("Para:");
    TextField asunto = new TextField("Asunto:");
    TextArea cuerpo = new TextArea("Cuerpo:");

    public PantallaEmail() {
        FormLayout formLayout = new FormLayout();

        H3 header = new H3("Enviar Correo");

        Button enviar = new Button("Enviar");
        enviar.setIcon(new Icon(VaadinIcon.ARROW_FORWARD));
        enviar.getElement().setAttribute("theme", "primary");

        Button cancelar = new Button("Cancelar");
        cancelar.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        cancelar.getElement().setAttribute("theme", "error");

        HorizontalLayout botones = new HorizontalLayout(enviar, cancelar);

        botones.setSpacing(true);

        formLayout.add(para, asunto, cuerpo);
        setAlignItems(Alignment.CENTER);

        add(header, formLayout, botones);

        enviar.addClickListener((evento) -> {
            Email desdeEmail = new Email("cesar18pena@gmail.com");
            String asuntoEmail = asunto.getValue();
            Email paraEmail = new Email(para.getValue());
            Content cuerpoEmail = new Content("text/plain", cuerpo.getValue());
            Mail email = new Mail(desdeEmail, asuntoEmail, paraEmail, cuerpoEmail);

            String apiKey = "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";
            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();

            try {
                request.method = Method.POST;
                request.endpoint = "mail/send";
                request.body = email.build();
                Response response = sg.api(request);

                System.out.println(response.statusCode);
                System.out.println(response.body);
                System.out.println(response.headers);

                para.setValue("");
                asunto.setValue("");
                cuerpo.setValue("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        cancelar.addClickListener((evento) -> {
            para.setValue("");
            asunto.setValue("");
            cuerpo.setValue("");
        });
    }
}
