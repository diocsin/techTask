package by.ilyushenko.tech.util;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ConverterCsv {

    public static <T> List<T> convertCsv(Class<T> tClass, String fileName) {
        List<T> beans = new ArrayList<>();
        try {
            beans = new CsvToBeanBuilder(new FileReader(fileName))
                    .withType(tClass)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return beans;
    }
}
