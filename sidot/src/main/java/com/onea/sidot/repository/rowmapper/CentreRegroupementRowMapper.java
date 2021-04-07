package com.onea.sidot.repository.rowmapper;

import com.onea.sidot.domain.CentreRegroupement;
import com.onea.sidot.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CentreRegroupement}, with proper type conversions.
 */
@Service
public class CentreRegroupementRowMapper implements BiFunction<Row, String, CentreRegroupement> {

    private final ColumnConverter converter;

    public CentreRegroupementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CentreRegroupement} stored in the database.
     */
    @Override
    public CentreRegroupement apply(Row row, String prefix) {
        CentreRegroupement entity = new CentreRegroupement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", String.class));
        entity.setResponsable(converter.fromRow(row, prefix + "_responsable", String.class));
        entity.setContact(converter.fromRow(row, prefix + "_contact", String.class));
        entity.setDirectionRegionaleId(converter.fromRow(row, prefix + "_direction_regionale_id", Long.class));
        return entity;
    }
}
