package com.borymskyi.exchangeratesapp.service;

import com.borymskyi.exchangeratesapp.model.dto.ExchangeRateWithMetaDataDto;
import com.borymskyi.exchangeratesapp.model.pojo.CurrencyUnit;
import org.springframework.stereotype.Service;

@Service
public interface ExchangeRateFacade {

    ExchangeRateWithMetaDataDto getLatestExchangeRateWithMinimumPriceForBuying(CurrencyUnit base, CurrencyUnit target);

}