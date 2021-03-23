package de.hpm.registration.delegate;

import de.hpm.registration.model.NewUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Random;

@Log4j2
@Component
@RequiredArgsConstructor
public class RegisterUser implements JavaDelegate {

    private static Random RAND = new Random();

    private  WebClient client = WebClient.create();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Aufruf Register User");
        NewUserRequest newUserRequest = RequestUtils.fromDelagtionExecution(delegateExecution);
        NewUserRequest userRequest = client
                .post()
                .uri("http://localhost:9090/api/iam/register")
                .bodyValue(newUserRequest)
                .retrieve()
                .bodyToMono(NewUserRequest.class)
                .block();
        delegateExecution.getProcessInstance().setVariable("status", userRequest.getStatus());
        delegateExecution.getProcessInstance().setVariable("uid", userRequest.getUid());

        log.info("Benutzer erfolgreich registriert -> {}", newUserRequest);
    }

}
