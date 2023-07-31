package com.borymskyi.exchangeratesapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExchangeRateDto {

    @JsonProperty(value = "base_currency_code")
    String baseCurrencyCode;

    @JsonProperty(value = "target_currency_code")
    String targetCurrencyCode;

    @JsonProperty(value = "rate")
    String rate;
}
