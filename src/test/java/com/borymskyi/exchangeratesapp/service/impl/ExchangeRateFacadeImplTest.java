package com.borymskyi.exchangeratesapp.service.impl;

import com.borymskyi.exchangeratesapp.model.dto.ExchangeRateDto;
import com.borymskyi.exchangeratesapp.model.dto.ExchangeRateWithMetaDataDto;
import com.borymskyi.exchangeratesapp.model.dto.MetaDataDto;
import com.borymskyi.exchangeratesapp.model.mapper.ExchangeRateMapper;
import com.borymskyi.exchangeratesapp.model.pojo.CurrencyUnit;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRate;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRateWithMetaData;
import com.borymskyi.exchangeratesapp.model.pojo.MetaData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ExchangeRateFacadeImplTest {

    @InjectMocks
    private ExchangeRateFacadeImpl exchangeRateFacade;

    @Mock
    private AVExchangeRateServiceImpl avExchangeRateService;

    @Mock
    private V6ExchangeRateServiceImpl v6ExchangeRateService;

    @Mock
    private ExchangeRateMapper exchangeRateMapper;



    private final CurrencyUnit base = new CurrencyUnit("USD");
    private final CurrencyUnit target = new CurrencyUnit("UAH");


    @Test
    void getLatestExchangeRateWithMinimumPriceForBuying_shouldCompareTwoRatesAndReturnDtoWithMinimumPriceForBuying() {
        JsonNode preparedAVexchangeRate = getValidAVExchangeRateJson();
        JsonNode preparedV6exchangeRate = getValidV6ExchangeRateJson();
        ExchangeRateWithMetaData preparedParsedJsonFromAVapi = getValidExchangeRateWithMetaDataFromAVapi();
        ExchangeRateWithMetaData preparedParsedJsonFromV6api = getValidExchangeRateWithMetaDataFromV6api();
        ExchangeRateWithMetaDataDto preparedExchangeRateWithMinimumPriceForBuyingDto = getValidExchangeRateWithMinimumPriceForBuyingDto();

        doReturn(preparedAVexchangeRate).when(avExchangeRateService).getLatestExchangeRateAsJson(base, target);
        doReturn(preparedV6exchangeRate).when(v6ExchangeRateService).getLatestExchangeRateAsJson(base, target);
        doReturn(preparedParsedJsonFromAVapi).when(avExchangeRateService).parseLatestExchangeRateJson(
                preparedAVexchangeRate, base, target
        );
        doReturn(preparedParsedJsonFromV6api).when(v6ExchangeRateService).parseLatestExchangeRateJson(
                preparedV6exchangeRate, base, target
        );
        doReturn(preparedExchangeRateWithMinimumPriceForBuyingDto).when(exchangeRateMapper)
                .exchangeRateWithMetaDataToDto(any(ExchangeRateWithMetaData.class));

        ExchangeRateWithMetaDataDto actualResult = exchangeRateFacade.getLatestExchangeRateWithMinimumPriceForBuying(
                base, target
        );

        assertNotNull(actualResult.getMetaData());
        assertNotNull(actualResult.getExchangeRate());
        assertEquals(actualResult.getExchangeRate().getBaseCurrencyCode(), base.getCurrencyCode());
        assertEquals(actualResult.getExchangeRate().getTargetCurrencyCode(), target.getCurrencyCode());
        assertEquals(actualResult.getExchangeRate().getRate(), "36.56200000");
    }

    @SneakyThrows
    private JsonNode getValidAVExchangeRateJson() {
        String jsonString = "{\n" +
                "    \"Realtime Currency Exchange Rate\": {\n" +
                "        \"1. From_Currency Code\": \"USD\",\n" +
                "        \"2. From_Currency Name\": \"United States Dollar\",\n" +
                "        \"3. To_Currency Code\": \"UAH\",\n" +
                "        \"4. To_Currency Name\": \"Ukrainian Hryvnia\",\n" +
                "        \"5. Exchange Rate\": \"36.56200000\",\n" +
                "        \"6. Last Refreshed\": \"2023-08-10 18:29:27\",\n" +
                "        \"7. Time Zone\": \"UTC\",\n" +
                "        \"8. Bid Price\": \"36.56150000\",\n" +
                "        \"9. Ask Price\": \"36.56310000\"\n" +
                "    }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }

    @SneakyThrows
    private JsonNode getValidV6ExchangeRateJson() {
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

    private ExchangeRateWithMetaData getValidExchangeRateWithMetaDataFromAVapi() {
        return ExchangeRateWithMetaData.builder()
                .metaData(
                        MetaData.builder()
                                .sourceOfData("https://www.alphavantage.co")
                                .lastUpdatedAt(Instant.parse("2023-08-10T18:29:27Z"))
                                .build()
                )
                .exchangeRate(
                        new ExchangeRate(
                                new CurrencyUnit("USD"),
                                new CurrencyUnit("UAH"),
                                new BigDecimal("36.56200000")
                        )
                )
                .build();
    }

    private ExchangeRateWithMetaData getValidExchangeRateWithMetaDataFromV6api() {
        return ExchangeRateWithMetaData.builder()
                .metaData(
                        MetaData.builder()
                                .sourceOfData("https://v6.exchangerate-api.com")
                                .lastUpdatedAt(Instant.parse("2023-08-10T00:00:01Z"))
                                .build()
                )
                .exchangeRate(
                        new ExchangeRate(
                                new CurrencyUnit("USD"),
                                new CurrencyUnit("UAH"),
                                new BigDecimal("36.8301")
                        )
                )
                .build();
    }

    private ExchangeRateWithMetaDataDto getValidExchangeRateWithMinimumPriceForBuyingDto() {
        return ExchangeRateWithMetaDataDto.builder()
                .metaData(
                        MetaDataDto.builder()
                                .sourceOfData("https://www.alphavantage.co")
                                .lastUpdatedAt(Instant.parse("2023-08-10T18:29:27Z"))
                                .build()
                )
                .exchangeRate(
                        ExchangeRateDto.builder()
                                .baseCurrencyCode("USD")
                                .targetCurrencyCode("UAH")
                                .rate("36.56200000")
                                .build()
                )
                .build();
    }
}