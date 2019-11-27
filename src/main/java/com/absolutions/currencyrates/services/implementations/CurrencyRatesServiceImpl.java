package com.absolutions.currencyrates.services.implementations;

import com.absolutions.currencyrates.domain.entities.Currency;
import com.absolutions.currencyrates.domain.entities.CurrencyPairRate;
import com.absolutions.currencyrates.domain.proxies.ConversionRates;
import com.absolutions.currencyrates.domain.proxies.CurrentConversionRates;
import com.absolutions.currencyrates.repositories.CurrencyPairRateRepository;
import com.absolutions.currencyrates.repositories.CurrencyRepository;
import com.absolutions.currencyrates.services.interfaces.CurrencyRatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyRatesServiceImpl implements CurrencyRatesService {

  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Value("${currency.api.base.uri}")
  private transient String baseUri;

  @Value("${api.key}")
  private transient String apiKey;

  @Autowired
  private CurrencyPairRateRepository currencyPairRateRepository;

  @Autowired
  private CurrencyRepository currencyRepository;

  @Override
  public ConversionRates getAllCurrencies() throws JsonProcessingException {

    String uri = String.format("%ssymbols?access_key=%s", baseUri, apiKey);
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> currenciesMap;
    String jsonInput= getResponse(uri).getBody();
    if (jsonInput != null && jsonInput.length() > 0) {
      ConversionRates conversionRates = mapper.readValue(jsonInput, ConversionRates.class);
      currenciesMap = conversionRates.getSymbols();
      this.populateWithCurrencies(currenciesMap);

      return conversionRates;
    }
    return null;
  }

  @Override
  public ConversionRates getAllExchangeRatesFromNinetyNine(String base)
    throws JsonProcessingException, ParseException {
    LocalDate startDate = LocalDate.of(1999, 1, 1);
    LocalDate today = LocalDate.now();
    ObjectMapper mapper = new ObjectMapper();
    Map <String, Map<String, String>> rates = new LinkedHashMap<>();
    List<CurrencyPairRate> currencyPairRates = new ArrayList<>();
    ConversionRates tmp = null;
    while(startDate.isBefore(today)) {
      LocalDate endDate = startDate.plusDays(365);
      if(endDate.isAfter(today)) {
        endDate=today;
      }
      String uri = String.format("%stimeseries?access_key=%s&start_date=%s&end_date=%s&base=%s", baseUri, apiKey, startDate.toString(), endDate.toString(), base);
      String jsonInput= getResponse(uri).getBody();
      if (jsonInput != null && jsonInput.length() > 0) {
        tmp = mapper.readValue(jsonInput, ConversionRates.class);
        populateCurrencyPairRate(tmp, base, currencyPairRates);
        rates.putAll(tmp.getRates());
      }
      startDate = endDate.plusDays(1);
    }
    currencyPairRateRepository.saveAll(currencyPairRates);
    if (tmp != null) {
      tmp.setRates(rates);
    }
    return tmp;
  }

  @Override
  public CurrentConversionRates getAllExchangeRatesFromDate(String base, String date)
    throws JsonProcessingException, ParseException {
    String uri = String.format("%s%s?access_key=%s&base=%s", baseUri, date, apiKey, base);
    ObjectMapper mapper = new ObjectMapper();
    String jsonInput= getResponse(uri).getBody();
    List<CurrencyPairRate> currencyPairRates = new ArrayList<>();
    if (jsonInput != null && jsonInput.length() > 0) {
      CurrentConversionRates currentConversionRates = mapper.readValue(jsonInput, CurrentConversionRates.class);
      for (Map.Entry<String, String> quoteValue: currentConversionRates.getRates().entrySet()) {
        CurrencyPairRate cpRate = new CurrencyPairRate();
        cpRate.setBase(base);
        cpRate.setDate(dateFormat.parse(date));
        cpRate.setQuote(quoteValue.getKey());
        cpRate.setRate(Float.parseFloat(quoteValue.getValue()));
        currencyPairRates.add(cpRate);
      }
      currencyPairRateRepository.saveAll(currencyPairRates);
      return currentConversionRates;
    }
    return null;
  }

  @Override
  public CurrentConversionRates getAllExchangeRatesForCurrentDay(String base)
    throws JsonProcessingException {
    String currentDate = dateFormat.format(new Date());
    String uri = String.format("%s%s?access_key=%s&base=%s", baseUri, currentDate, apiKey, base);
    ObjectMapper mapper = new ObjectMapper();
    String jsonInput= getResponse(uri).getBody();
    List<CurrencyPairRate> currencyPairRates = new ArrayList<>();
    if (jsonInput != null && jsonInput.length() > 0) {
      CurrentConversionRates currentConversionRates = mapper.readValue(jsonInput, CurrentConversionRates.class);
      for (Map.Entry<String, String> quoteValue: currentConversionRates.getRates().entrySet()) {
        CurrencyPairRate cpRate = new CurrencyPairRate();
        cpRate.setBase(base);
        cpRate.setDate(new Date());
        cpRate.setQuote(quoteValue.getKey());
        cpRate.setRate(Float.parseFloat(quoteValue.getValue()));
        currencyPairRates.add(cpRate);
      }
      currencyPairRateRepository.saveAll(currencyPairRates);
      return currentConversionRates;
    }
    return null;
  }

  @Override
  public Boolean populateDatabase(String base) throws JsonProcessingException, ParseException {
    LocalDate startDate = LocalDate.of(1999, 1, 1);
    LocalDate today = LocalDate.now();
    ObjectMapper mapper = new ObjectMapper();
    List<CurrencyPairRate> rates = new ArrayList<>();
    ConversionRates tmp;
    while(startDate.isBefore(today)) {
      LocalDate endDate = startDate.plusDays(365);
      if(endDate.isAfter(today)) {
        endDate=today;
      }
      String uri = String.format("%stimeseries?access_key=%s&start_date=%s&end_date=%s&base=%s", baseUri, apiKey, startDate.toString(), endDate.toString(), base);
      String jsonInput= getResponse(uri).getBody();
      if (jsonInput != null && jsonInput.length() > 0) {
        tmp = mapper.readValue(jsonInput, ConversionRates.class);
        populateCurrencyPairRate(tmp, base, rates);
      }
      startDate = endDate.plusDays(1);
    }
    currencyPairRateRepository.saveAll(rates);
    return true;
  }

  @Override
  public Boolean populateCurrencies() throws JsonProcessingException {
    String uri = String.format("%ssymbols?access_key=%s", baseUri, apiKey);
    ObjectMapper mapper = new ObjectMapper();
    String jsonInput= getResponse(uri).getBody();
    Map<String, String> currenciesMap = new HashMap<>();
    if ( jsonInput != null && jsonInput.length() > 0) {
      ConversionRates conversionRates = mapper.readValue(jsonInput, ConversionRates.class);
      currenciesMap = conversionRates.getSymbols();
    }
    this.populateWithCurrencies(currenciesMap);
    return true;
  }

  @Override
  public List<CurrencyPairRate> getReport(String currencyName, String date) throws ParseException {
    Currency currency = currencyRepository.findFirstByCode(currencyName);
    if (currency == null) {
      throw new IllegalArgumentException("Invalid base currency");
    }
    return currencyPairRateRepository.getAllByBaseAndQuoteAndDateAfter("EUR", currencyName, dateFormat.parse(date));
  }

  private HttpEntity<String> getResponse(String uri){
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/json");
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity entity = new HttpEntity(headers);
    return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

  }

  private void populateWithCurrencies(Map<String, String> currenciesMap ) {
    for (Map.Entry<String,String> entry : currenciesMap.entrySet()) {
      Currency currency = new Currency();
      currency.setCode(entry.getKey());
      currency.setName(entry.getValue());
      currencyRepository.save(currency);
    }
  }

  private void populateCurrencyPairRate(ConversionRates tmp, String base, List<CurrencyPairRate> rates)
    throws ParseException {
    for (Map.Entry<String, Map<String, String>> dateQuoteValue : tmp.getRates().entrySet()) {
      for (Map.Entry<String, String> quoteValue: dateQuoteValue.getValue().entrySet()) {
        CurrencyPairRate cpRate = new CurrencyPairRate();
        cpRate.setBase(base);
        cpRate.setDate(dateFormat.parse(dateQuoteValue.getKey()));
        cpRate.setQuote(quoteValue.getKey());
        cpRate.setRate(Float.parseFloat(quoteValue.getValue()));
        rates.add(cpRate);
      }
    }
  }
}
