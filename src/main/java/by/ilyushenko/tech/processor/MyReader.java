package by.ilyushenko.tech.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

abstract class MyReader<T> {

    private String filePath;

    private Path path;

    Class<T> clazz;

    BufferedReader fileReader;

    MyReader(String filePath, Class<T> clazz) {
        this.filePath = filePath;
        this.clazz = clazz;
    }

    void readFile() throws IOException {
        createPath();
        createReader();
        doSpecialClassReader();
        doPrepareFoIterate();
    }

    private void createPath() {
        path = Paths.get(filePath);
    }

    void createReader() throws IOException {
        fileReader = Files.newBufferedReader(path);
    }

    String getFilePath() {
        return filePath;
    }

    abstract void doSpecialClassReader();

    abstract void doPrepareFoIterate() throws IOException;

    abstract boolean hasNext() throws IOException;

    abstract T doBean();

    abstract void closeRead() throws IOException;
}
