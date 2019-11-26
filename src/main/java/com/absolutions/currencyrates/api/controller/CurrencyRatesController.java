package com.absolutions.currencyrates.api.controller;

import com.absolutions.currencyrates.api.responses.GenericResponse;
import com.absolutions.currencyrates.domain.proxies.ConversionRates;
import com.absolutions.currencyrates.domain.proxies.CurrentConversionRates;
import com.absolutions.currencyrates.services.interfaces.CurrencyRatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/")
public class CurrencyRatesController {

  @Autowired
  private CurrencyRatesService currencyRatesService;

  @RequestMapping(value = "/currencies", method = RequestMethod.GET)
  public GenericResponse<?, ?> getAllCurrencies()
    throws JsonProcessingException {
    ConversionRates conversionRates = currencyRatesService.getAllCurrencies();
    if (conversionRates.getSuccess()) {
      return new GenericResponse<>().setSuccessfull(Boolean.TRUE).setBody(new ArrayList<>(conversionRates.getSymbols().keySet()));
    }
    return new GenericResponse<>().setSuccessfull(Boolean.FALSE).setErrors(conversionRates.getError()).setBody(null);
  }

  @RequestMapping(value = "/rates/historic/{base}", method = RequestMethod.GET)
  public GenericResponse<?, ?> getAllExchangeRatesFromNinetyNine(@PathVariable String base)
    throws JsonProcessingException {
    ConversionRates conversionRates = currencyRatesService.getAllExchangeRatesFromNinetyNine(base);
    if (conversionRates.getSuccess()) {
    return new GenericResponse<>().setSuccessfull(Boolean.TRUE).setBody(new HashMap<>(conversionRates.getRates()));
    }
    return new GenericResponse<>().setSuccessfull(Boolean.FALSE).setErrors(conversionRates.getError()).setBody(null);
  }

  @RequestMapping(value = "/rates/historic/{base}/{date}", method = RequestMethod.GET)
  public GenericResponse<?, ?> getAllExchangeRatesFromDate(@PathVariable String base, @PathVariable String date)
    throws JsonProcessingException {
    ConversionRates conversionRates = currencyRatesService.getAllExchangeRatesFromDate(base, date);
    if (conversionRates.getSuccess()) {
      return new GenericResponse<>().setSuccessfull(Boolean.TRUE).setBody(conversionRates.getRates());
    }
    return new GenericResponse<>().setSuccessfull(Boolean.FALSE).setErrors(conversionRates.getError()).setBody(null);
  }

  @RequestMapping(value = "/rates/latest/{base}", method = RequestMethod.GET)
  public GenericResponse<?, ?> getAllExchangeRatesForCurrentDay(@PathVariable String base)
    throws JsonProcessingException {
    CurrentConversionRates conversionRates = currencyRatesService.getAllExchangeRatesForCurrentDay(base);
    if (conversionRates.getSuccess()) {
      return new GenericResponse<>().setSuccessfull(Boolean.TRUE).setBody(new HashMap<>(conversionRates.getRates()));
    }
    return new GenericResponse<>().setSuccessfull(Boolean.FALSE).setErrors(conversionRates.getError()).setBody(null);
  }

  @RequestMapping(value = "/report/{currency}/{date} ", method = RequestMethod.GET)
  public GenericResponse<?, ?> getReport(@PathVariable String currency, @PathVariable String date)
    throws JsonProcessingException {
    //TODO MAKE REPORT
//    CurrentConversionRates conversionRates = currencyRatesService.getAllExchangeRatesForCurrentDay(base);
//    if (conversionRates.getSuccess()) {
//      return new GenericResponse<>().setSuccessfull(Boolean.TRUE).setBody(new HashMap<>(conversionRates.getRates()));
//    }
    return new GenericResponse<>().setSuccessfull(Boolean.FALSE).setErrors(null).setBody(null);
  }

}
