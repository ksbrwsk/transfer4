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
public class CheckUserExists implements JavaDelegate {

    private static Random RAND = new Random();

    private  WebClient client = WebClient.create();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Aufruf Prüfung Benutzer existiert");
        NewUserRequest newUserRequest = RequestUtils.fromDelagtionExecution(delegateExecution);
        newUserRequest = client
                .post()
                .uri("http://localhost:9090/api/iam/check")
                .bodyValue(newUserRequest)
                .retrieve()
                .bodyToMono(NewUserRequest.class)
                .block();

        if(newUserRequest.isVorhanden()) {
            delegateExecution.getProcessInstance().setVariable("vorhanden", newUserRequest.isVorhanden());
        }
    }
}
