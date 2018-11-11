package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Design;

import com.sendgrid.*;
import com.vaadin.event.ShortcutAction;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component

public class PantallaEmail extends VerticalLayout {

//    TextField desde = new TextField("Desde:");
//    TextField hacia = new TextField("Hacia:");
//    TextField titulo = new TextField("Titulo");
//    TextArea descripcion = new TextArea("Descripcion");
//
//    Button enviar = new Button("Enviar");
//    Button cancelar = new Button("Cancelar");
//
//    public PantallaEmail(String email) {
//        desde.setValue(email);
//        setup();
//    }
//
//    public PantallaEmail() {
//        desde.setValue("");
//        setup();
//    }
//
//    private void setup() {
//        setSizeUndefined();
//        setMargin(true);
//        setSpacing(true);
//
//        enviar.setClickShortcut(ShortcutAction.KeyCode.ENTER);
//        enviar.addClickListener((evento) -> {
//                Email from = new Email(desde.getValue());
//                String subject = titulo.getValue();
//                Email to = new Email(hacia.getValue());
//                Content content = new Content("text/plain", descripcion.getValue());
//                Mail mail = new Mail(from, subject, to, content);
//
//                SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
//                Request request = new Request();
//                try {
//                    request.method = Method.POST;
//                    request.endpoint = "mail/send";
//                    request.body = mail.build();
//                    Response response = sg.api(request);
//                    System.out.println(response.statusCode);
//                    System.out.println(response.body);
//                    System.out.println(response.headers);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                ((Window)getParent()).close();
//        });
//
//        cancelar.addClickListener((evento) -> {
//            ((Window)getParent()).close();
//        });
//
//        HorizontalLayout botoneslayout = new HorizontalLayout(enviar, cancelar);
//        botoneslayout.setSpacing(true);
//
//        desde.setCaption("Desde:");
//        desde.setValue("practica14ocj@ocj.com");
//        hacia.setCaption("Hacia:");
//
//        titulo.setCaption("Titulo:");
//        descripcion.setCaption("Descripcion:");
//
//        addComponents(desde, hacia, titulo, descripcion, botoneslayout);
//    }
}
