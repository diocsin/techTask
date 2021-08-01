package by.ilyushenko.tech.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;

public class ConverterCsv {

    @SuppressWarnings("unchecked")
    public static <T> CsvToBean<T> getCsvBean(final Class<T> clazz, final Reader reader) {
        return new CsvToBeanBuilder(reader)
                .withType(clazz)
                .build();
    }
}
