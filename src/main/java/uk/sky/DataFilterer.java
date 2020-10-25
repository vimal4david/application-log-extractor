package uk.sky;

import java.io.Reader;
import java.util.Collection;
import java.util.Collections;

public class DataFilterer {
    public static Collection<?> filterByCountry(Reader source, String country) {
        return Collections.emptyList();
    }

    public static Collection<?> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) {
        return Collections.emptyList();
    }

    public static Collection<?> filterByResponseTimeAboveAverage(Reader source) {
        return Collections.emptyList();
    }
}