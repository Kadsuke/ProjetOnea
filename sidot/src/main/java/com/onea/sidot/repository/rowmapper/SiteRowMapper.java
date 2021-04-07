package com.onea.sidot.repository.rowmapper;

import com.onea.sidot.domain.Site;
import com.onea.sidot.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Site}, with proper type conversions.
 */
@Service
public class SiteRowMapper implements BiFunction<Row, String, Site> {

    private final ColumnConverter converter;

    public SiteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Site} stored in the database.
     */
    @Override
    public Site apply(Row row, String prefix) {
        Site entity = new Site();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", String.class));
        entity.setResponsable(converter.fromRow(row, prefix + "_responsable", String.class));
        entity.setContact(converter.fromRow(row, prefix + "_contact", String.class));
        entity.setCentreId(converter.fromRow(row, prefix + "_centre_id", Long.class));
        return entity;
    }
}
