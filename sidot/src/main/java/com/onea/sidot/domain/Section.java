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
 * A Section.
 */
@Table("section")
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    private String libelle;

    @Transient
    @JsonIgnoreProperties(value = { "parcelles", "section" }, allowSetters = true)
    private Set<Lot> lots = new HashSet<>();

    @JsonIgnoreProperties(value = { "sections", "localite" }, allowSetters = true)
    @Transient
    private Secteur secteur;

    @Column("secteur_id")
    private Long secteurId;

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
        this.secteurId = secteur != null ? secteur.getId() : null;
        return this;
    }

    public void setSecteur(Secteur secteur) {
        this.secteur = secteur;
        this.secteurId = secteur != null ? secteur.getId() : null;
    }

    public Long getSecteurId() {
        return this.secteurId;
    }

    public void setSecteurId(Long secteur) {
        this.secteurId = secteur;
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
