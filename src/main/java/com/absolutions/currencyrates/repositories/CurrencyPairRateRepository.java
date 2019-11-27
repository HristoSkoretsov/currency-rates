package com.absolutions.currencyrates.repositories;

import com.absolutions.currencyrates.domain.entities.CurrencyPairRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface CurrencyPairRateRepository extends JpaRepository<CurrencyPairRate, Long> {
   List<CurrencyPairRate> getAllByBaseAndQuoteAndDateAfter(String base, String quote, Date date);
}
