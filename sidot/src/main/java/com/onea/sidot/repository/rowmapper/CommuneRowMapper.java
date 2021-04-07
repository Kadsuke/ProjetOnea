package com.onea.sidot.repository.rowmapper;

import com.onea.sidot.domain.Commune;
import com.onea.sidot.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Commune}, with proper type conversions.
 */
@Service
public class CommuneRowMapper implements BiFunction<Row, String, Commune> {

    private final ColumnConverter converter;

    public CommuneRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Commune} stored in the database.
     */
    @Override
    public Commune apply(Row row, String prefix) {
        Commune entity = new Commune();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", String.class));
        entity.setProvinceId(converter.fromRow(row, prefix + "_province_id", Long.class));
        entity.setTypeCommuneId(converter.fromRow(row, prefix + "_type_commune_id", Long.class));
        return entity;
    }
}
