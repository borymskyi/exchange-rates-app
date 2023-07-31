package com.borymskyi.exchangeratesapp.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
public class ExchangeRate implements Comparable<ExchangeRate> {

    private final CurrencyUnit baseCurrencyCode;
    private final CurrencyUnit targetCurrencyCode;
    private final BigDecimal rate;

    public ExchangeRate(CurrencyUnit baseCurrencyCode, CurrencyUnit targetCurrencyCode, BigDecimal rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public int compareRate(ExchangeRate o) {
        Objects.requireNonNull(o);
        return this.getRate().compareTo(o.getRate());
    }

    public boolean compareCurrency(ExchangeRate o) {
        Objects.requireNonNull(o);
        int compare = this.getBaseCurrencyCode().compareTo(o.getBaseCurrencyCode());
        if (compare == 0) {
            compare = this.getTargetCurrencyCode().compareTo(o.getTargetCurrencyCode());
        }
        if (compare == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(ExchangeRate o) {
        Objects.requireNonNull(o);
        int compare = this.getBaseCurrencyCode().compareTo(o.getBaseCurrencyCode());
        if (compare == 0) {
            compare = this.getTargetCurrencyCode().compareTo(o.getTargetCurrencyCode());
        }
        if (compare == 0) {
            compare = this.getRate().compareTo(o.getRate());
        }
        return compare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRate that = (ExchangeRate) o;

        return Objects.equals(baseCurrencyCode, that.baseCurrencyCode) &&
                Objects.equals(targetCurrencyCode, that.targetCurrencyCode)
                && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrencyCode, targetCurrencyCode, rate);
    }
}
