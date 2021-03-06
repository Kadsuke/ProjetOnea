package com.onea.referentiel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Section.
 */
@Entity
@Table(name = "section")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "section")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parcelles", "section" }, allowSetters = true)
    private Set<Lot> lots = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sections", "localite" }, allowSetters = true)
    private Secteur secteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Section id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Section libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Lot> getLots() {
        return this.lots;
    }

    public Section lots(Set<Lot> lots) {
        this.setLots(lots);
        return this;
    }

    public Section addLot(Lot lot) {
        this.lots.add(lot);
        lot.setSection(this);
        return this;
    }

    public Section removeLot(Lot lot) {
        this.lots.remove(lot);
        lot.setSection(null);
        return this;
    }

    public void setLots(Set<Lot> lots) {
        if (this.lots != null) {
            this.lots.forEach(i -> i.setSection(null));
        }
        if (lots != null) {
            lots.forEach(i -> i.setSection(this));
        }
        this.lots = lots;
    }

    public Secteur getSecteur() {
        return this.secteur;
    }

    public Section secteur(Secteur secteur) {
        this.setSecteur(secteur);
        return this;
    }

    public void setSecteur(Secteur secteur) {
        this.secteur = secteur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        return id != null && id.equals(((Section) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Section{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
