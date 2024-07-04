package com.example.numbers.producer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NumbersProducer {

  private static final int MAX_RANDOM_INTEGERS = 5;
  private static final int MAX_NUMBERS_COUNT = 100;

  private static final String TOPIC = "produced-numbers";
  private static final Path CSV_PATH = Paths.get("data/generated-numbers.csv");

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final Random random = new Random();
  private Stream<Integer> integerStream = Stream.empty();
  private int streamSize = 0;

  @Scheduled(fixedRate = 1000)
  public void generateNumbers() {
    int amountOfIntegers = random.nextInt(MAX_RANDOM_INTEGERS) + 1;

    if (streamSize + amountOfIntegers > MAX_NUMBERS_COUNT) {
      amountOfIntegers = MAX_NUMBERS_COUNT - streamSize;
    }

    Stream<Integer> randomIntegers = random.ints(amountOfIntegers, 1, 10000).boxed();

    streamSize += amountOfIntegers;

    integerStream = Stream.concat(integerStream, randomIntegers);

    log.info(
        "Numbers generated -> amountOfIntegers: {} :: streamSize: {}",
        amountOfIntegers,
        streamSize);

    checkConditionsAndSendNumbers();
  }

  private void checkConditionsAndSendNumbers() {
    if (streamSize == MAX_NUMBERS_COUNT) {
      log.info("Maximum number of integers reached. Sending ...");

      String numbers = integerStream.map(String::valueOf).collect(Collectors.joining(","));

      kafkaTemplate.send(TOPIC, numbers);
      writeToFile(numbers);

      integerStream = Stream.empty();
      streamSize = 0;
    }
  }

  private void writeToFile(String numbers) {
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
        csvPrinter.printRecord(List.of(numbers.split(",")));
        log.info("Writing numbers to {}", CSV_PATH);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
