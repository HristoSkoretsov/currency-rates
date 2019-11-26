package com.absolutions.currencyrates.api.responses;

import org.owasp.encoder.Encode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericResponse<E, B> {

  private boolean successfull;
  private E errors;
  private B body;

  public GenericResponse() {

  }

  public boolean isSuccessfull() {
    return successfull;
  }

  public GenericResponse<E, B> setSuccessfull(boolean successfull) {
    this.successfull = successfull;
    return this;
  }

  public E getErrors() {
    return errors;
  }

  @SuppressWarnings("unchecked")
  public GenericResponse<E, B> setErrors(E errors) {
    if (errors instanceof String) {
      this.errors = (E) Encode.forHtml((String) errors);
    } else {
      this.errors = errors;
    }

    return this;
  }

  public B getBody() {
    return body;
  }

  public GenericResponse<E, B> setBody(B body) {
    this.body = body;
    return this;
  }

}
