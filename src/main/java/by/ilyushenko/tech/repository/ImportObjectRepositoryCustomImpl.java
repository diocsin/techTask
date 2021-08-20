package by.ilyushenko.tech.repository;

import by.ilyushenko.tech.model.ImportObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ImportObjectRepositoryCustomImpl implements ImportObjectRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    @Override
    public List<ImportObject> getFilterImportObjectPage(final int offset, final int limit, final String filter) {
        final CriteriaBuilder criteria = em.getCriteriaBuilder();
        final CriteriaQuery<ImportObject> cq = criteria.createQuery(ImportObject.class);
        final Root<ImportObject> root = cq.from(ImportObject.class);
        final Predicate disjunction = criteria.disjunction();
        disjunction.getExpressions().add(criteria.like(root.get("eventName"), "%" + filter + "%"));
        disjunction.getExpressions().add(criteria.like(root.get("formName"), "%" + filter + "%"));
        disjunction.getExpressions().add(criteria.like(root.get("variableName"), "%" + filter + "%"));
        disjunction.getExpressions().add(criteria.like(root.get("value"), "%" + filter + "%"));
        cq.orderBy(criteria.asc(root.get("pid")));
        return em.createQuery(cq).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    @Override
    public List<ImportObject> getImportObjectPage(final int offset, final int limit) {
        final CriteriaBuilder criteria = em.getCriteriaBuilder();
        final CriteriaQuery<ImportObject> cq = criteria.createQuery(ImportObject.class);
        final Root<ImportObject> root = cq.from(ImportObject.class);
        cq.orderBy(criteria.asc(root.get("pid")));
        return em.createQuery(cq).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    @Override
    public void saveImportObjects(final List<ImportObject> importObjects) {
        for (int i = 0; i < importObjects.size(); i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
            em.persist(importObjects.get(i));
        }
        em.flush();
        em.clear();
        importObjects.clear();
    }
}
