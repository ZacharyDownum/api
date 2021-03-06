package com.shepherdjerred.easely.api.controller.payload;

import lombok.Getter;
import lombok.Setter;

public class LoginPayload implements Payload {

    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;

    @Override
    public boolean isValid() {
        return true;
    }

}
