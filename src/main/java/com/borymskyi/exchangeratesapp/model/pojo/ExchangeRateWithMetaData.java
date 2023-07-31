package com.borymskyi.exchangeratesapp.model.pojo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExchangeRateWithMetaData {

    MetaData metaData;

    ExchangeRate exchangeRate;

}