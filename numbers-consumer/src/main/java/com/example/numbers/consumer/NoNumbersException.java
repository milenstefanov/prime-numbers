package com.example.numbers.consumer;

public class NoNumbersException extends RuntimeException {
  public NoNumbersException() {
    super("Message empty/no numbers to process");
  }
}
