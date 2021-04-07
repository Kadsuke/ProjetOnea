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
 * A CentreRegroupement.
 */
@Entity
@Table(name = "centre_regroupement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CentreRegroupement implements Serializable {

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

    @OneToMany(mappedBy = "centreRegroupement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sites", "centreRegroupement" }, allowSetters = true)
    private Set<Centre> centres = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "centreRegroupements" }, allowSetters = true)
    private DirectionRegionale directionRegionale;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CentreRegroupement id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public CentreRegroupement libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getResponsable() {
        return this.responsable;
    }

    public CentreRegroupement responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getContact() {
        return this.contact;
    }

    public CentreRegroupement contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<Centre> getCentres() {
        return this.centres;
    }

    public CentreRegroupement centres(Set<Centre> centres) {
        this.setCentres(centres);
        return this;
    }

    public CentreRegroupement addCentre(Centre centre) {
        this.centres.add(centre);
        centre.setCentreRegroupement(this);
        return this;
    }

    public CentreRegroupement removeCentre(Centre centre) {
        this.centres.remove(centre);
        centre.setCentreRegroupement(null);
        return this;
    }

    public void setCentres(Set<Centre> centres) {
        if (this.centres != null) {
            this.centres.forEach(i -> i.setCentreRegroupement(null));
        }
        if (centres != null) {
            centres.forEach(i -> i.setCentreRegroupement(this));
        }
        this.centres = centres;
    }

    public DirectionRegionale getDirectionRegionale() {
        return this.directionRegionale;
    }

    public CentreRegroupement directionRegionale(DirectionRegionale directionRegionale) {
        this.setDirectionRegionale(directionRegionale);
        return this;
    }

    public void setDirectionRegionale(DirectionRegionale directionRegionale) {
        this.directionRegionale = directionRegionale;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CentreRegroupement)) {
            return false;
        }
        return id != null && id.equals(((CentreRegroupement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CentreRegroupement{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
