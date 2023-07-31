package com.borymskyi.exchangeratesapp.exception;

public class InvalidCodeForCurrencyCodeException extends RuntimeException {

    public InvalidCodeForCurrencyCodeException(String message) {
        super(message);
    }

}