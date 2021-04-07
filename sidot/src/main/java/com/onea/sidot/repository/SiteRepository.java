package com.onea.sidot.repository;

import com.onea.sidot.domain.Site;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Site entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteRepository extends R2dbcRepository<Site, Long>, SiteRepositoryInternal {
    Flux<Site> findAllBy(Pageable pageable);

    @Query("SELECT * FROM site entity WHERE entity.centre_id = :id")
    Flux<Site> findByCentre(Long id);

    @Query("SELECT * FROM site entity WHERE entity.centre_id IS NULL")
    Flux<Site> findAllWhereCentreIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Site> findAll();

    @Override
    Mono<Site> findById(Long id);

    @Override
    <S extends Site> Mono<S> save(S entity);
}

interface SiteRepositoryInternal {
    <S extends Site> Mono<S> insert(S entity);
    <S extends Site> Mono<S> save(S entity);
    Mono<Integer> update(Site entity);

    Flux<Site> findAll();
    Mono<Site> findById(Long id);
    Flux<Site> findAllBy(Pageable pageable);
    Flux<Site> findAllBy(Pageable pageable, Criteria criteria);
}
