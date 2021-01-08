package uk.sky;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DataFiltererTest {

  private static final String COUNTRY_EXISTS_ONCE = "GB";
  private static final String COUNTRY_EXISTS_MULTIPLE = "US";
  private static final String ERROR_MESSAGE = "Invalid Data Line: GB,200";
  private static final long RESPONSE_TIME_BELOW_LIMIT = 100;
  private static final long RESPONSE_TIME_AT_AVERAGE = 526;

  @Rule public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void returnEmptyListForEmptyFileWhenFilterByCountry() throws FileNotFoundException {
    // given
    FileReader emptyFile = getEmptyFile();

    // when
    Collection<?> actual = DataFilterer.filterByCountry(emptyFile, COUNTRY_EXISTS_ONCE);

    // then
    assertTrue(actual.isEmpty());
  }

  @Test
  public void returnEmptyListForEmptyFileWhenFilterByCountryWithResponseTimeAboveLimit()
          throws FileNotFoundException {
    // given
    FileReader emptyFile = getEmptyFile();

    // when
    Collection<?> actual =
            DataFilterer.filterByCountryWithResponseTimeAboveLimit(emptyFile, COUNTRY_EXISTS_ONCE, RESPONSE_TIME_BELOW_LIMIT);

    // then
    assertTrue(actual.isEmpty());
  }

  @Test
  public void returnEmptyListForEmptyFileWhenFilterByResponseTimeAboveAverage()
          throws FileNotFoundException {
    // given
    FileReader source = getEmptyFile();

    // when
    Collection<?> actual = DataFilterer.filterByResponseTimeAboveAverage(source);

    // then
    assertTrue(actual.isEmpty());
  }

  @Test
  public void returnEmptyListForHeaderLineFileWhenFilterByCountry() throws FileNotFoundException {
    // given
    FileReader emptyFile = getHeaderLineFile();

    // when
    Collection<?> actual = DataFilterer.filterByCountry(emptyFile, COUNTRY_EXISTS_ONCE);

    // then
    assertTrue(actual.isEmpty());
  }

  @Test
  public void returnEmptyListForHeaderLineFileWhenFilterByCountryWithResponseTimeAboveLimit()
          throws FileNotFoundException {
    // given
    FileReader emptyFile = getHeaderLineFile();

    // when
    Collection<?> actual =
            DataFilterer.filterByCountryWithResponseTimeAboveLimit(emptyFile, COUNTRY_EXISTS_ONCE, RESPONSE_TIME_BELOW_LIMIT);

    // then
    assertTrue(actual.isEmpty());
  }

  @Test
  public void returnEmptyListForHeaderLineFileWhenFilterByResponseTimeAboveAverage()
          throws FileNotFoundException {
    // given
    FileReader source = getHeaderLineFile();

    // when
    Collection<?> actual = DataFilterer.filterByResponseTimeAboveAverage(source);

    // then
    assertTrue(actual.isEmpty());
  }

  @Test
  public void returnSingletonListForSingleLineFileWhenFilterByCountry()
          throws FileNotFoundException {
    // given
    FileReader source = getSingleLineFile();

    // when
    Collection<?> actual = DataFilterer.filterByCountry(source, COUNTRY_EXISTS_ONCE);

    // then
    Collection<?> expected = getFilteredDataLineList(COUNTRY_EXISTS_ONCE);
    assertEquals(expected, actual);
  }

  @Test
  public void returnSingletonListForSingleLineFileWhenFilterByCountryWithResponseTimeAboveLimit()
          throws FileNotFoundException {
    // given
    FileReader source = getSingleLineFile();

    // when
    Collection<?> actual =
            DataFilterer.filterByCountryWithResponseTimeAboveLimit(source, COUNTRY_EXISTS_ONCE, RESPONSE_TIME_BELOW_LIMIT);

    // then
    Collection<?> expected = getFilteredDataLineList(COUNTRY_EXISTS_ONCE);
    assertEquals(expected, actual);
  }

  @Test
  public void returnEmptyListForSingleLineFileWhenFilterByResponseTimeAboveAverage()
          throws FileNotFoundException {
    // given
    FileReader source = getSingleLineFile();

    // when
    Collection<?> actual = DataFilterer.filterByResponseTimeAboveAverage(source);

    // then
    assertTrue(actual.isEmpty());
  }

  @Test
  public void returnExpectedListForDoubleLineFileWhenFilterByCountry()
          throws FileNotFoundException {
    // given
    FileReader source = getMultiLineFile();

    // when
    Collection<?> actual = DataFilterer.filterByCountry(source, COUNTRY_EXISTS_MULTIPLE);

    // then
    List<DataLine> expected = getFilteredDataLineList(COUNTRY_EXISTS_MULTIPLE);
    assertEquals(expected, actual);
  }

  @Test
  public void returnExpectedListForDoubleLineFileWhenFilterByCountryWithResponseTimeAboveLimit()
          throws FileNotFoundException {
    // given
    FileReader source = getMultiLineFile();

    // when
    Collection<?> actual =
            DataFilterer.filterByCountryWithResponseTimeAboveLimit(source, COUNTRY_EXISTS_MULTIPLE, RESPONSE_TIME_AT_AVERAGE);

    // then
    List<DataLine> expected = getFilteredDataLineList(COUNTRY_EXISTS_MULTIPLE);
    assertEquals(expected, actual);
  }

  @Test
  public void returnExpectedListForDoubleLineFileWhenFilterByResponseTimeAboveAverage()
          throws FileNotFoundException {
    // given
    FileReader source = getMultiLineFile();

    // when
    Collection<?> actual = DataFilterer.filterByResponseTimeAboveAverage(source);

    // then
    List<DataLine> expected = getFilteredDataLineList(COUNTRY_EXISTS_MULTIPLE);
    assertEquals(expected, actual);
  }

  @Test
  public void InvalidDataExceptionThrownForInvalidDataWhenFilterByCountry()
          throws FileNotFoundException {
    // given
    FileReader invalidFile = getInvalidFile();
    exceptionRule.expect(InvalidDataException.class);
    exceptionRule.expectMessage(ERROR_MESSAGE);

    // when
    DataFilterer.filterByCountry(invalidFile, COUNTRY_EXISTS_ONCE);

    // then
    // Expected exception
  }

  @Test
  public void
  InvalidDataExceptionThrownForInvalidDataWhenFilterByCountryWithResponseTimeAboveLimit()
          throws FileNotFoundException {
    // given
    FileReader invalidFile = getInvalidFile();
    exceptionRule.expect(InvalidDataException.class);
    exceptionRule.expectMessage(ERROR_MESSAGE);

    // when
    DataFilterer.filterByCountryWithResponseTimeAboveLimit(
            invalidFile, COUNTRY_EXISTS_ONCE, RESPONSE_TIME_BELOW_LIMIT);

    // then
    // Expected exception
  }

  @Test
  public void InvalidDataExceptionThrownForInvalidDataWhenFilterByResponseTimeAboveAverage()
          throws FileNotFoundException {
    // given
    FileReader invalidFile = getInvalidFile();
    exceptionRule.expect(InvalidDataException.class);
    exceptionRule.expectMessage(ERROR_MESSAGE);

    // when
    DataFilterer.filterByResponseTimeAboveAverage(invalidFile);

    // then
    // Expected exception
  }

  private List<DataLine> getFilteredDataLineList(String countryToFilter) {
    return getDataLines().parallelStream()
            .filter(x -> countryToFilter.equals(x.getCountryCode()))
            .collect(Collectors.toList());
  }

  private ArrayList<DataLine> getDataLines() {
    ArrayList<DataLine> expected = new ArrayList<>();
    expected.add(new DataLine(1433190845, "US", 539));
    expected.add(new DataLine(1431592497, "GB", 200));
    expected.add(new DataLine(1433666287, "US", 789));
    expected.add(new DataLine(1432484176, "US", 850));
    expected.add(new DataLine(1432364076, "DE", 415));
    return expected;
  }

  private FileReader getEmptyFile() throws FileNotFoundException {
    return openFile("src/test/resources/empty");
  }

  private FileReader getHeaderLineFile() throws FileNotFoundException {
    return openFile("src/test/resources/header-line");
  }

  private FileReader getSingleLineFile() throws FileNotFoundException {
    return openFile("src/test/resources/single-line");
  }

  private FileReader getMultiLineFile() throws FileNotFoundException {
    return openFile("src/test/resources/multi-lines");
  }

  private FileReader getInvalidFile() throws FileNotFoundException {
    return openFile("src/test/resources/invalid-line");
  }

  private FileReader openFile(String filename) throws FileNotFoundException {
    return new FileReader(new File(filename));
  }
}
