package de.hpm.registration.listener;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;

@Log4j2
public class TaskAssignmentListener implements TaskListener {

    // TODO: Set Mail Server Properties
    private static final String HOST = "127.0.0.1";
    private static final String USER = "admin@localhost.org";
    private static final String PWD = "toomanysecrets";

    public void notify(DelegateTask delegateTask) {

        String assignee = delegateTask.getAssignee();
        String taskId = delegateTask.getId();

        if (assignee != null) {

            // Get User Profile from User Management
            IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
            User user = identityService.createUserQuery().userId(assignee).singleResult();

            if (user != null) {

                // Get Email Address from User Profile
                String recipient = user.getEmail();

                if (recipient != null && !recipient.isEmpty()) {

                    Email email = new SimpleEmail();
                    email.setSmtpPort(25);
                    email.setCharset("utf-8");
                    email.setHostName(HOST);
                    //email.setAuthentication(USER, PWD);

                    try {
                        email.setFrom("noreply@camunda.org");
                        email.setSubject("Task assigned: " + delegateTask.getName());
                        email.setMsg("Please complete: http://localhost:8080/camunda/app/tasklist/default/#/task=" + taskId);

                        email.addTo(recipient);

                        email.send();
                        log.info("Task Assignment Email successfully sent to user '" + assignee + "' with address '" + recipient + "'.");

                    } catch (Exception e) {
                        log.info("Could not send email to assignee", e);
                    }

                } else {
                    log.info("Not sending email to user " + assignee + "', user has no email address.");
                }

            } else {
                log.info("Not sending email to user " + assignee + "', user is not enrolled with identity service.");
            }

        }

    }

}
