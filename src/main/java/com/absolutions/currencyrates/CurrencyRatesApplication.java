package com.absolutions.currencyrates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.absolutions"})
public class CurrencyRatesApplication {

  public static void main(String[] args) {
    SpringApplication.run(CurrencyRatesApplication.class, args);
  }

}
