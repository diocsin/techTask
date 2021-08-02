package by.ilyushenko.tech.service;

import by.ilyushenko.tech.model.ImportObject;
import by.ilyushenko.tech.repository.ImportObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImportObjectServiceImpl implements ImportObjectService {

    private final ImportObjectRepository repository;

    @Autowired
    public ImportObjectServiceImpl(ImportObjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ImportObject> findImportObject(final int offset, final int limit, final String filter) {
        if (filter == null || filter.length() == 0) {
            return repository.getImportObjectPage(offset, limit);
        }
        return repository.getFilterImportObjectPage(offset, limit, filter);
    }

    @Async
    @Transactional
    @Override
    public void saveImportObjects(final List<ImportObject> importObjects) {
        repository.saveImportObjects(importObjects);
    }
}
