package com.borymskyi.exchangeratesapp.exception.handler;

import com.borymskyi.exchangeratesapp.exception.InvalidCodeForCurrencyCodeException;
import com.borymskyi.exchangeratesapp.exception.RequestToExchangeRateApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExchangeRateHandlerException {

    private final HttpServletRequest request;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleNullPointerException(InvalidCodeForCurrencyCodeException e) {
        return ResponseException.builder()
                .title(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .instance(request.getRequestURI())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseException handleRequestToExchangeRateApiException(RequestToExchangeRateApiException e) {
        return ResponseException.builder()
                .title("Invalid API Call")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail(e.getMessage())
                .instance(request.getRequestURI())
                .build();
    }
}
