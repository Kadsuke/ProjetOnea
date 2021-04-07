package com.onea.sidot.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.onea.sidot.domain.Section;
import com.onea.sidot.repository.rowmapper.SecteurRowMapper;
import com.onea.sidot.repository.rowmapper.SectionRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Section entity.
 */
@SuppressWarnings("unused")
class SectionRepositoryInternalImpl implements SectionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SecteurRowMapper secteurMapper;
    private final SectionRowMapper sectionMapper;

    private static final Table entityTable = Table.aliased("section", EntityManager.ENTITY_ALIAS);
    private static final Table secteurTable = Table.aliased("secteur", "secteur");

    public SectionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SecteurRowMapper secteurMapper,
        SectionRowMapper sectionMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.secteurMapper = secteurMapper;
        this.sectionMapper = sectionMapper;
    }

    @Override
    public Flux<Section> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Section> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Section> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = SectionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SecteurSqlHelper.getColumns(secteurTable, "secteur"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(secteurTable)
            .on(Column.create("secteur_id", entityTable))
            .equals(Column.create("id", secteurTable));

        String select = entityManager.createSelect(selectFrom, Section.class, pageable, criteria);
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
    public Flux<Section> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Section> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Section process(Row row, RowMetadata metadata) {
        Section entity = sectionMapper.apply(row, "e");
        entity.setSecteur(secteurMapper.apply(row, "secteur"));
        return entity;
    }

    @Override
    public <S extends Section> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Section> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Section with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Section entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class SectionSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("libelle", table, columnPrefix + "_libelle"));

        columns.add(Column.aliased("secteur_id", table, columnPrefix + "_secteur_id"));
        return columns;
    }
}
