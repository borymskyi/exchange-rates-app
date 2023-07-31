package com.borymskyi.exchangeratesapp.input;

import com.borymskyi.exchangeratesapp.model.dto.ExchangeRateWithMetaDataDto;
import com.borymskyi.exchangeratesapp.model.pojo.CurrencyUnit;
import com.borymskyi.exchangeratesapp.service.ExchangeRateFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1/best-exchange-rate/")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExchangeRateHttpInput {

    ExchangeRateFacade exchangeRateFacade;

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestExchangeRate(
            @RequestParam(value = "base", defaultValue = "USD") String base,
            @RequestParam(value = "target", defaultValue = "UAH") String target
    ) {

        ExchangeRateWithMetaDataDto exchangeRateWithMetaDataDto = exchangeRateFacade
                .getLatestExchangeRateWithMinimumPriceForBuying(
                        new CurrencyUnit(base),
                        new CurrencyUnit(target));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exchangeRateWithMetaDataDto);
    }

    @GetMapping("/available")
    public String getAvailableCurrencyCodes() {
        return "list of code";
    }

    @GetMapping("/history")
    public String getExchangeRatesHistoryByParam(
            @RequestParam(value = "code", defaultValue = "USD") String code,
            @RequestParam(value = "identifier", required = false) String identifier,
            @RequestParam(value = "date") String date
    ) {
        return "list of exchange rates";
    }
}
