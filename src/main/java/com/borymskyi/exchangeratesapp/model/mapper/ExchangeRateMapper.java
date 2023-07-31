package com.borymskyi.exchangeratesapp.model.mapper;

import com.borymskyi.exchangeratesapp.model.dto.ExchangeRateDto;
import com.borymskyi.exchangeratesapp.model.dto.ExchangeRateWithMetaDataDto;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRate;
import com.borymskyi.exchangeratesapp.model.pojo.ExchangeRateWithMetaData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    @Mapping(
            source = "exchangeRateWithMetaData.exchangeRate",
            target = "exchangeRate",
            qualifiedByName = "mapExchangeRateToDto"
    )
    ExchangeRateWithMetaDataDto exchangeRateWithMetaDataToDto(ExchangeRateWithMetaData exchangeRateWithMetaData);

    @Named(value = "mapExchangeRateToDto")
    default ExchangeRateDto mapExchangeRateToDto(ExchangeRate exchangeRate) {
        return ExchangeRateDto.builder()
                .baseCurrencyCode(exchangeRate.getBaseCurrencyCode().getCurrencyCode())
                .targetCurrencyCode(exchangeRate.getTargetCurrencyCode().getCurrencyCode())
                .rate(exchangeRate.getRate().toPlainString())
                .build();
    }
}
