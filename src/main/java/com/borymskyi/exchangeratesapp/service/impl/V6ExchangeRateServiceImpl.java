package com.borymskyi.exchangeratesapp.service.impl;

import com.borymskyi.exchangeratesapp.exception.RequestToExchangeRateApiException;
import com.borymskyi.exchangeratesapp.model.pojo.*;
import com.borymskyi.exchangeratesapp.output.ExchangeRateHttpOutput;
import com.borymskyi.exchangeratesapp.service.ExchangeRateService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

@Slf4j
@Service
public class V6ExchangeRateServiceImpl implements ExchangeRateService {

    private final String v6ExchangeRateUrl;
    private final ExchangeRateHttpOutput exchangeClient;

    public V6ExchangeRateServiceImpl(
            @Value("${provider.V6ExchangeRate.url}") String v6ExchangeRateUrl,
            ExchangeRateHttpOutput exchangeClient
    ) {
        this.v6ExchangeRateUrl = v6ExchangeRateUrl;
        this.exchangeClient = exchangeClient;
    }

    @Override
    public JsonNode getLatestExchangeRateAsJson(
            @NonNull CurrencyUnit base,
            @NonNull CurrencyUnit target
    ) throws RequestToExchangeRateApiException {

        URI identifier1Uri = URI.create(
                v6ExchangeRateUrl
                        .replaceFirst("%s", base.getCurrencyCode())
                        .replaceFirst("%s", target.getCurrencyCode())
        );

        JsonNode jsonResponse = exchangeClient.sendRequestToGetLatestExchangeRateByPreparedURI(identifier1Uri);

        checkForErrorMessageInJsonResponse(jsonResponse);

        return jsonResponse;
    }

    private void checkForErrorMessageInJsonResponse(JsonNode jsonResponse) {
        if (jsonResponse.get("result").asText().equals("error")) {
            String message = jsonResponse.get("error-type").asText();
            log.error("[EXCEPTION]: {}, Message: {}", RequestToExchangeRateApiException.class, message);
            throw new RequestToExchangeRateApiException(message);
        }
    }

    @Override
    public ExchangeRateWithMetaData parseLatestExchangeRateJson(
            JsonNode jsonResponse,
            CurrencyUnit base,
            CurrencyUnit target
    ) {
        ExchangeRateWithMetaData parsedData = ExchangeRateWithMetaData.builder()
                .metaData(
                        MetaData.builder()
                                .sourceOfData(getHostByUrl(getLatestExchangeRateUrl()))
                                .lastUpdatedAt(parseDateLastUpdate(jsonResponse.get("time_last_update_utc").asText()))
                                .build()
                )
                .exchangeRate(
                        new ExchangeRate(
                                new CurrencyUnit(jsonResponse.get("base_code").asText()),
                                new CurrencyUnit(jsonResponse.get("target_code").asText()),
                                new BigDecimal(jsonResponse.get("conversion_rate").asText())
                        )
                )
                .build();

        return parsedData;
    }

    private String getHostByUrl(String url) {
        return url.substring(0, url.indexOf("/", 8));
    }

    private Instant parseDateLastUpdate(String date) {
        if (date == null) return null;

        return Instant.from(RFC_1123_DATE_TIME.parse(date));
    }

    @Override
    public String getLatestExchangeRateUrl() {
        return v6ExchangeRateUrl;
    }
}
