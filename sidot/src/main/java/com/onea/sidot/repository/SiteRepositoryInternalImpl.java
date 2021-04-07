package com.onea.sidot.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.onea.sidot.domain.Site;
import com.onea.sidot.repository.rowmapper.CentreRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Site entity.
 */
@SuppressWarnings("unused")
class SiteRepositoryInternalImpl implements SiteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CentreRowMapper centreMapper;
    private final SiteRowMapper siteMapper;

    private static final Table entityTable = Table.aliased("site", EntityManager.ENTITY_ALIAS);
    private static final Table centreTable = Table.aliased("centre", "centre");

    public SiteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CentreRowMapper centreMapper,
        SiteRowMapper siteMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.centreMapper = centreMapper;
        this.siteMapper = siteMapper;
    }

    @Override
    public Flux<Site> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Site> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Site> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = SiteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CentreSqlHelper.getColumns(centreTable, "centre"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(centreTable)
            .on(Column.create("centre_id", entityTable))
            .equals(Column.create("id", centreTable));

        String select = entityManager.createSelect(selectFrom, Site.class, pageable, criteria);
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
    public Flux<Site> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Site> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Site process(Row row, RowMetadata metadata) {
        Site entity = siteMapper.apply(row, "e");
        entity.setCentre(centreMapper.apply(row, "centre"));
        return entity;
    }

    @Override
    public <S extends Site> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Site> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Site with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Site entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class SiteSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("libelle", table, columnPrefix + "_libelle"));
        columns.add(Column.aliased("responsable", table, columnPrefix + "_responsable"));
        columns.add(Column.aliased("contact", table, columnPrefix + "_contact"));

        columns.add(Column.aliased("centre_id", table, columnPrefix + "_centre_id"));
        return columns;
    }
}
