package com.mvassoler.oauht.mvassoleroauth.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

@Getter
public class AuthUser extends User implements Serializable {

    private static final long serialVersionUID = 1l;

    private String fullName;
    private UUID userId;

    public AuthUser(com.mvassoler.oauht.mvassoleroauth.domain.User user) {
        super(user.getUsername(), user.getPassword(), Collections.emptyList());
        this.fullName = user.getUsername();
        this.userId = user.getId();
    }

}
