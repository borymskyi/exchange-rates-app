package com.borymskyi.exchangeratesapp.service;

import com.borymskyi.exchangeratesapp.exception.RequestToExchangeRateApiException;
import com.borymskyi.exchangeratesapp.model.pojo.CurrencyUnit;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRateWithMetaData;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public interface ExchangeRateService {

    JsonNode getLatestExchangeRateAsJson(
            @NonNull CurrencyUnit base,
            @NonNull CurrencyUnit target
    ) throws RequestToExchangeRateApiException;

    ExchangeRateWithMetaData parseLatestExchangeRateJson(
            JsonNode jsonResponse,
            CurrencyUnit base,
            CurrencyUnit target
    );

    String getLatestExchangeRateUrl();
}
