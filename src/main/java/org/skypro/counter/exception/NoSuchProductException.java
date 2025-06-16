package org.skypro.counter.exception;

public class NoSuchProductException extends RuntimeException {
  public NoSuchProductException(String message) {
    super(message);
  }
}