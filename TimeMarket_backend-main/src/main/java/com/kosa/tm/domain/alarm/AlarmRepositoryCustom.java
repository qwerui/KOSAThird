package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.product.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public interface AlarmRepositoryCustom {

    List<Alarm> findAlarmsByMembername(String memberName);
    List<Alarm> findAlarmsByEmail(String email);

    List<Alarm> findAlarmsByEmail2(String email);

    List<Alarm> findAllAlarmsByProduct(Product product);
}
