package de.hpm.registration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OktaUser {
    private String uid;
    private String email;
    private String vorname;
    private String name;
    private Status status = Status.REQUESTED;
    private String privateKey;
    private String account;
}
