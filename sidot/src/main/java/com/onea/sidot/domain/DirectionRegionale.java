package com.onea.sidot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DirectionRegionale.
 */
@Table("direction_regionale")
public class DirectionRegionale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    private String libelle;

    @NotNull(message = "must not be null")
    @Column("responsable")
    private String responsable;

    @NotNull(message = "must not be null")
    @Column("contact")
    private String contact;

    @Transient
    @JsonIgnoreProperties(value = { "centres", "directionRegionale" }, allowSetters = true)
    private Set<CentreRegroupement> centreRegroupements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DirectionRegionale id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public DirectionRegionale libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getResponsable() {
        return this.responsable;
    }

    public DirectionRegionale responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getContact() {
        return this.contact;
    }

    public DirectionRegionale contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<CentreRegroupement> getCentreRegroupements() {
        return this.centreRegroupements;
    }

    public DirectionRegionale centreRegroupements(Set<CentreRegroupement> centreRegroupements) {
        this.setCentreRegroupements(centreRegroupements);
        return this;
    }

    public DirectionRegionale addCentreRegroupement(CentreRegroupement centreRegroupement) {
        this.centreRegroupements.add(centreRegroupement);
        centreRegroupement.setDirectionRegionale(this);
        return this;
    }

    public DirectionRegionale removeCentreRegroupement(CentreRegroupement centreRegroupement) {
        this.centreRegroupements.remove(centreRegroupement);
        centreRegroupement.setDirectionRegionale(null);
        return this;
    }

    public void setCentreRegroupements(Set<CentreRegroupement> centreRegroupements) {
        if (this.centreRegroupements != null) {
            this.centreRegroupements.forEach(i -> i.setDirectionRegionale(null));
        }
        if (centreRegroupements != null) {
            centreRegroupements.forEach(i -> i.setDirectionRegionale(this));
        }
        this.centreRegroupements = centreRegroupements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectionRegionale)) {
            return false;
        }
        return id != null && id.equals(((DirectionRegionale) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DirectionRegionale{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
