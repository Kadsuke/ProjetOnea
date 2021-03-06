package com.onea.sidot.repository.rowmapper;

import com.onea.sidot.domain.Parcelle;
import com.onea.sidot.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Parcelle}, with proper type conversions.
 */
@Service
public class ParcelleRowMapper implements BiFunction<Row, String, Parcelle> {

    private final ColumnConverter converter;

    public ParcelleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Parcelle} stored in the database.
     */
    @Override
    public Parcelle apply(Row row, String prefix) {
        Parcelle entity = new Parcelle();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", String.class));
        entity.setLotId(converter.fromRow(row, prefix + "_lot_id", Long.class));
        return entity;
    }
}
