package by.ilyushenko.tech.service;

import by.ilyushenko.tech.model.ImportObject;

import java.util.List;

public interface ImportObjectService {

    List<ImportObject> findImportObject(final int offset, final int limit, final String filter);

    void saveImportObjects(List<ImportObject> importObjects);

}
