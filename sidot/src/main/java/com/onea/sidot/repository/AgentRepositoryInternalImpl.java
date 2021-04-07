package com.onea.sidot.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.onea.sidot.domain.Agent;
import com.onea.sidot.repository.rowmapper.AgentRowMapper;
import com.onea.sidot.repository.rowmapper.SiteRowMapper;
import com.onea.sidot.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Agent entity.
 */
@SuppressWarnings("unused")
class AgentRepositoryInternalImpl implements AgentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SiteRowMapper siteMapper;
    private final AgentRowMapper agentMapper;

    private static final Table entityTable = Table.aliased("agent", EntityManager.ENTITY_ALIAS);
    private static final Table siteTable = Table.aliased("site", "site");
    private static final Table siteTable = Table.aliased("site", "site");

    public AgentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SiteRowMapper siteMapper,
        AgentRowMapper agentMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.siteMapper = siteMapper;
        this.agentMapper = agentMapper;
    }

    @Override
    public Flux<Agent> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Agent> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Agent> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = AgentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SiteSqlHelper.getColumns(siteTable, "site"));
        columns.addAll(SiteSqlHelper.getColumns(siteTable, "site"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(siteTable)
            .on(Column.create("site_id", entityTable))
            .equals(Column.create("id", siteTable))
            .leftOuterJoin(siteTable)
            .on(Column.create("site_id", entityTable))
            .equals(Column.create("id", siteTable));

        String select = entityManager.createSelect(selectFrom, Agent.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit ->
                    new StringBuilder(select)
                        .append(" ")
                        .append("WHERE")
                        .append(" ")
                        .append(alias)
                        .append(".")
                        .append(crit.toString())
                        .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<Agent> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Agent> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Agent process(Row row, RowMetadata metadata) {
        Agent entity = agentMapper.apply(row, "e");
        entity.setSite(siteMapper.apply(row, "site"));
        entity.setSite(siteMapper.apply(row, "site"));
        return entity;
    }

    @Override
    public <S extends Agent> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Agent> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Agent with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Agent entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class AgentSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom", table, columnPrefix + "_nom"));
        columns.add(Column.aliased("numero", table, columnPrefix + "_numero"));
        columns.add(Column.aliased("role", table, columnPrefix + "_role"));

        columns.add(Column.aliased("site_id", table, columnPrefix + "_site_id"));
        columns.add(Column.aliased("site_id", table, columnPrefix + "_site_id"));
        return columns;
    }
}
