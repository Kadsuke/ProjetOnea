package com.onea.sidot.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.domain.Centre} entity.
 */
public class CentreDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String libelle;

    @NotNull(message = "must not be null")
    private String responsable;

    @NotNull(message = "must not be null")
    private String contact;

    private CentreRegroupementDTO centreRegroupement;

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

    public CentreRegroupementDTO getCentreRegroupement() {
        return centreRegroupement;
    }

    public void setCentreRegroupement(CentreRegroupementDTO centreRegroupement) {
        this.centreRegroupement = centreRegroupement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CentreDTO)) {
            return false;
        }

        CentreDTO centreDTO = (CentreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, centreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CentreDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", contact='" + getContact() + "'" +
            ", centreRegroupement=" + getCentreRegroupement() +
            "}";
    }
}
