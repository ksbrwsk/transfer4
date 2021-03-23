package de.hpm.registration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewUserRequest {
    private String email;
    private String vorname;
    private String name;
    private Status status = Status.REQUESTED;
    private boolean vorhanden = false;
    private String uid;
    private String privateKey;
    private String account;
}
