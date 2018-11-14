package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model;

import com.sendgrid.*;

import java.io.IOException;

public class Emails {

    public static void main(String[] args) throws IOException {

        String exampleEmail = "test@example.com";

        Email from = new Email("cesar18pena@gmail.com");
        String subject = "Practica 14 Vaadin - Oscar Jean Cesar";
        Email to = new Email("oscardns96@gmail.com");
        Content content = new Content("text/plain", "Este es un correo de ejemplo");
        Mail mail = new Mail(from, subject, to, content);

        String apiKey = "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";
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sg.api(request);
            System.out.println(response.statusCode);
            System.out.println(response.body);
            System.out.println(response.headers);

        } catch (IOException ex) {
            throw ex;
        }
    }
}