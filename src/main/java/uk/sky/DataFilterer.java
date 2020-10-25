package uk.sky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class DataFilterer {

  private DataFilterer() {}

  private static final String COMMA_SEPARATOR = ",";
  private static final long DEFAULT_RESPONSE_TIME_LIMIT = 0;
  private static final int VALID_LINE_SPLIT_SIZE = 3;

  public static Collection<?> filterByCountry(Reader source, String country) {
    return filterByCountryAndResponseTime(source, country, DEFAULT_RESPONSE_TIME_LIMIT);
  }

  public static Collection<?> filterByCountryWithResponseTimeAboveLimit(
      Reader source, String country, long limit) {
    return filterByCountryAndResponseTime(source, country, limit);
  }

  public static Collection<?> filterByResponseTimeAboveAverage(Reader source) {
    List<DataLine> result = new ArrayList<>();
    AtomicLong responseTimeAggregate = new AtomicLong();
    try (BufferedReader reader = new BufferedReader(source)) {
      List<DataLine> unFilteredList =
          reader
              .lines()
              .skip(1)
              .map(DataFilterer::strToDataLine)
              .map(
                  dataLine -> {
                    responseTimeAggregate.addAndGet(dataLine.getResponseTime());
                    return dataLine;
                  })
              .collect(Collectors.toList());

      if (unFilteredList.isEmpty()) {
        return result;
      }

      long average = responseTimeAggregate.get() / unFilteredList.size();
      result =
          unFilteredList.parallelStream()
              .filter(dataLine -> dataLine.getResponseTime() > average)
              .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  private static Collection<?> filterByCountryAndResponseTime(
      Reader source, String country, long limit) {
    List<DataLine> result = new ArrayList<>();
    try (BufferedReader reader = getReader(source)) {
      result =
          reader
              .lines()
              .skip(1)
              .map(DataFilterer::strToDataLine)
              .filter(dataLine -> isCountryEquals(country, dataLine))
              .filter(dataLine -> isResponseTimeAboveLimit(limit, dataLine))
              .collect(Collectors.toList());
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return result;
  }

  private static BufferedReader getReader(Reader source) {
    return new BufferedReader(source);
  }

  private static DataLine strToDataLine(String line) {
    String[] split = line.split(COMMA_SEPARATOR);
    if (split.length != VALID_LINE_SPLIT_SIZE) {
      throw new InvalidDataException("Invalid Data Line: " + line);
    }
    return new DataLine(Long.parseLong(split[0]), split[1], Long.parseLong(split[2]));
  }

  private static boolean isCountryEquals(String country, DataLine dataLine) {
    return country.equals(dataLine.getCountryCode());
  }

  private static boolean isResponseTimeAboveLimit(long limit, DataLine dataLine) {
    return dataLine.getResponseTime() > limit;
  }
}
