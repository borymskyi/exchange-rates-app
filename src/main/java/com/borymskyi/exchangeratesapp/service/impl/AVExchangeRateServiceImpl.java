package com.borymskyi.exchangeratesapp.service.impl;

import com.borymskyi.exchangeratesapp.exception.RequestToExchangeRateApiException;
import com.borymskyi.exchangeratesapp.model.pojo.CurrencyUnit;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRate;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRateWithMetaData;
import com.borymskyi.exchangeratesapp.model.pojo.MetaData;
import com.borymskyi.exchangeratesapp.output.ExchangeRateHttpOutput;
import com.borymskyi.exchangeratesapp.service.ExchangeRateService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class AVExchangeRateServiceImpl implements ExchangeRateService {

    private final String aVExchangeRateUrl;
    private final ExchangeRateHttpOutput exchangeClient;

    @Autowired
    public AVExchangeRateServiceImpl(
            @Value("${provider.AVExchangeRate.url}") String aVExchangeRateUrl,
            ExchangeRateHttpOutput exchangeClient,
            HttpServletRequest request
    ) {
        this.aVExchangeRateUrl = aVExchangeRateUrl;
        this.exchangeClient = exchangeClient;
    }

    @Override
    public JsonNode getLatestExchangeRateAsJson(
            @NonNull CurrencyUnit base,
            @NonNull CurrencyUnit target
    ) throws RequestToExchangeRateApiException {

        URI aVExchangeRateURI = buildLatestExchangeRateURI(base, target);

        JsonNode jsonResponse = exchangeClient.sendRequestToGetLatestExchangeRateByPreparedURI(aVExchangeRateURI);

        checkForErrorMessageInJsonResponse(jsonResponse);

        return jsonResponse;
    }

    private URI buildLatestExchangeRateURI(CurrencyUnit base, CurrencyUnit target) {
        return URI.create(
                aVExchangeRateUrl
                        .replaceFirst("%s", base.getCurrencyCode())
                        .replaceFirst("%s", target.getCurrencyCode())
        );
    }

    private void checkForErrorMessageInJsonResponse(JsonNode jsonResponse) {
        if (jsonResponse.has("Error Message")) {
            String message = jsonResponse.get("Error Message").asText();
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
        JsonNode jNext = jsonResponse.get("Realtime Currency Exchange Rate");

        ExchangeRateWithMetaData parsedData = ExchangeRateWithMetaData.builder()
                .metaData(MetaData.builder()
                        .sourceOfData(getHostByUrl(this.getLatestExchangeRateUrl()))
                        .lastUpdatedAt(parseDateLastUpdate(jNext.get("6. Last Refreshed").asText()))
                        .build()
                )
                .exchangeRate(
                        new ExchangeRate(
                                new CurrencyUnit(jNext.get("1. From_Currency Code").asText()),
                                new CurrencyUnit(jNext.get("3. To_Currency Code").asText()),
                                new BigDecimal(jNext.get("5. Exchange Rate").asText())
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    @Override
    public String getLatestExchangeRateUrl() {
        return aVExchangeRateUrl;
    }
}
