package com.assignment.assignment.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MetaDataNotFountException extends RuntimeException {
    public MetaDataNotFountException() {
    }

    public MetaDataNotFountException(String s) {
        super(s);
    }

}
