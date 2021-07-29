package by.ilyushenko.tech.repository;

import by.ilyushenko.tech.model.ImportObject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ImportObjectRepositoryCustomImpl implements ImportObjectRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ImportObject> getFilterImportObjectPage(int offset, int limit, String filter) {
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
        return criteria.list();
    }

    @Override
    public List<ImportObject> getImportObjectPage(int offset, int limit) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ImportObject.class);
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        return criteria.list();
    }
}
