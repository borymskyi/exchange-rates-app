package com.borymskyi.exchangeratesapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Builder
@RequiredArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExchangeRateDto implements Serializable {

    private static final long serialVersionUID = 4431461082604617301L;

    @JsonProperty(value = "base_currency_code")
    String baseCurrencyCode;

    @JsonProperty(value = "target_currency_code")
    String targetCurrencyCode;

    @JsonProperty(value = "rate")
    String rate;
}
