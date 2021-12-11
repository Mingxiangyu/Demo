package com.iglens.jpa;

import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/** @author xming */
public class BaseJpaRepositoryImpl<T> extends SimpleJpaRepository<T, Long>
    implements BaseJpaRepository<T> {

  private final JpaEntityInformation<T, ?> entityInformation;
  private final EntityManager em;

  public BaseJpaRepositoryImpl(
      JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {

    super(entityInformation, entityManager);

    this.entityInformation = entityInformation;
    this.em = entityManager;
  }

  public BaseJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
    this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
  }
}
