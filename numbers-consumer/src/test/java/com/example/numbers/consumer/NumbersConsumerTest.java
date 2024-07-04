package com.example.numbers.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NumbersConsumerTest {

  private static final Path CSV_PATH = Paths.get("data/prime-numbers.csv");

  NumbersConsumer numbersConsumer = new NumbersConsumer();

  @BeforeEach
  void setUp() throws IOException {
    if (Files.notExists(CSV_PATH.getParent())) {
      Files.createDirectories(CSV_PATH.getParent());
    }

    Files.write(CSV_PATH, new byte[0]);
  }

  @AfterAll
  static void tearDown() throws IOException {
    Files.deleteIfExists(CSV_PATH);
    Files.deleteIfExists(CSV_PATH.getParent());
  }

  @Test
  void consumeProducedNumbers_withValidNumbers() throws IOException {
    String concatenatedNumbers = "2,3,4,5,6,7";

    numbersConsumer.consumeProducedNumbers(concatenatedNumbers);

    List<String> lines = Files.readAllLines(CSV_PATH);
    assertThat(lines).contains("2,3,5,7");
  }

  @Test
  void consumeProducedNumbers_withNoPrimeNumbers() throws IOException {
    String concatenatedNumbers = "4,6,8,10,12";

    numbersConsumer.consumeProducedNumbers(concatenatedNumbers);

    List<String> lines = Files.readAllLines(CSV_PATH);

    assertThat(lines).isEmpty();
  }

  @Test
  void consumeProducedNumbers_withEmptyList() {
    String concatenatedNumbers = "";

    assertThrows(
        NoNumbersException.class,
        () -> numbersConsumer.consumeProducedNumbers(concatenatedNumbers));
  }

  @Test
  void consumeProducedNumbers_withInvalidNumbers() {
    String concatenatedNumbers = "a,b,c";

    assertThrows(
        NumberFormatException.class,
        () -> {
          numbersConsumer.consumeProducedNumbers(concatenatedNumbers);
        });
  }

  @Test
  void consumeProducedNumbers_withNegativeNumbers() throws IOException {
    String concatenatedNumbers = "-2,-3,-5,-7";

    numbersConsumer.consumeProducedNumbers(concatenatedNumbers);

    List<String> lines = Files.readAllLines(CSV_PATH);
    assertThat(lines).isEmpty();
  }

  @Test
  void consumeProducedNumbers_withMixedValidAndInvalidNumbers() {
    String concatenatedNumbers = "2,3,x,5,y,7";

    assertThrows(
        NumberFormatException.class,
        () -> {
          numbersConsumer.consumeProducedNumbers(concatenatedNumbers);
        });
  }

  @Test
  void consumeProducedNumbers_withLargeNumbers() throws IOException {
    String concatenatedNumbers = "997,1009,1013,1021,1031";

    numbersConsumer.consumeProducedNumbers(concatenatedNumbers);

    List<String> lines = Files.readAllLines(CSV_PATH);
    assertThat(lines).contains("997,1009,1013,1021,1031");
  }

  @Test
  void consumeProducedNumbers_withSingleNumber() throws IOException {
    String concatenatedNumbers = "13";

    numbersConsumer.consumeProducedNumbers(concatenatedNumbers);

    List<String> lines = Files.readAllLines(CSV_PATH);
    assertThat(lines).contains("13");
  }
}
