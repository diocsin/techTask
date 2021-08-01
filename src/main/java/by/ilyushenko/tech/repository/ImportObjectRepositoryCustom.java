package by.ilyushenko.tech.repository;

import by.ilyushenko.tech.model.ImportObject;

import java.util.List;

public interface ImportObjectRepositoryCustom {

    List<ImportObject> getFilterImportObjectPage(final int offset, final int limit, final String filter);

    List<ImportObject> getImportObjectPage(final int offset, final int limit);

    void saveImportObjects(final List<ImportObject> importObjects);
}
