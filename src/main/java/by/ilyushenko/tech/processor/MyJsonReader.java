package by.ilyushenko.tech.processor;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

class MyJsonReader<T> extends MyReader<T> {

    private JsonReader reader;

    MyJsonReader(String pathStr, Class<T> clazz) {
        super(pathStr, clazz);
    }

    @Override
    void doSpecialClassReader() {
        reader = new JsonReader(fileReader);
    }

    @Override
    void doPrepareFoIterate() throws IOException {
        reader.beginArray();
    }

    @Override
    boolean hasNext() throws IOException {
        return reader.hasNext();
    }


    @Override
    void closeRead() throws IOException {
        reader.endArray();
        fileReader.close();
    }

    @Override
    T doBean() {
        final Gson gson = new Gson();
        return gson.fromJson(reader, clazz);
    }
}
