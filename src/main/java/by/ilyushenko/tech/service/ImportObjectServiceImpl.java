package by.ilyushenko.tech.service;

import by.ilyushenko.tech.model.ImportObject;
import by.ilyushenko.tech.repository.ImportObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportObjectServiceImpl implements ImportObjectService {

    private ImportObjectRepository repository;

    @Autowired
    public ImportObjectServiceImpl(ImportObjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ImportObject> findImportObject(int offset, int limit, String filter) {
        if (filter == null || filter.length() == 0) {
            return repository.getImportObjectPage(offset, limit);
        }
        return repository.getFilterImportObjectPage(offset, limit, filter);
    }

    @Override
    public List<ImportObject> saveImportObjects(List<ImportObject> importObjects) {
        return repository.saveAll(importObjects);
    }

    @Override
    public ImportObject saveImportObject(ImportObject importObject) {
        return repository.save(importObject);
    }
}
