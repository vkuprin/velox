package com.velox.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSuchUserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  public NoSuchUserException(long id) {
        super(String.format("No user with client number  %s", id));
    }
}
