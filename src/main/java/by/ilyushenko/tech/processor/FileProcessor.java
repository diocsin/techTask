package by.ilyushenko.tech.processor;

import by.ilyushenko.tech.exception.ServiceException;
import by.ilyushenko.tech.model.ImportObject;
import by.ilyushenko.tech.service.ImportObjectService;
import by.ilyushenko.tech.util.ConverterCsv;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.opencsv.bean.CsvToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class FileProcessor {
    private static final String HEADER_FILE_NAME = "file_name";
    private static final String MSG = "%s received. Content: %s";
    private static final int ARRAY_SIZE = 30000;
    private final ImportObjectService importObjectService;

    @Value("${path.processed.files}")
    private String pathForProcess;

    @Autowired
    public FileProcessor(ImportObjectService importObjectService) {
        this.importObjectService = importObjectService;
    }

    public void process(Message<String> msg) {
        String fileName = (String) msg.getHeaders().get(HEADER_FILE_NAME);
        String regularCsv = "^.*\\.csv$";
        String regularJson = "^.*\\.json$";
        String content = msg.getPayload();
        System.out.println(String.format(MSG, fileName, content));
        Path pathProcessFile = Paths.get(pathForProcess + "/" + fileName);
        try {
            moveFile(content);
            if (Pattern.matches(regularCsv, fileName)) {
                importCsv(pathProcessFile);
            } else if (Pattern.matches(regularJson, fileName)) {
                importJson(pathProcessFile);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void moveFile(final String path) throws ServiceException {
        Path source = Paths.get(path);
        Path pathProcess = Paths.get(pathForProcess);
        try {
            //TODO костыль для работы нескольких экземпляров приложения.
            Files.move(source, pathProcess.resolve(source.getFileName()), StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new ServiceException(String.format("The file - %s does not exist or has already been moved", source.getFileName()));
        }
    }

    private void importJson(final Path pathFileForSave) throws Exception {
        List<ImportObject> importObjectList = new ArrayList<>(ARRAY_SIZE);
        try (Reader fileReader = Files.newBufferedReader(pathFileForSave);
             JsonReader reader = new JsonReader(fileReader)) {
            reader.beginArray();
            int i = 0;
            while (reader.hasNext()) {
                importObjectList.add(new Gson().fromJson(reader, ImportObject.class));
                i++;
                if (i == ARRAY_SIZE) {
                    importObjectService.saveImportObjects(importObjectList);
                    i = 0;
                    importObjectList = new ArrayList<>(ARRAY_SIZE);
                }
            }
            reader.endArray();
            System.out.println("Loading done");
            if (i != 0) {
                importObjectService.saveImportObjects(importObjectList);
            }
        } catch (PersistenceException e) {
            throw new ServiceException(String.format("Error occurred while writing the file - %s to DB", pathFileForSave));
        } catch (IOException e) {
            throw new ServiceException(String.format("Error occurred while reading the file - %s", pathFileForSave));
        }

    }

    private void importCsv(final Path pathFileForSave) throws Exception {
        List<ImportObject> importObjectList = new ArrayList<>(ARRAY_SIZE);
        try {
            Reader fileReader = Files.newBufferedReader(pathFileForSave);
            CsvToBean<ImportObject> csvToBean = ConverterCsv.getCsvBean(ImportObject.class, fileReader);
            Iterator<ImportObject> iterator = csvToBean.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                importObjectList.add(iterator.next());
                i++;
                if (i == ARRAY_SIZE) {
                    importObjectService.saveImportObjects(importObjectList);
                    i = 0;
                    importObjectList = new ArrayList<>(ARRAY_SIZE);
                }
            }
            fileReader.close();
            System.out.println("Loading done");
            if (i != 0) {
                importObjectService.saveImportObjects(importObjectList);
            }
        } catch (PersistenceException e) {
            throw new ServiceException(String.format("Error occurred while writing the file - %s to DB", pathFileForSave));
        } catch (IOException e) {
            throw new ServiceException(String.format("Error occurred while reading the file - %s", pathFileForSave));
        }

    }
}
