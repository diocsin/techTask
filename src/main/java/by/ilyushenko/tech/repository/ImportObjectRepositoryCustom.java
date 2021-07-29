package by.ilyushenko.tech.repository;

import by.ilyushenko.tech.model.ImportObject;

import java.util.List;

public interface ImportObjectRepositoryCustom {

    List<ImportObject> getFilterImportObjectPage(int offset, int limit, String filter);

    List<ImportObject> getImportObjectPage(int offset, int limit);
}
