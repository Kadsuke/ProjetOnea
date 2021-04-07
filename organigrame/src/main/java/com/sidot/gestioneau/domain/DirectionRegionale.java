package com.sidot.gestioneau.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DirectionRegionale.
 */
@Entity
@Table(name = "direction_regionale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DirectionRegionale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "responsable", nullable = false)
    private String responsable;

    @NotNull
    @Column(name = "contact", nullable = false)
    private String contact;

    @OneToMany(mappedBy = "directionRegionale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
