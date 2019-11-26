package com.absolutions.currencyrates.domain.proxies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionRates {

  private Boolean success;
  private Long timestamp;
  private String base;
  private Date date;
  private Map <String, Map<String, String>> rates;
  private Map<String, String> symbols;
  private Error error;

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public String getBase() {
    return base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Map<String, Map<String, String>> getRates() {
    return rates;
  }

  public void setRates(Map<String, Map<String, String>> rates) {
    this.rates = rates;
  }

  public Map<String, String> getSymbols() {
    return symbols;
  }

  public void setSymbols(Map<String, String> symbols) {
    this.symbols = symbols;
  }

  public Error getError() {
    return error;
  }

  public void setError(Error error) {
    this.error = error;
  }
}

