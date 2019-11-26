package com.absolutions.currencyrates.services.implementations;

import com.absolutions.currencyrates.domain.proxies.ConversionRates;
import com.absolutions.currencyrates.domain.proxies.CurrentConversionRates;
import com.absolutions.currencyrates.services.interfaces.CurrencyRatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CurrencyRatesServiceImpl implements CurrencyRatesService {

  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Value("${currency.api.base.uri}")
  private transient String baseUri;

  @Value("${api.key}")
  private transient String apiKey;

  @Override
  public ConversionRates getAllCurrencies() throws JsonProcessingException {

    String uri = String.format("%ssymbols?access_key=%s", baseUri, apiKey);
    ObjectMapper mapper = new ObjectMapper();
    String jsonInput= getResponse(uri).getBody();
    if ( jsonInput != null && jsonInput.length() > 0) {
      return mapper.readValue(jsonInput, ConversionRates.class);
    }
    return null;
  }

  @Override
  public ConversionRates getAllExchangeRatesFromNinetyNine(String base) throws JsonProcessingException {
    String endDate = dateFormat.format(new Date());
    String uri = String.format("%stimeseries?access_key=%s&start_date=1999-01-01&end_date=%s&base=%s", baseUri, apiKey, endDate, base);
    ObjectMapper mapper = new ObjectMapper();
    //TODO Think how to full whole info
    String jsonInput= getResponse(uri).getBody();
    if (jsonInput != null && jsonInput.length() > 0) {
      return mapper.readValue(jsonInput, ConversionRates.class);
    }
    return null;

  }

  @Override
  public ConversionRates getAllExchangeRatesFromDate(String base, String startDate) throws JsonProcessingException {
    String endDate = dateFormat.format(new Date());
    String uri = String.format("%stimeseries?access_key=%s&start_date=%s&end_date=%s&base=%s", baseUri, apiKey, startDate, endDate, base);
    ObjectMapper mapper = new ObjectMapper();
    String jsonInput= getResponse(uri).getBody();
    if (jsonInput != null && jsonInput.length() > 0) {
      return mapper.readValue(jsonInput, ConversionRates.class);
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
    if (jsonInput != null && jsonInput.length() > 0) {
      return mapper.readValue(jsonInput, CurrentConversionRates.class);
    }
    return null;
  }

  private HttpEntity<String> getResponse(String uri){
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/json");
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity entity = new HttpEntity(headers);
    return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

  }
}
