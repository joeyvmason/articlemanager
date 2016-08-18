package com.joeyvmason.articlemanager.core.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<ENTITY extends AuditableEntity> extends Repository<ENTITY, String> {

    <S extends ENTITY> S save(S entity);

    <S extends ENTITY> Iterable<S> save(Iterable<S> entities);

    ENTITY findOne(String id);

    List<ENTITY> findAll(Iterable<String> ids);

    Long count();

    boolean exists(String id);

    Page<ENTITY> findAll(Pageable pageable);

    void delete(ENTITY entity);

    void deleteAll();

    void delete(Iterable<? extends ENTITY> entities);

    void delete(String id);
}

