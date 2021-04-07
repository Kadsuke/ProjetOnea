package com.onea.sidot.repository.rowmapper;

import com.onea.sidot.domain.Agent;
import com.onea.sidot.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Agent}, with proper type conversions.
 */
@Service
public class AgentRowMapper implements BiFunction<Row, String, Agent> {

    private final ColumnConverter converter;

    public AgentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Agent} stored in the database.
     */
    @Override
    public Agent apply(Row row, String prefix) {
        Agent entity = new Agent();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setNumero(converter.fromRow(row, prefix + "_numero", String.class));
        entity.setRole(converter.fromRow(row, prefix + "_role", String.class));
        entity.setSiteId(converter.fromRow(row, prefix + "_site_id", Long.class));
        entity.setSiteId(converter.fromRow(row, prefix + "_site_id", Long.class));
        return entity;
    }
}
