package de.hpm.registration.listener;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

@Log4j2
public class ExecutionEventListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        log.info("Registrierung Benutzer begonnen");
        delegateExecution.setVariable("vorhanden", false);
        delegateExecution.setVariable("uid", "");
        delegateExecution.setVariable("privateKey", "");
        delegateExecution.setVariable("account", "");
        delegateExecution.setVariable("freigegeben", false);
        log.info("ExecutionEventListener -> {}", delegateExecution.getEventName());
    }
}
