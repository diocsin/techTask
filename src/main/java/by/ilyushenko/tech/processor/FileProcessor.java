package by.ilyushenko.tech.processor;

import by.ilyushenko.tech.exception.ExportException;
import by.ilyushenko.tech.exception.ImportException;
import by.ilyushenko.tech.model.ImportObject;
import by.ilyushenko.tech.service.ImportObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
            MyReader<ImportObject> reader = null;
            if (Pattern.matches(regularCsv, fileName)) {
                reader = new MyCsvReader<>(pathForProcess + "/" + fileName, ImportObject.class);
            } else if (Pattern.matches(regularJson, fileName)) {
                reader = new MyJsonReader<>(pathForProcess + "/" + fileName, ImportObject.class);
            }
            generalImport(reader);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void moveFile(final String path) throws ImportException {
        Path source = Paths.get(path);
        Path pathProcess = Paths.get(pathForProcess);
        try {
            //TODO костыль для работы нескольких экземпляров приложения.
            Files.move(source, pathProcess.resolve(source.getFileName()), StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new ImportException(String.format("The file - %s does not exist or has already been moved", source.getFileName()));
        }
    }

    private void generalImport(MyReader<ImportObject> reader) throws Exception {
        List<ImportObject> importObjectList = new ArrayList<>(ARRAY_SIZE);
        try {
            reader.readFile();
            int i = 0;
            while (reader.hasNext()) {
                ImportObject importObject = reader.doBean();
                importObjectList.add(importObject);
                i++;
                if (i == ARRAY_SIZE) {
                    importObjectService.saveImportObjects(importObjectList);
                    i = 0;
                    importObjectList = new ArrayList<>(ARRAY_SIZE);
                }
            }
            reader.closeRead();
            if (i != 0) {
                importObjectService.saveImportObjects(importObjectList);
            }
            System.out.println("Loading done");
        } catch (PersistenceException | DataAccessException e) {
            throw new ExportException(String.format("Error occurred while writing the file - %s to DB", reader.getPathStr()));
        } catch (IOException e) {
            throw new ImportException(String.format("Error occurred while reading the file - %s", reader.getPathStr()));
        }

    }

}
