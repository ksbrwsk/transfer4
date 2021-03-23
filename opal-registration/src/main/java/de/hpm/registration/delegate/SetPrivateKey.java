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
public class SetPrivateKey implements JavaDelegate {

    private WebClient client = WebClient.create();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Generiere Private Key");
        delegateExecution.setVariable("privateKey", this.generatePrivateKey());
        NewUserRequest newUserRequest = RequestUtils.fromDelagtionExecution(delegateExecution);
        NewUserRequest userRequest = client
                .post()
                .uri("http://localhost:9090/api/iam/privateKey")
                .bodyValue(newUserRequest)
                .retrieve()
                .bodyToMono(NewUserRequest.class)
                .block();
        delegateExecution.getProcessInstance().setVariable("privateKey", newUserRequest.getPrivateKey());
        log.info("Private Key erfolgreich gesetzt -> {}", newUserRequest);
    }

    private String generatePrivateKey() {
        return UUID.randomUUID().toString();
    }
}
