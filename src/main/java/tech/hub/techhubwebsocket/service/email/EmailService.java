package tech.hub.techhubwebsocket.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final TaskExecutor taskExecutor;

    @Value("${spring.mail.username}")
    private String remetente;

    public void sendMail(Email emailDto) {
        taskExecutor.execute(() -> {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(remetente);

            simpleMailMessage.setTo(emailDto.destinatario());
            simpleMailMessage.setSubject(emailDto.assunto());
            simpleMailMessage.setText(emailDto.conteudo());

            javaMailSender.send(simpleMailMessage);
        });
    }

    public void sendEmailWithHtmlTemplate(String destinatario, String assunto, String templateName, Context context) {
        taskExecutor.execute(() -> {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                mimeMessageHelper.setFrom(remetente);
                mimeMessageHelper.setTo(destinatario);
                mimeMessageHelper.setSubject(assunto);

                String htmlContent = templateEngine.process(templateName, context);
                mimeMessageHelper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);

            } catch (MessagingException ex) {
                System.out.println("MENSAGEM DE EMAIL EXCEPTION");
                ex.printStackTrace();
            } catch (MailAuthenticationException ex) {
                System.out.println("MAIL AUTHENTICATION EXCEPTION");
                ex.printStackTrace();
            } catch (MailSendException ex) {
                System.out.println("MAIL SEND EXCEPTION");
                ex.printStackTrace();
            } catch (MailPreparationException ex) {
                System.out.println("MAIL PREPARATION EXCEPTION");
                ex.printStackTrace();
            } catch (Exception ex) {
                System.out.println("Erro ao enviar email");
                ex.printStackTrace();
            }
        });
    }
}
