package com.absolutions.currencyrates.repositories;

import com.absolutions.currencyrates.domain.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
  Currency findFirstByCode(String code);
}
