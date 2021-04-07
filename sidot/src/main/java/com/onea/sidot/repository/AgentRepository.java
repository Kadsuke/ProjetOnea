package com.onea.sidot.repository;

import com.onea.sidot.domain.Agent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Agent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentRepository extends R2dbcRepository<Agent, Long>, AgentRepositoryInternal {
    Flux<Agent> findAllBy(Pageable pageable);

    @Query("SELECT * FROM agent entity WHERE entity.site_id = :id")
    Flux<Agent> findBySite(Long id);

    @Query("SELECT * FROM agent entity WHERE entity.site_id IS NULL")
    Flux<Agent> findAllWhereSiteIsNull();

    @Query("SELECT * FROM agent entity WHERE entity.site_id = :id")
    Flux<Agent> findBySite(Long id);

    @Query("SELECT * FROM agent entity WHERE entity.site_id IS NULL")
    Flux<Agent> findAllWhereSiteIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Agent> findAll();

    @Override
    Mono<Agent> findById(Long id);

    @Override
    <S extends Agent> Mono<S> save(S entity);
}

interface AgentRepositoryInternal {
    <S extends Agent> Mono<S> insert(S entity);
    <S extends Agent> Mono<S> save(S entity);
    Mono<Integer> update(Agent entity);

    Flux<Agent> findAll();
    Mono<Agent> findById(Long id);
    Flux<Agent> findAllBy(Pageable pageable);
    Flux<Agent> findAllBy(Pageable pageable, Criteria criteria);
}
