package com.borymskyi.exchangeratesapp.output;

import com.borymskyi.exchangeratesapp.exception.RequestToExchangeRateApiException;
import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RequiredArgsConstructor
@Slf4j
@Component
public class ExchangeRateHttpOutput {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final HttpServletRequest request;

    // TODO: fix counter and add monitoring
    private Counter RETRIES_COUNTER = Counter
            .builder("exchange_rates_retries_counter")
            .register(Metrics.globalRegistry);

    public JsonNode sendRequestToGetLatestExchangeRateByPreparedURI(URI preparedUri) throws RequestToExchangeRateApiException {
        JsonNode jsonResponse;

        try {
            jsonResponse = retryTemplate.execute(
                    context -> {
                        RETRIES_COUNTER.increment();
                        return restTemplate.getForObject(preparedUri, JsonNode.class);
                    }
            );
            log.info("Response from URI {}: {}. Current retries: {}",
                    preparedUri, jsonResponse.toString(), RETRIES_COUNTER.count());

        } catch (Exception e) {
            String message = String.format(
                    "[EXCEPTION]: %s. Error executing a request to 3rd party system to get the latest exchange rate. " +
                    "URI: %s. Number of retries: %s.", e.getClass(), preparedUri, RETRIES_COUNTER.count());
            log.warn(message + " Remote addr: {}", request.getRemoteAddr());
            throw new RequestToExchangeRateApiException(message);

        }

        return jsonResponse;
    }
}
