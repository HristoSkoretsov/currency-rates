package com.absolutions.currencyrates.services.interfaces;

import com.absolutions.currencyrates.domain.proxies.ConversionRates;
import com.absolutions.currencyrates.domain.proxies.CurrentConversionRates;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CurrencyRatesService {

  ConversionRates getAllCurrencies() throws JsonProcessingException;

  ConversionRates getAllExchangeRatesFromNinetyNine(String base) throws JsonProcessingException;

  ConversionRates getAllExchangeRatesFromDate(String base, String date) throws JsonProcessingException;

  CurrentConversionRates getAllExchangeRatesForCurrentDay(String base) throws JsonProcessingException;
}
