package com.borymskyi.exchangeratesapp.model.pojo;

import com.borymskyi.exchangeratesapp.exception.InvalidCodeForCurrencyCodeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * This interface represents a unit of currency such as the Euro, Ukrainian hryvnia,
 * US Dollar, Bitcoin or other.
 *
 * @author Dmytrii Borymskyi
 *
 * @version 1.0
 *
 * @see <a href="https://en.wikipedia.org/wiki/Currency">Wikipedia: Currency</a>
 */
@Getter
@Setter
@ToString
public class CurrencyUnit implements Comparable<CurrencyUnit> {

    private final String currencyCode;

    public CurrencyUnit(String currencyCode) {
        if (!StringUtils.hasText(currencyCode)) {
            throw new InvalidCodeForCurrencyCodeException("currencyCode required: " + currencyCode);
        }
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CurrencyUnit that = (CurrencyUnit) o;
        return this.getCurrencyCode().equals(that.getCurrencyCode());
    }

    @Override
    public int hashCode() {
        return currencyCode.hashCode();
    }

    @Override
    public int compareTo(CurrencyUnit o) {
        Objects.requireNonNull(o);
        return this.getCurrencyCode().compareTo(o.getCurrencyCode());
    }
}
