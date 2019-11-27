package com.absolutions.currencyrates.services.interfaces;

import com.absolutions.currencyrates.domain.entities.CurrencyPairRate;
import com.absolutions.currencyrates.domain.proxies.ConversionRates;
import com.absolutions.currencyrates.domain.proxies.CurrentConversionRates;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.ParseException;
import java.util.List;

public interface CurrencyRatesService {

  ConversionRates getAllCurrencies() throws JsonProcessingException;

  ConversionRates getAllExchangeRatesFromNinetyNine(String base)
    throws JsonProcessingException, ParseException;

  CurrentConversionRates getAllExchangeRatesFromDate(String base, String date)
    throws JsonProcessingException, ParseException;

  CurrentConversionRates getAllExchangeRatesForCurrentDay(String base) throws JsonProcessingException;

  Boolean populateDatabase(String base) throws JsonProcessingException, ParseException;

  Boolean populateCurrencies() throws JsonProcessingException, ParseException;

  List<CurrencyPairRate> getReport(String currency, String data) throws ParseException;
}
