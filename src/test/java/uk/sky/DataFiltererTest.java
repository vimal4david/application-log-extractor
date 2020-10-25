package uk.sky;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class DataFiltererTest {

    private static final String COUNTRY_EXISTS_ONCE = "GB";
    private static final String COUNTRY_EXISTS_MULTIPLE = "US";
    private static final long RESPONSE_TIME_BELOW_LIMIT = 100;
    private static final long RESPONSE_TIME_AT_AVERAGE = 526;

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
                DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                        emptyFile, COUNTRY_EXISTS_ONCE, RESPONSE_TIME_BELOW_LIMIT);

        // then
        Collection<String> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    public void returnEmptyListForEmptyFileWhenFilterByResponseTimeAboveAverage()
            throws FileNotFoundException {
        // given
        FileReader source = getEmptyFile();

        // when
        Collection<?> actual = DataFilterer.filterByResponseTimeAboveAverage(source);

        // then
        Collection<String> expected = Collections.emptyList();
        assertEquals(expected, actual);
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
                DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                        emptyFile, COUNTRY_EXISTS_ONCE, RESPONSE_TIME_BELOW_LIMIT);

        // then
        Collection<String> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    public void returnEmptyListForHeaderLineFileWhenFilterByResponseTimeAboveAverage()
            throws FileNotFoundException {
        // given
        FileReader source = getHeaderLineFile();

        // when
        Collection<?> actual = DataFilterer.filterByResponseTimeAboveAverage(source);

        // then
        Collection<String> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    public void returnSingletonListForSingleLineFileWhenFilterByCountry()
            throws FileNotFoundException {
        // given
        FileReader source = getSingleLineFile();

        // when
        Collection<?> actual = DataFilterer.filterByCountry(source, COUNTRY_EXISTS_ONCE);

        // then
        Collection<?> expected = getFilteredSingletonList();
        assertEquals(expected, actual);
    }

    @Test
    public void returnSingletonListForSingleLineFileWhenFilterByCountryWithResponseTimeAboveLimit()
            throws FileNotFoundException {
        // given
        FileReader source = getSingleLineFile();

        // when
        Collection<?> actual =
                DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                        source, COUNTRY_EXISTS_ONCE, RESPONSE_TIME_BELOW_LIMIT);

        // then
        Collection<?> expected = getFilteredSingletonList();
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
        Collection<?> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    public void returnExpectedListForDoubleLineFileWhenFilterByCountry()
            throws FileNotFoundException {
        // given
        FileReader source = getMultiLineFile();
        // String country = "US";

        // when
        Collection<?> actual = DataFilterer.filterByCountry(source, COUNTRY_EXISTS_MULTIPLE);

        // then
        ArrayList<DataLine> expected = getFilteredDataLines();
        assertEquals(expected, actual);
    }

    @Test
    public void returnExpectedListForDoubleLineFileWhenFilterByCountryWithResponseTimeAboveLimit()
            throws FileNotFoundException {
        // given
        FileReader source = getMultiLineFile();

        // when
        Collection<?> actual =
                DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                        source, COUNTRY_EXISTS_MULTIPLE, RESPONSE_TIME_AT_AVERAGE);

        // then
        ArrayList<DataLine> expected = getFilteredDataLines();
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
        ArrayList<DataLine> expected = getFilteredDataLines();
        assertEquals(expected, actual);
    }

    private List<DataLine> getFilteredSingletonList() {
        return Collections.singletonList(new DataLine(1431592497, COUNTRY_EXISTS_ONCE, 200));
    }

    private ArrayList<DataLine> getFilteredDataLines() {
        ArrayList<DataLine> expected = new ArrayList<>();
        expected.add(new DataLine(1433190845, COUNTRY_EXISTS_MULTIPLE, 539));
        expected.add(new DataLine(1433666287, COUNTRY_EXISTS_MULTIPLE, 789));
        expected.add(new DataLine(1432484176, COUNTRY_EXISTS_MULTIPLE, 850));
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

    private FileReader openFile(String filename) throws FileNotFoundException {
        return new FileReader(new File(filename));
    }
}
