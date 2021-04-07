package com.onea.sidot.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.domain.CentreRegroupement} entity.
 */
public class CentreRegroupementDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String libelle;

    @NotNull(message = "must not be null")
    private String responsable;

    @NotNull(message = "must not be null")
    private String contact;

    private DirectionRegionaleDTO directionRegionale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public DirectionRegionaleDTO getDirectionRegionale() {
        return directionRegionale;
    }

    public void setDirectionRegionale(DirectionRegionaleDTO directionRegionale) {
        this.directionRegionale = directionRegionale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CentreRegroupementDTO)) {
            return false;
        }

        CentreRegroupementDTO centreRegroupementDTO = (CentreRegroupementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, centreRegroupementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CentreRegroupementDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", contact='" + getContact() + "'" +
            ", directionRegionale=" + getDirectionRegionale() +
            "}";
    }
}
