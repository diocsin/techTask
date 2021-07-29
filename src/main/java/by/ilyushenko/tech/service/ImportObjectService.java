package by.ilyushenko.tech.service;

import by.ilyushenko.tech.model.ImportObject;

import java.util.List;

public interface ImportObjectService {

    List<ImportObject> findImportObject(int offset, int limit, String filter);

    List<ImportObject> saveImportObjects(List<ImportObject> importObjects);

    ImportObject saveImportObject(ImportObject importObject);
}
