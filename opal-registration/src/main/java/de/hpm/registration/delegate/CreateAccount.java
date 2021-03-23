package de.hpm.registration.delegate;

import de.hpm.registration.model.NewUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class CreateAccount implements JavaDelegate {

    private WebClient client = WebClient.create();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Erzeuge Account");
        delegateExecution.setVariable("account", this.createAccount());
        NewUserRequest newUserRequest = RequestUtils.fromDelagtionExecution(delegateExecution);
        NewUserRequest userRequest = client
                .post()
                .uri("http://localhost:9090/api/iam/account")
                .bodyValue(newUserRequest)
                .retrieve()
                .bodyToMono(NewUserRequest.class)
                .block();
        log.info("Account erfolgreich gesetzt -> {}", newUserRequest);
    }

    private String createAccount() {
        return "AGUTSCHLUESEL="+ UUID.randomUUID().toString();
    }
}
