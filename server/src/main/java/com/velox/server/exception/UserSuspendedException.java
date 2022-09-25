package com.velox.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserSuspendedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserSuspendedException(String email) {
        super(String.format("User suspended  %s", email));
    }
}
