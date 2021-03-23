package de.hpm.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class IamRestController {

    private final IamLayer iamLayer;

    @PostMapping("/api/iam/check")
    public NewUserRequest checkUserExists(@RequestBody NewUserRequest newUserRequest) {
        NewUserRequest exists = this.iamLayer.checkExists(newUserRequest);
        return exists;
    }

    @PostMapping("/api/iam/register")
    public NewUserRequest registerUser(@RequestBody NewUserRequest newUserRequest) {
        NewUserRequest registered = this.iamLayer.register(newUserRequest);
        return registered;
    }

    @PostMapping("/api/iam/account")
    public NewUserRequest createAccount(@RequestBody NewUserRequest newUserRequest) {
        NewUserRequest user = this.iamLayer.createAccount(newUserRequest);
        return user;
    }

    @PostMapping("/api/iam/privateKey")
    public NewUserRequest createPrivateKey(@RequestBody NewUserRequest newUserRequest) {
        NewUserRequest user = this.iamLayer.createPrivateKey(newUserRequest);
        return user;
    }

}
