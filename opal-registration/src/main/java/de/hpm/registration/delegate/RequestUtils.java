package de.hpm.registration.delegate;

import de.hpm.registration.model.NewUserRequest;
import org.camunda.bpm.engine.delegate.DelegateExecution;

public class RequestUtils {

    public static NewUserRequest fromDelagtionExecution(DelegateExecution delegateExecution) {
        String email = delegateExecution.getProcessInstance().getVariable("email").toString();
        String vorname = delegateExecution.getProcessInstance().getVariable("vorname").toString();
        String name = delegateExecution.getProcessInstance().getVariable("name").toString();
        String uid = delegateExecution.getProcessInstance().getVariable("uid").toString();
        String privateKey = delegateExecution.getProcessInstance().getVariable("privateKey").toString();
        String account = delegateExecution.getProcessInstance().getVariable("account").toString();
        boolean vorhanden = (boolean) delegateExecution.getProcessInstance().getVariable("vorhanden");

        NewUserRequest newUserRequest = new NewUserRequest();
        newUserRequest.setEmail(email);
        newUserRequest.setVorname(vorname);
        newUserRequest.setName(name);
        newUserRequest.setVorhanden(vorhanden);
        newUserRequest.setPrivateKey(privateKey);
        newUserRequest.setAccount(account);
        newUserRequest.setUid(uid);
        return newUserRequest;
    }

}
