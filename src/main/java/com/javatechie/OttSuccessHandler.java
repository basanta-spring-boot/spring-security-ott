package com.javatechie;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;

    private final OneTimeTokenGenerationSuccessHandler redirectHandler =
            new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");

    public OttSuccessHandler(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            OneTimeToken oneTimeToken) throws IOException, ServletException {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                        .replacePath(request.getContextPath())
                        .replaceQuery(null)
                        .fragment(null)
                        .path("/login/ott")
                        .queryParam("token", oneTimeToken.getTokenValue());

        String magiclink = builder.toUriString();

        System.out.println("Magic link: " + magiclink);

        // Send magic link via mail
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Javatechie <" + sender + ">");
            message.setTo(getEmail().get(oneTimeToken.getUsername()));
            message.setSubject("One Time Token");

            String messageBody = """
                    Hello %s,
                           \s
                    Use the following link to sign in to the application:
                           \s
                    %s
                           \s
                    This link is valid for a limited time. If you did not request this, please ignore this email.
                           \s
                   \s""".formatted(oneTimeToken.getUsername(), magiclink);

            message.setText(messageBody);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.redirectHandler.handle(request, response, oneTimeToken);
    }

    private Map<String, String> getEmail() {
        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("javatechie", "javatechie4u@gmail.com");
        return emailMap;
    }
}
