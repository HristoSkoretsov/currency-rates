package com.absolutions.currencyrates.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.Index;
import java.util.Date;

@Entity
@Table(name = "currency_pair_rate", indexes = { @Index(name = "CURRENCYPAIRINDEX", columnList = "id,date,base") })
public class CurrencyPairRate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "base", nullable = false, length = 254)
  private String base;

  @Column(name = "quote", nullable = false, length = 254)
  private String quote;

  @Column(name = "rate", nullable = false)
  private Float rate;

  @Column(name = "date", nullable = false)
  private Date date;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getBase() {
    return base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public String getQuote() {
    return quote;
  }

  public void setQuote(String quote) {
    this.quote = quote;
  }

  public Float getRate() {
    return rate;
  }

  public void setRate(Float rate) {
    this.rate = rate;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
