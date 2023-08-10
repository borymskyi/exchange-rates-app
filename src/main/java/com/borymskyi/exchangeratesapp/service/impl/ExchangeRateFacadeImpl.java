package com.borymskyi.exchangeratesapp.service.impl;

import com.borymskyi.exchangeratesapp.exception.RequestToExchangeRateApiException;
import com.borymskyi.exchangeratesapp.model.dto.ExchangeRateWithMetaDataDto;
import com.borymskyi.exchangeratesapp.model.mapper.ExchangeRateMapper;
import com.borymskyi.exchangeratesapp.model.pojo.CurrencyUnit;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRateWithMetaData;
import com.borymskyi.exchangeratesapp.service.ExchangeRateFacade;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExchangeRateFacadeImpl implements ExchangeRateFacade {

    private final AVExchangeRateServiceImpl aVExchangeRateServiceImpl;
    private final V6ExchangeRateServiceImpl v6ExchangeRateServiceImpl;
    private final ExchangeRateMapper exchangeRateMapper;

    @Autowired
    public ExchangeRateFacadeImpl(
            AVExchangeRateServiceImpl aVExchangeRateServiceImpl,
            V6ExchangeRateServiceImpl v6ExchangeRateServiceImpl,
            ExchangeRateMapper exchangeRateMapper
    ) {
        this.aVExchangeRateServiceImpl = aVExchangeRateServiceImpl;
        this.v6ExchangeRateServiceImpl = v6ExchangeRateServiceImpl;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    @Override
    public ExchangeRateWithMetaDataDto getLatestExchangeRateWithMinimumPriceForBuying(CurrencyUnit base, CurrencyUnit target) {
        JsonNode json1;
        JsonNode json2;
        ExchangeRateWithMetaData parsedJson1;
        ExchangeRateWithMetaData parsedJson2;
        ExchangeRateWithMetaData exchangeRateWithMinimumPriceForBuying;
        ExchangeRateWithMetaDataDto exchangeRateDto;

        try {
            json1 = aVExchangeRateServiceImpl.getLatestExchangeRateAsJson(base, target);

        } catch (RequestToExchangeRateApiException rE1) {
            try {
                json2 = v6ExchangeRateServiceImpl.getLatestExchangeRateAsJson(base, target);
                parsedJson2 = v6ExchangeRateServiceImpl.parseLatestExchangeRateJson(json2, base, target);
                exchangeRateDto = exchangeRateMapper.exchangeRateWithMetaDataToDto(parsedJson2);
                return exchangeRateDto;

            } catch (RequestToExchangeRateApiException rE2) {
                String message = String.format(
                        "[EXCEPTION]: Failed to make requests on all exchange rate API. " +
                                " Exception from request to the first API: %s" +
                                " Exception from request to the second API: %s", rE1.getMessage(), rE2.getMessage());
                log.error(message);
                throw new RequestToExchangeRateApiException("Failed to make requests on all exchange rate API.");

            }
        }
        try {
            json2 = v6ExchangeRateServiceImpl.getLatestExchangeRateAsJson(base, target);

        } catch (RequestToExchangeRateApiException rE2) {
            parsedJson1 = aVExchangeRateServiceImpl.parseLatestExchangeRateJson(json1, base, target);
            exchangeRateDto = exchangeRateMapper.exchangeRateWithMetaDataToDto(parsedJson1);
            return exchangeRateDto;

        }
        parsedJson1 = aVExchangeRateServiceImpl.parseLatestExchangeRateJson(json1, base, target);
        parsedJson2 = v6ExchangeRateServiceImpl.parseLatestExchangeRateJson(json2, base, target);
        exchangeRateWithMinimumPriceForBuying = identifyExchangeRateAtMinimumPriceForBuying(parsedJson1, parsedJson2);
        exchangeRateDto = exchangeRateMapper.exchangeRateWithMetaDataToDto(exchangeRateWithMinimumPriceForBuying);
        return exchangeRateDto;
    }

    private ExchangeRateWithMetaData identifyExchangeRateAtMinimumPriceForBuying(
            @NonNull ExchangeRateWithMetaData first,
            @NonNull ExchangeRateWithMetaData second
    ) {
        if (first.getExchangeRate().compareRate(second.getExchangeRate()) < 0) {
            return first;
        } else {
            return second;
        }
    }
}