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
 * A Localite.
 */
@Table("localite")
public class Localite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    private String libelle;

    @Transient
    @JsonIgnoreProperties(value = { "sections", "localite" }, allowSetters = true)
    private Set<Secteur> secteurs = new HashSet<>();

    @JsonIgnoreProperties(value = { "localites", "province", "typeCommune" }, allowSetters = true)
    @Transient
    private Commune commune;

    @Column("commune_id")
    private Long communeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Localite id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Localite libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Secteur> getSecteurs() {
        return this.secteurs;
    }

    public Localite secteurs(Set<Secteur> secteurs) {
        this.setSecteurs(secteurs);
        return this;
    }

    public Localite addSecteur(Secteur secteur) {
        this.secteurs.add(secteur);
        secteur.setLocalite(this);
        return this;
    }

    public Localite removeSecteur(Secteur secteur) {
        this.secteurs.remove(secteur);
        secteur.setLocalite(null);
        return this;
    }

    public void setSecteurs(Set<Secteur> secteurs) {
        if (this.secteurs != null) {
            this.secteurs.forEach(i -> i.setLocalite(null));
        }
        if (secteurs != null) {
            secteurs.forEach(i -> i.setLocalite(this));
        }
        this.secteurs = secteurs;
    }

    public Commune getCommune() {
        return this.commune;
    }

    public Localite commune(Commune commune) {
        this.setCommune(commune);
        this.communeId = commune != null ? commune.getId() : null;
        return this;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
        this.communeId = commune != null ? commune.getId() : null;
    }

    public Long getCommuneId() {
        return this.communeId;
    }

    public void setCommuneId(Long commune) {
        this.communeId = commune;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Localite)) {
            return false;
        }
        return id != null && id.equals(((Localite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Localite{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
