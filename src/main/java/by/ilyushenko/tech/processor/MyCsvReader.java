package by.ilyushenko.tech.processor;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.util.Iterator;

class MyCsvReader<T> extends MyReader<T> {

    private CsvToBean<T> csvToBean;

    private Iterator<T> iterator;

    MyCsvReader(String filePath, Class<T> clazz) {
        super(filePath, clazz);
    }

    @Override
    void doSpecialClassReader() {
        csvToBean = new CsvToBeanBuilder<T>(fileReader)
                .withType(clazz)
                .build();
    }

    @Override
    boolean hasNext() throws IOException {
        return iterator.hasNext();
    }

    @Override
    void doPrepareFoIterate() throws IOException {
        iterator = csvToBean.iterator();
    }

    @Override
    void closeRead() throws IOException {
        fileReader.close();
    }

    @Override
    T doBean() {
        return iterator.next();
    }
}
