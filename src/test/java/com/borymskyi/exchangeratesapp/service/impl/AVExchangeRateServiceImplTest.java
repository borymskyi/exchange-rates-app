package com.borymskyi.exchangeratesapp.service.impl;

import com.borymskyi.exchangeratesapp.exception.RequestToExchangeRateApiException;
import com.borymskyi.exchangeratesapp.model.pojo.CurrencyUnit;
import com.borymskyi.exchangeratesapp.output.ExchangeRateHttpOutput;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AVExchangeRateServiceImplTest {

    @InjectMocks
    private AVExchangeRateServiceImpl avExchangeRateService;

    @Mock
    private ExchangeRateHttpOutput exchangeClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                avExchangeRateService,
                "aVExchangeRateUrl",
                "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s"
        );
    }

    @Test
    void getLatestExchangeRateAsJson_shouldReturnValidJsonWithExchangeRate() {
        CurrencyUnit base = new CurrencyUnit("USD");
        CurrencyUnit target = new CurrencyUnit("UAH");
        JsonNode preparedJsonNode = getValidJsonLatestExchangeRate();
        doReturn(preparedJsonNode).when(exchangeClient).sendRequestToGetLatestExchangeRateByPreparedURI(any());

        JsonNode actual = avExchangeRateService.getLatestExchangeRateAsJson(base, target);

        assertThat(actual).isNotNull();
        verify(exchangeClient, times(1)).sendRequestToGetLatestExchangeRateByPreparedURI(any());
    }

    @Test
    void getLatestExchangeRateAsJson_shouldThrowInvalidApiCallException() {
        CurrencyUnit base = new CurrencyUnit("USD");
        CurrencyUnit target = new CurrencyUnit("UAH");
        JsonNode preparedJsonNode = getJsonLatestExchangeRateWithError();
        doReturn(preparedJsonNode).when(exchangeClient).sendRequestToGetLatestExchangeRateByPreparedURI(any());

        assertThrows(RequestToExchangeRateApiException.class, () -> avExchangeRateService.getLatestExchangeRateAsJson(base, target));
        verify(exchangeClient, times(1)).sendRequestToGetLatestExchangeRateByPreparedURI(any());
    }

    @SneakyThrows
    private JsonNode getValidJsonLatestExchangeRate() {
        String jsonString = "{\n" +
                "    \"Realtime Currency Exchange Rate\": {\n" +
                "        \"1. From_Currency Code\": \"USD\",\n" +
                "        \"2. From_Currency Name\": \"United States Dollar\",\n" +
                "        \"3. To_Currency Code\": \"UAH\",\n" +
                "        \"4. To_Currency Name\": \"Ukrainian Hryvnia\",\n" +
                "        \"5. Exchange Rate\": \"36.90920000\",\n" +
                "        \"6. Last Refreshed\": \"2023-07-24 19:06:35\",\n" +
                "        \"7. Time Zone\": \"UTC\",\n" +
                "        \"8. Bid Price\": \"36.90826000\",\n" +
                "        \"9. Ask Price\": \"36.91031000\"\n" +
                "    }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }

    @SneakyThrows
    private JsonNode getJsonLatestExchangeRateWithError() {
        String jsonString = "{\n" +
                "    \"Error Message\": \"Invalid API call. Please retry or visit the documentation (https://www.alphavantage.co/documentation/) for CURRENCY_EXCHANGE_RATE.\"\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }
}