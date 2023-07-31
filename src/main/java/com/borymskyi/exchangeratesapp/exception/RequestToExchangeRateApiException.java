package com.borymskyi.exchangeratesapp.exception;

public class RequestToExchangeRateApiException extends RuntimeException {
    public RequestToExchangeRateApiException(String message) {
        super(message);
    }
    public RequestToExchangeRateApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
