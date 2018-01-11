package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.share.CURDEntity;
import org.jzb.share.CURDEntityRepository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;


/**
 * Created by jzb on 17-4-15.
 */
public abstract class JpaCURDRepository<E extends CURDEntity<ID>, ID> implements CURDEntityRepository<E, ID> {
    @Inject
    protected EntityManager em;
    private Class<E> entiyClass;

    @PostConstruct
    void entiyClass() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entiyClass = (Class<E>) parameterizedType.getActualTypeArguments()[0];
    }

    public E save(E entity) {
        return em.merge(entity);
    }

    public E find(ID id) {
        return em.find(entiyClass, id);
    }

    public void delete(ID id) {
        E entity = find(id);
        entity.setDeleted(true);
        save(entity);
    }

    public Stream<E> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> resultCq = cb.createQuery(entiyClass).distinct(true);
        Root<E> root = resultCq.from(entiyClass);
        resultCq.select(root).where(cb.equal(root.get("deleted"), false));
        return em.createQuery(resultCq).getResultList().stream();
    }

    protected <R> R querySingle(TypedQuery<R> typedQuery) {
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
