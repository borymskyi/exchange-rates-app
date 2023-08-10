package com.borymskyi.exchangeratesapp.service.impl;

import com.borymskyi.exchangeratesapp.exception.RequestToExchangeRateApiException;
import com.borymskyi.exchangeratesapp.model.pojo.CurrencyUnit;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRateWithMetaData;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class V6ExchangeRateServiceImplTest {

    @InjectMocks
    private V6ExchangeRateServiceImpl v6ExchangeRateService;

    @Mock
    private ExchangeRateHttpOutput exchangeClient;

    private final CurrencyUnit base = new CurrencyUnit("USD");
    private final CurrencyUnit target = new CurrencyUnit("UAH");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                v6ExchangeRateService,
                "v6ExchangeRateUrl",
                "https://v6.exchangerate-api.com/v6/pair/%s/%s"
        );
    }

    @Test
    void getLatestExchangeRateAsJson_shouldReturnValidJsonWithExchangeRate() {
        JsonNode preparedJsonNode = getValidJsonLatestExchangeRate();
        doReturn(preparedJsonNode).when(exchangeClient).sendRequestToGetLatestExchangeRateByPreparedURI(any());

        JsonNode actual = v6ExchangeRateService.getLatestExchangeRateAsJson(base, target);

        assertNotNull(actual);
        verify(exchangeClient, times(1)).sendRequestToGetLatestExchangeRateByPreparedURI(any());
    }

    @Test
    void getLatestExchangeRateAsJson_firstCase_shouldThrowRequestToExchangeRateApiException() {
        JsonNode preparedJsonNode = getJsonLatestExchangeRateWithErrorFirstCase();
        doReturn(preparedJsonNode).when(exchangeClient).sendRequestToGetLatestExchangeRateByPreparedURI(any());

        assertThrows(RequestToExchangeRateApiException.class, () -> v6ExchangeRateService.getLatestExchangeRateAsJson(base, target));
        verify(exchangeClient, times(1)).sendRequestToGetLatestExchangeRateByPreparedURI(any());
    }

    @Test
    void getLatestExchangeRateAsJson_secondCase_shouldThrowRequestToExchangeRateApiException() {
        JsonNode preparedJsonNode = getJsonLatestExchangeRateWithErrorSecondCase();
        doReturn(preparedJsonNode).when(exchangeClient).sendRequestToGetLatestExchangeRateByPreparedURI(any());

        assertThrows(RequestToExchangeRateApiException.class, () -> v6ExchangeRateService.getLatestExchangeRateAsJson(base, target));
        verify(exchangeClient, times(1)).sendRequestToGetLatestExchangeRateByPreparedURI(any());
    }

    @SneakyThrows
    private JsonNode getJsonLatestExchangeRateWithErrorFirstCase() {
        String jsonString = "{\n" +
                "    \"result\": \"error\",\n" +
                "    \"documentation\": \"https://www.exchangerate-api.com/docs\",\n" +
                "    \"terms-of-use\": \"https://www.exchangerate-api.com/terms\",\n" +
                "    \"error-type\": \"invalid-key\"\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }

    @SneakyThrows
    private JsonNode getJsonLatestExchangeRateWithErrorSecondCase() {
        String jsonString = "{\n" +
                "    \"result\": \"error\",\n" +
                "    \"documentation\": \"https://www.exchangerate-api.com/docs\",\n" +
                "    \"terms-of-use\": \"https://www.exchangerate-api.com/terms\",\n" +
                "    \"error-type\": \"unsupported-code\"\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }

    @Test
    void parseLatestExchangeRateJson_shouldReturnParsedObject() {
        JsonNode dataWhichNeedParse = getValidJsonLatestExchangeRate();

        ExchangeRateWithMetaData actual = v6ExchangeRateService.parseLatestExchangeRateJson(
                dataWhichNeedParse, base, target
        );

        assertNotNull(actual);
        assertNotNull(actual.getMetaData());
        assertNotNull(actual.getExchangeRate());
        assertEquals(actual.getExchangeRate().getBaseCurrencyCode().getCurrencyCode(), base.getCurrencyCode());
        assertEquals(actual.getExchangeRate().getTargetCurrencyCode().getCurrencyCode(), target.getCurrencyCode());
    }

    @SneakyThrows
    private JsonNode getValidJsonLatestExchangeRate() {
        String jsonString = "{\n" +
                "    \"result\": \"success\",\n" +
                "    \"documentation\": \"https://www.exchangerate-api.com/docs\",\n" +
                "    \"terms_of_use\": \"https://www.exchangerate-api.com/terms\",\n" +
                "    \"time_last_update_unix\": 1691625601,\n" +
                "    \"time_last_update_utc\": \"Thu, 10 Aug 2023 00:00:01 +0000\",\n" +
                "    \"time_next_update_unix\": 1691712001,\n" +
                "    \"time_next_update_utc\": \"Fri, 11 Aug 2023 00:00:01 +0000\",\n" +
                "    \"base_code\": \"USD\",\n" +
                "    \"target_code\": \"UAH\",\n" +
                "    \"conversion_rate\": 36.8301\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }
}