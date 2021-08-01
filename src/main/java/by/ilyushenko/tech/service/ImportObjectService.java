package by.ilyushenko.tech.service;

import by.ilyushenko.tech.model.ImportObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ImportObjectService {

    List<ImportObject> findImportObject(final int offset, final int limit, final String filter);

    void saveImportObjects(List<ImportObject> importObjects) throws IOException, SQLException;

}
