package uk.sky;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

public class DataFiltererTest {
    @Test
    public void whenEmpty() throws FileNotFoundException {
        assertTrue(DataFilterer.filterByCountry(openFile("src/test/resources/empty"), "GB").isEmpty());
    }

    private FileReader openFile(String filename) throws FileNotFoundException {
        return new FileReader(new File(filename));
    }
}
