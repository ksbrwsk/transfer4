package de.hpm.registration;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class IamLayer {

    private final ConcurrentHashMap<String, OktaUser> DATA = new ConcurrentHashMap<>();

    public NewUserRequest checkExists(NewUserRequest newUserRequest) {
        log.info("Prüfe Benutzer vorhanden -> {}", newUserRequest.getEmail());
        var email = newUserRequest.getEmail();
        Optional<OktaUser> first = DATA.values()
                .stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
        newUserRequest.setVorhanden(first.isPresent());
        log.info("Prüfe Benutzer vorhanden -> {} -> {}", newUserRequest.getEmail(), first.isPresent());
        return newUserRequest;
    }

    public NewUserRequest register(NewUserRequest newUserRequest) {
        log.info("Registriere Benutzer -> {}", newUserRequest);
        newUserRequest.setStatus(Status.STAGED);
        OktaUser oktaUser = new OktaUser();
        oktaUser.setEmail(newUserRequest.getEmail());
        oktaUser.setName(newUserRequest.getName());
        oktaUser.setVorname(newUserRequest.getVorname());
        oktaUser.setStatus(Status.STAGED);
        oktaUser.setUid(UUID.randomUUID().toString());
        newUserRequest.setUid(oktaUser.getUid());
        this.DATA.put(oktaUser.getEmail(), oktaUser);
        log.info("Benutzer wurde registriert -> {}", newUserRequest);
        return newUserRequest;
    }

    public NewUserRequest createAccount(NewUserRequest newUserRequest) {
        log.info("Érzeuge Account -> {}", newUserRequest);
        var user = DATA.get(newUserRequest.getEmail());
        user.setAccount(newUserRequest.getAccount());
        this.DATA.put(user.getEmail(), user);
        log.info("Account wurde gesetzt -> {}", newUserRequest);
        return newUserRequest;
    }

    public NewUserRequest createPrivateKey(NewUserRequest newUserRequest) {
        log.info("Setze Private Key -> {}", newUserRequest);
        var user = DATA.get(newUserRequest.getEmail());
        if(user == null) {
            this.register(newUserRequest);
            user = DATA.get(newUserRequest.getEmail());
        }
        user.setPrivateKey(newUserRequest.getPrivateKey());
        this.DATA.put(user.getEmail(), user);
        log.info("PrivateKey wurde gesetzt -> {}", newUserRequest);
        return newUserRequest;
    }
}
