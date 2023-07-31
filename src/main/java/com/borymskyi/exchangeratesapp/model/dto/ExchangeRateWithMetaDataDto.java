package com.borymskyi.exchangeratesapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExchangeRateWithMetaDataDto {

    @JsonProperty(value = "meta_data")
    MetaDataDto metaData;

    @JsonProperty(value = "exchange_rate")
    ExchangeRateDto exchangeRate;
}
