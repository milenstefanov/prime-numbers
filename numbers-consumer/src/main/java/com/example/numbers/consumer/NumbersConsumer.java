package com.example.numbers.consumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NumbersConsumer {

  private static final String TOPIC = "produced-numbers";
  private static final Path CSV_PATH = Paths.get("data/prime-numbers.csv");

  @KafkaListener(topics = TOPIC, groupId = "")
  public void consumeProducedNumbers(String concatenatedNumbers) {

    log.info("Numbers received: {}", concatenatedNumbers);

    if (concatenatedNumbers.isBlank()) {
      throw new NoNumbersException();
    }

    String[] splittedNumbers = concatenatedNumbers.split(",");

    List<Integer> primeNumbers =
        Arrays.stream(splittedNumbers).map(Integer::valueOf).filter(this::isPrimeNumber).toList();

    if (!primeNumbers.isEmpty()) {
      writeToFile(primeNumbers);
    }
  }

  private boolean isPrimeNumber(int number) {
    if (number <= 1) {
      return false;
    }

    for (int i = 2; i <= Math.sqrt(number); i++) {
      if (number % i == 0) {
        return false;
      }
    }

    return true;
  }

  private void writeToFile(List<Integer> numbers) {
    try {
      if (Files.notExists(CSV_PATH.getParent())) {
        Files.createDirectories(CSV_PATH.getParent());
      }
      try (var writer =
              Files.newBufferedWriter(
                  CSV_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
          var csvPrinter =
              new CSVPrinter(
                  writer,
                  CSVFormat.DEFAULT.builder().setDelimiter(',').setRecordSeparator('\n').build())) {
        csvPrinter.printRecord(numbers);
        log.info("Writing numbers to {}", CSV_PATH);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
