package de.hpm.registration.delegate;

import de.hpm.registration.model.NewUserRequest;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ProcessEnd implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        NewUserRequest newUserRequest = RequestUtils.fromDelagtionExecution(execution);
        log.info("Prozess {} erfolgreich beendet -> {}", execution, newUserRequest);
    }
}
