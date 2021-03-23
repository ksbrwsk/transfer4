package de.hpm.registration.listener;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

import java.util.Map;

@Log4j2
public class TaskLogger implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> variables = delegateExecution.getProcessInstance().getVariables();
        log.info("Task beendet -> {} {}", delegateExecution.getProcessInstance().getId(), delegateExecution.getEventName());
        log.info("Variablen -> {}", variables.toString());
    }
}
