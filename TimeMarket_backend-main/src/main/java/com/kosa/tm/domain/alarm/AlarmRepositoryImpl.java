package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.product.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AlarmRepositoryImpl implements AlarmRepositoryCustom {


    @PersistenceContext
    private EntityManager entityManager;


    // memberName 을 이용하여 Alarm Title 조회
    @Override
    public List<Alarm> findAlarmsByMembername(String memberName) {

        String jpql = "SELECT a FROM Alarm a JOIN a.member m WHERE m.name = :memberName";
        TypedQuery<Alarm> query = entityManager.createQuery(jpql, Alarm.class);
        query.setParameter("memberName", memberName);

        return query.getResultList();
    }

    @Override
    public List<Alarm> findAlarmsByEmail(String email) {
        String jpql = "SELECT a FROM Alarm a JOIN a.member m WHERE m.email = :email AND a.isread = false AND a.time < :time ORDER BY a.time DESC";
        TypedQuery<Alarm> query = entityManager.createQuery(jpql, Alarm.class);
        query.setParameter("email", email);
        query.setParameter("time", LocalDateTime.now());
        return query.getResultList();
    }

    @Override
    public List<Alarm> findAlarmsByEmail2(String email) {
        String jpql = "SELECT a FROM Alarm a JOIN a.member m WHERE m.email = :email AND a.isread = false AND a.time > :time ORDER BY a.time DESC";
        TypedQuery<Alarm> query = entityManager.createQuery(jpql, Alarm.class);
        query.setParameter("email", email);
        query.setParameter("time", LocalDateTime.now());
        return query.getResultList();
    }

    @Override
    public List<Alarm> findAllAlarmsByProduct(Product product) {
        String jpql = "SELECT a FROM Alarm a JOIN a.member m WHERE a.product = :product";
        TypedQuery<Alarm> query = entityManager.createQuery(jpql, Alarm.class);
        query.setParameter("product", product);
        return query.getResultList();
    }
}
