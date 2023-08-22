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
public class ExchangeRateWithMetaDataDto implements Serializable {

    private static final long serialVersionUID = -4344153806575947962L;

    @JsonProperty(value = "meta_data")
    MetaDataDto metaData;

    @JsonProperty(value = "exchange_rate")
    ExchangeRateDto exchangeRate;
}
