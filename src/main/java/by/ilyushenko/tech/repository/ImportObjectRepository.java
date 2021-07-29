package by.ilyushenko.tech.repository;

import by.ilyushenko.tech.model.ImportObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportObjectRepository extends JpaRepository<ImportObject, Long>, ImportObjectRepositoryCustom {

}
