package by.ilyushenko.tech.repository;

import by.ilyushenko.tech.model.ImportObject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ImportObjectRepositoryCustomImpl implements ImportObjectRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    @Override
    public List<ImportObject> getFilterImportObjectPage(final int offset, final int limit, final String filter) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ImportObject.class);
        Disjunction disjunction = Restrictions.or(
                Restrictions.ilike("eventName", filter, MatchMode.ANYWHERE),
                Restrictions.ilike("formName", filter, MatchMode.ANYWHERE),
                Restrictions.ilike("variableName", filter, MatchMode.ANYWHERE),
                Restrictions.ilike("value", filter, MatchMode.ANYWHERE)
        );
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        criteria.add(disjunction);
        criteria.addOrder(Order.asc("pid"));
        return criteria.list();
    }

    @Override
    public List<ImportObject> getImportObjectPage(final int offset, final int limit) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ImportObject.class);
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        criteria.addOrder(Order.asc("pid"));
        return criteria.list();
    }

    @Override
    public void saveImportObjects(final List<ImportObject> importObjects) {
        for (int i = 0; i < importObjects.size(); i++) {
            if (i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
            em.persist(importObjects.get(i));
        }
        importObjects.clear();
        System.gc();
    }
}
