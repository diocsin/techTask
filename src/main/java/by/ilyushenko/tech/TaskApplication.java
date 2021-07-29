package by.ilyushenko.tech;

import by.ilyushenko.tech.model.ImportObject;
import by.ilyushenko.tech.service.ImportObjectService;
import by.ilyushenko.tech.util.ConverterCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskApplication implements CommandLineRunner {

    @Autowired
    private ImportObjectService importObjectService;

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        importObjectService.saveImportObjects(ConverterCsv.convertCsv(ImportObject.class, "myFile0.csv"));
    }
}
